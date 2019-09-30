package com.newtonapp.model.rvmodel;

import com.newtonapp.data.database.entity.Customer;

public class OutstandingRvModelNew {

    private Customer customer;

    public OutstandingRvModelNew(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
