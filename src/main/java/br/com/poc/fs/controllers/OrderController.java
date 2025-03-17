package br.com.poc.fs.controllers;

import br.com.poc.fs.payload.request.OrderRequest;
import br.com.poc.fs.payload.response.OrderResponse;
import br.com.poc.fs.payload.response.PurchaseAverageResponse;
import br.com.poc.fs.payload.response.TopFiveBuyResponse;
import br.com.poc.fs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> list() {
        return this.orderService.listAll();
    }

    @PostMapping
    public ResponseEntity<OrderResponse> save(@RequestBody OrderRequest request) {
        OrderResponse response = this.orderService.save(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> byId(@PathVariable String id) {
        OrderResponse response = this.orderService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.orderService.delete(id);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> pay(@PathVariable String id) {
        OrderResponse response = this.orderService.pay(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/top-five-purchases/details")
    public List<TopFiveBuyResponse> getTopFiveUsersWithDetails() {
        return this.orderService.getTopFiveUsersWithDetails();
    }

    @GetMapping("/ticket-average")
    public List<PurchaseAverageResponse> calculateAverageTicketPerUser() {
        return this.orderService.calculateAverageTicketPerUser();
    }

    @GetMapping("/monthly-billing")
    public Map<String, Object> calculateMonthlyBilling() {
        Double totalInvoiced = this.orderService.calculateMonthlyBilling();
        Map<String, Object> response = new HashMap<>();
        response.put("month", LocalDate.now().getMonth().toString());
        response.put("year", LocalDate.now().getYear());
        response.put("total_invoiced", totalInvoiced);
        return response;
    }

}
