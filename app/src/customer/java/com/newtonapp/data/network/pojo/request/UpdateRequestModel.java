package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class UpdateRequestModel{

	@SerializedName("note")
	private String note;

	@SerializedName("password")
	private String password;

	@SerializedName("flag")
	private String flag = "update";

	@SerializedName("action")
	private String action = "cmp";

	@SerializedName("category")
	private String category = "customer";

	@SerializedName("username")
	private String username;

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
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

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"UpdateRequestModel{" + 
			"note = '" + note + '\'' + 
			",password = '" + password + '\'' + 
			",flag = '" + flag + '\'' + 
			",action = '" + action + '\'' + 
			",category = '" + category + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}