package br.com.poc.fs.payload.request;

import java.util.Set;

public class OrderRequest {

    private Set<String> productsId;

    public OrderRequest() {
    }

    public OrderRequest(Set<String> productsId) {
        this.productsId = productsId;
    }

    public Set<String> getProductsId() {
        return productsId;
    }

    public void setProductsId(Set<String> productsId) {
        this.productsId = productsId;
    }

}
