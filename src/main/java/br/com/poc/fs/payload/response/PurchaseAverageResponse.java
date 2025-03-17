package br.com.poc.fs.payload.response;

public class PurchaseAverageResponse {

    private String idUser;
    private String name;
    private String email;
    private Double totalPurchases;
    private Integer numberOfOrders;
    private Double ticketAverage;

    public PurchaseAverageResponse(String idUser, String name, String email, Double totalPurchases, Integer numberOfOrders, Double ticketAverage) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.totalPurchases = totalPurchases;
        this.numberOfOrders = numberOfOrders;
        this.ticketAverage = ticketAverage;
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

    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public Double getTicketAverage() {
        return ticketAverage;
    }

    public void setTicketAverage(Double ticketAverage) {
        this.ticketAverage = ticketAverage;
    }

}
