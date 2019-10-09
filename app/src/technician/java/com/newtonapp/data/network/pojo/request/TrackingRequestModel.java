package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class TrackingRequestModel{

	@SerializedName("action")
	private String action = "track";

	@SerializedName("category")
	private String category = "technician";

	@SerializedName("token")
	private String token;

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

	@Override
 	public String toString(){
		return 
			"TrackingRequestModel{" + 
			"action = '" + action + '\'' + 
			",category = '" + category + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}