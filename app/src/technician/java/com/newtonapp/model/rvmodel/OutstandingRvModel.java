package com.newtonapp.model.rvmodel;

public class OutstandingRvModel {

    private String idProblem;
    private String customer;
    private String address;

    public OutstandingRvModel(String idProblem, String customer, String address) {
        this.idProblem = idProblem;
        this.customer = customer;
        this.address = address;
    }

    public String getIdProblem() {
        return idProblem;
    }

    public void setIdProblem(String idProblem) {
        this.idProblem = idProblem;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
