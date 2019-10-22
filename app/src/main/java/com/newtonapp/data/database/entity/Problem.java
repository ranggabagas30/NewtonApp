package com.newtonapp.data.database.entity;

import com.google.gson.annotations.SerializedName;

public class Problem {

	@SerializedName("id_problem")
	private String idProblem;

	@SerializedName("id_produk")
	private String idProduk;

	@SerializedName("note")
	private String note;

	@SerializedName("reason")
	private String reason;

	@SerializedName("status_complain")
	private String statusComplain;

	@SerializedName("otp")
	private String otp;

	@SerializedName("waktu_comp_act")
	private String waktuCompAct;

	@SerializedName("waktu_comp")
	private String waktuComp;

	@SerializedName("solving")
	private Solving solving;

	public String getIdProblem() {
		return idProblem;
	}

	public void setIdProblem(String idProblem) {
		this.idProblem = idProblem;
	}

	public void setIdProduk(String idProduk){
		this.idProduk = idProduk;
	}

	public String getIdProduk(){
		return idProduk;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setStatusComplain(String statusComplain){
		this.statusComplain = statusComplain;
	}

	public String getStatusComplain(){
		return statusComplain;
	}

	public void setOtp(String otp){
		this.otp = otp;
	}

	public String getOtp(){
		return otp;
	}

	public void setWaktuCompAct(String waktuCompAct){
		this.waktuCompAct = waktuCompAct;
	}

	public String getWaktuCompAct(){
		return waktuCompAct;
	}

	public void setWaktuComp(String waktuComp){
		this.waktuComp = waktuComp;
	}

	public String getWaktuComp(){
		return waktuComp;
	}

	public Solving getSolving() {
		return solving;
	}

	public void setSolving(Solving solving) {
		this.solving = solving;
	}

	@Override
 	public String toString(){
		return 
			"Problem{" +
			"id_produk = '" + idProduk + '\'' + 
			",note = '" + note + '\'' + 
			",reason = '" + reason + '\'' + 
			",status_complain = '" + statusComplain + '\'' + 
			",otp = '" + otp + '\'' + 
			",waktu_comp_act = '" + waktuCompAct + '\'' + 
			",waktu_comp = '" + waktuComp + '\'' + 
			"}";
		}
}