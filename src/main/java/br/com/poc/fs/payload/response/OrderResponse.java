package br.com.poc.fs.payload.response;

import br.com.poc.fs.enums.EOrderStatus;

import java.math.BigDecimal;
import java.util.Set;

public class OrderResponse {

    private String id;

    private UserResponse user;

    private EOrderStatus status;

    private Set<ProductResponse> products;

    private BigDecimal total;

    public OrderResponse() {
    }

    public OrderResponse(String id, UserResponse user, EOrderStatus status, Set<ProductResponse> products, BigDecimal total) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.products = products;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public void setStatus(EOrderStatus status) {
        this.status = status;
    }

    public Set<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductResponse> products) {
        this.products = products;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
