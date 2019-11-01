package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;
import com.newtonapp.utility.Constants;

public class ApprovalRequestModel {

	@SerializedName("prob")
	private String prob;

	@SerializedName("flag")
	private String flag = Constants.FLAG_CLOSED;

	@SerializedName("action")
	private String action = "solving";

	@SerializedName("category")
	private String category = "technician";

	@SerializedName("token")
	private String token;

	@SerializedName("rate")
	private String rate;

	@SerializedName("otp")
	private String otp;

	@SerializedName("cust")
	private String idcust;

	public void setProb(String prob){
		this.prob = prob;
	}

	public String getProb(){
		return prob;
	}

	public void setFlag(String flag){
		this.flag = flag;
	}

	public String getFlag(){
		return flag;
	}

	public void setAction(String action){
		this.action = action;
	}

	public String getAction(){
		return action;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getIdcust() {
		return idcust;
	}

	public void setIdcust(String idcust) {
		this.idcust = idcust;
	}

	@Override
 	public String toString(){
		return 
			"ApprovalRequestModel{" +
			"prob = '" + prob + '\'' + 
			",flag = '" + flag + '\'' + 
			",action = '" + action + '\'' + 
			",category = '" + category + '\'' + 
			",token = '" + token + '\'' +
			",rate = '" + rate + '\'' +
			",otp = '" + otp + '\'' +
			",idcust = '" + idcust + '\'' +
			"}";
		}
}