package com.newtonapp.data.network.pojo.response;

import com.newtonapp.data.database.entity.Customer;

import java.util.List;

public class ReportsResponseModel extends BaseResponseModel {

    private List<Customer> data;

    public List<Customer> getData() {
        return data;
    }

    public void setData(List<Customer> data) {
        this.data = data;
    }
}

