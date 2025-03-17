package br.com.poc.fs.service;

import br.com.poc.fs.enums.EOrderStatus;
import br.com.poc.fs.exception.NoSuchElementFoundException;
import br.com.poc.fs.models.Order;
import br.com.poc.fs.models.Product;
import br.com.poc.fs.models.User;
import br.com.poc.fs.payload.request.OrderRequest;
import br.com.poc.fs.payload.response.OrderResponse;
import br.com.poc.fs.repository.OrderRepository;
import br.com.poc.fs.repository.ProductRepository;
import br.com.poc.fs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    void testFindById_WhenOrderExists() {

        String orderId = "123";
        String username = "testUser";
        when(userDetails.getUsername()).thenReturn(username);

        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setUser(new User(username, "email@test.com", null, Collections.emptySet()));

        when(orderRepository.findByIdAndUserUserName(orderId, username)).thenReturn(Optional.of(mockOrder));

        OrderResponse result = orderService.findById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).findByIdAndUserUserName(orderId, username);
    }

    @Test
    void testSave_WhenProductsAreEmpty() {

        OrderRequest request = new OrderRequest();
        request.setProductsId(new HashSet<>());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.save(request);
        });
        assertEquals("Products list is empty", exception.getMessage());
    }

    @Test
    void testSave_WhenValidRequest() {

        OrderRequest request = new OrderRequest();
        Set<String> productIds = Set.of("prod1", "prod2");
        request.setProductsId(productIds);
        when(productRepository.findByIdIn(productIds)).thenReturn(List.of(
                new Product("prod1", "Product 1", 1, BigDecimal.TEN, null),
                new Product("prod2", "Product 2", 1, BigDecimal.TEN, null)
        ));

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(new User("testUser", "email@test.com", null, Collections.emptySet()));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse result = orderService.save(request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(20), result.getTotal());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).findByIdIn(productIds);
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void testUpdateStatus_WhenOrderExists() {

        String orderId = "123";
        EOrderStatus newStatus = EOrderStatus.COMPLETED;

        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setUser(new User("testUser", "", null, Collections.emptySet()));
        mockOrder.setStatus(EOrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(mockOrder)).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse result = orderService.updateStatus(orderId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testDelete() {
        String orderId = "123";

        doNothing().when(orderRepository).deleteById(orderId);

        orderService.delete(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
