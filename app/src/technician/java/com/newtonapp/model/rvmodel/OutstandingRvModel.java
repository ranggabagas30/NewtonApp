package com.newtonapp.model.rvmodel;

public class OutstandingRvModel {

    private String customer;
    private String address;

    public OutstandingRvModel(String customer, String address) {
        this.customer = customer;
        this.address = address;
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
