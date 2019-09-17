package com.newtonapp.model;

public class ReportRvModel {

    private String idcustomer;
    private String idprinter;
    private String issueddate;
    private String status;

    public ReportRvModel(String idcustomer, String idprinter, String issueddate, String status) {
        this.idcustomer = idcustomer;
        this.idprinter = idprinter;
        this.issueddate = issueddate;
        this.status = status;
    }

    public String getIdcustomer() {
        return idcustomer;
    }

    public void setIdcustomer(String idcustomer) {
        this.idcustomer = idcustomer;
    }

    public String getIdprinter() {
        return idprinter;
    }

    public void setIdprinter(String idprinter) {
        this.idprinter = idprinter;
    }

    public String getIssueddate() {
        return issueddate;
    }

    public void setIssueddate(String issueddate) {
        this.issueddate = issueddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
