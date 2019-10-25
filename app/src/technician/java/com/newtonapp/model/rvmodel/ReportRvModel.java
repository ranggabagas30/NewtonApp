package com.newtonapp.model.rvmodel;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.newtonapp.data.database.entity.Customer;

public class ReportRvModel {

    private Customer customer;
    private String empty = "N/A";

    public ReportRvModel(@NonNull Customer customer) {
        this.customer = customer;
    }

    public String getIdcustomer() {
        if (!TextUtils.isEmpty(customer.getIdCust()))
            return customer.getIdCust();
        return empty;
    }

    public void setIdcustomer(@NonNull String idcustomer) {
        customer.setIdCust(idcustomer);
    }

    public String getIdprinter() {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null) {
                if (!TextUtils.isEmpty(customer.getProblems().get(0).getIdProduk()))
                    return customer.getProblems().get(0).getIdProduk();
            }
        }
        return empty;
    }

    public void setIdprinter(@NonNull String idprinter) {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null) {
                customer.getProblems().get(0).setIdProduk(idprinter);
            }
        }
    }

    public String getIssueddate() {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null) {
                if (!TextUtils.isEmpty(customer.getProblems().get(0).getWaktuComp())) {
                    return customer.getProblems().get(0).getWaktuComp();
                }
            }
        }
        return empty;
    }

    public void setIssueddate(String issueddate) {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null) {
                customer.getProblems().get(0).setWaktuComp(issueddate);
            }
        }
    }

    public String getStatus() {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null) {
                if (!TextUtils.isEmpty(customer.getProblems().get(0).getStatusComplain())) {
                    return customer.getProblems().get(0).getStatusComplain();
                }
            }
        }
        return empty;
    }

    public void setStatus(int status) {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null) {
                customer.getProblems().get(0).setStatusComplain(String.valueOf(status));
            }
        }
    }

    public String getSolvingOption() {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null && customer.getProblems().get(0).getSolving() != null) {
                if (!TextUtils.isEmpty(customer.getProblems().get(0).getSolving().getSolvingOption())) {
                    return customer.getProblems().get(0).getSolving().getSolvingOption();
                }
            }
        }
        return empty;
    }

    public String getSolvingReason() {
        if (customer.getProblems() != null && !customer.getProblems().isEmpty()) {
            if (customer.getProblems().get(0) != null && customer.getProblems().get(0).getSolving() != null) {
                if (!TextUtils.isEmpty(customer.getProblems().get(0).getSolving().getSolvingNote())) {
                    return customer.getProblems().get(0).getSolving().getSolvingNote();
                }
            }
        }
        return empty;
    }
}
