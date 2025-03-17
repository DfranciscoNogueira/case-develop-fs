package br.com.poc.fs.payload.request;

import java.math.BigDecimal;

public class ProductRequest {

    private String name;

    private BigDecimal price;

    private String description;

    private int quantity;

    private String idCategory;

    public ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price, String description, int quantity, String idCategory) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

}
