package br.com.poc.fs.payload.response;

public class TopFiveBuyResponse {

    private String idUser;
    private String name;
    private String email;
    private Double totalPurchases;

    public TopFiveBuyResponse(String idUser, String name, String email, Double totalPurchases) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.totalPurchases = totalPurchases;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(Double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

}
