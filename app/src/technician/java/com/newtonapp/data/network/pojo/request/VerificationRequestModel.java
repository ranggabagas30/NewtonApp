package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class TeknisiVerifyRequestModel{

	@SerializedName("action")
	private String action = "in";

	@SerializedName("username")
	private String username;

	@SerializedName("password")
	private String password;

	@SerializedName("category")
	private String category = "technician";


	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
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

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"TeknisiVerifyRequestModel{" + 
			"password = '" + password + '\'' + 
			",action = '" + action + '\'' + 
			",category = '" + category + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}