package com.newtonapp.model.rvmodel;

public class OutstandingRvModel {

    private String idProblem;
    private String customer;
    private String address;
    private String picName;
    private String picTelp;

    public OutstandingRvModel(String idProblem, String customer, String address, String picName, String picTelp) {
        this.idProblem = idProblem;
        this.customer = customer;
        this.address = address;
        this.picName = picName;
        this.picTelp = picTelp;
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

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicTelp() {
        return picTelp;
    }

    public void setPicTelp(String picTelp) {
        this.picTelp = picTelp;
    }
}
