package br.com.poc.fs.service;

import br.com.poc.fs.enums.EOrderStatus;
import br.com.poc.fs.exception.BusinessException;
import br.com.poc.fs.exception.NoSuchElementFoundException;
import br.com.poc.fs.models.Order;
import br.com.poc.fs.models.Product;
import br.com.poc.fs.models.User;
import br.com.poc.fs.payload.request.OrderRequest;
import br.com.poc.fs.payload.response.OrderResponse;
import br.com.poc.fs.payload.response.ProductResponse;
import br.com.poc.fs.payload.response.PurchaseAverageResponse;
import br.com.poc.fs.payload.response.TopFiveBuyResponse;
import br.com.poc.fs.payload.response.UserResponse;
import br.com.poc.fs.repository.OrderRepository;
import br.com.poc.fs.repository.ProductRepository;
import br.com.poc.fs.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public OrderResponse findById(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Order order = this.orderRepository.findByIdAndUserUserName(id, principal.getUsername()).orElseThrow(() -> new NoSuchElementFoundException(String.format("Order with id %s not found", id)));
        return this.convertToResponse(order);
    }

    public List<OrderResponse> listAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        List<Order> list = this.orderRepository.findByUserUserName(principal.getUsername());
        return list.stream().map(this::convertToResponse).toList();
    }

    public OrderResponse save(OrderRequest request) {

        if (Objects.isNull(request.getProductsId()) || request.getProductsId().isEmpty())
            throw new IllegalArgumentException("Products list is empty");

        Order order = this.convertToEntity(request);
        Order entitySave = this.orderRepository.save(order);
        return this.convertToResponse(entitySave);
    }

    private OrderResponse convertToResponse(Order order) {
        Set<ProductResponse> productResponses = order.getProducts().stream().map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice())).collect(Collectors.toSet());
        UserResponse userResponse = new UserResponse(order.getUser().getUserName(), order.getUser().getEmail());
        return new OrderResponse(order.getId(), userResponse, order.getStatus(), productResponses, order.getTotal());
    }

    public OrderResponse updateStatus(String id, EOrderStatus status) {
        Order order = this.orderRepository.findById(id).orElseThrow(() -> new NoSuchElementFoundException(String.format("Order with id %s not found", id)));
        order.setStatus(status);
        return this.convertToResponse(this.orderRepository.save(order));
    }

    public Double calculateMonthlyBilling() {
        Double totalInvoiced = this.orderRepository.calculateMonthlyBilling();
        return Objects.nonNull(totalInvoiced) ? totalInvoiced : 0.0;
    }

    public List<PurchaseAverageResponse> calculateAverageTicketPerUser() {

        List<Object[]> results = this.orderRepository.calculateAverageTicketPerUser();

        return results.stream().map(result -> {
            String idUser = Objects.nonNull(result[0]) ? result[0].toString() : null;
            String name = Objects.nonNull(result[1]) ? result[1].toString() : null;
            String email = Objects.nonNull(result[2]) ? result[2].toString() : null;
            double totalPurchases = Objects.nonNull(result[3]) ? Double.parseDouble(result[3].toString()) : 0;
            int numberOfOrders = Objects.nonNull(result[4]) ? Integer.parseInt(result[4].toString()) : 0;
            double ticketAverage = Objects.nonNull(result[5]) ? Double.parseDouble(result[5].toString()) : 0;
            return new PurchaseAverageResponse(idUser, name, email, totalPurchases, numberOfOrders, ticketAverage);
        }).toList();
    }

    public List<TopFiveBuyResponse> getTopFiveUsersWithDetails() {

        List<Object[]> results = this.orderRepository.findTopFiveUsersByTotalPurchases();

        return results.stream().map(result -> {
            String idUser = Objects.nonNull(result[0]) ? result[0].toString() : null;
            String name = Objects.nonNull(result[1]) ? result[1].toString() : null;
            String email = Objects.nonNull(result[2]) ? result[2].toString() : null;
            double totalPurchases = Objects.nonNull(result[3]) ? Double.parseDouble(result[3].toString()) : 0;
            return new TopFiveBuyResponse(idUser, name, email, totalPurchases);
        }).toList();
    }

    public void delete(String id) {
        this.orderRepository.deleteById(id);
    }

    private Order convertToEntity(OrderRequest request) {

        Order order = new Order(EOrderStatus.PENDING);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = this.userRepository.findByUserName(principal.getUsername());
        order.setUser(user);

        Set<Product> products = request.getProductsId().stream().map(Product::new).collect(Collectors.toSet());
        order.setProducts(products);

        BigDecimal total = this.productRepository.findByIdIn(request.getProductsId()).stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        return order;
    }

    public OrderResponse pay(String id) {

        if (Objects.isNull(id) || id.isBlank())
            throw new IllegalArgumentException("Id is empty");

        if (!this.orderRepository.existsById(id))
            throw new NoSuchElementFoundException(String.format("Order with id %s not found", id));

        if (!this.findById(id).getStatus().equals(EOrderStatus.PENDING)) {
            throw new IllegalArgumentException("Only orders with PENDING status can be paid.");
        }

        Order order = this.orderRepository.findById(id).orElse(null);

        if (Objects.isNull(order)) {
            throw new NoSuchElementFoundException(String.format("Order with id %s not found", id));
        }

        List<Product> productsFromOrder = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : order.getProductsQuantity().entrySet()) {

            Product product = this.productRepository.findById(entry.getKey().getId()).orElseThrow(() -> new NoSuchElementFoundException("Product not found"));

            Integer quantity = entry.getValue();

            if (product.getQuantity() < quantity) {
                this.updateStatus(id, EOrderStatus.CANCELED);
                throw new BusinessException("Insufficient stock for the product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - quantity);
            productsFromOrder.add(product);
        }

        this.productRepository.saveAll(productsFromOrder);
        return this.updateStatus(id, EOrderStatus.COMPLETED);
    }

}
