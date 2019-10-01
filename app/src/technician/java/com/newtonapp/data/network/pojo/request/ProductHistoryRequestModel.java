package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class ProductHistoryRequestModel {

	@SerializedName("flag")
	private String flag = "7";

	@SerializedName("action")
	private String action = "history";

	@SerializedName("sn")
	private String sn;

	@SerializedName("category")
	private String category = "technician";

	@SerializedName("token")
	private String token;

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

	public void setSn(String sn){
		this.sn = sn;
	}

	public String getSn(){
		return sn;
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

	@Override
 	public String toString(){
		return 
			"ProductHistoryRequestModel{" +
			"flag = '" + flag + '\'' + 
			",action = '" + action + '\'' + 
			",sn = '" + sn + '\'' + 
			",category = '" + category + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}