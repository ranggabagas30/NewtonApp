package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class FirebaseTokenSendingRequestModel{

	@SerializedName("firebasetoken")
	private String firebasetoken;

	@SerializedName("webtoken")
	private String webtoken;

	@SerializedName("action")
	private String action = "put_firetoken";

	@SerializedName("imei")
	private String imei;

	@SerializedName("username")
	private String username;

	public void setFirebasetoken(String firebasetoken){
		this.firebasetoken = firebasetoken;
	}

	public String getFirebasetoken(){
		return firebasetoken;
	}

	public void setWebtoken(String webtoken){
		this.webtoken = webtoken;
	}

	public String getWebtoken(){
		return webtoken;
	}

	public void setAction(String action){
		this.action = action;
	}

	public String getAction(){
		return action;
	}

	public void setImei(String imei){
		this.imei = imei;
	}

	public String getImei(){
		return imei;
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
			"FirebaseTokenSendingRequestModel{" + 
			"firebasetoken = '" + firebasetoken + '\'' + 
			",webtoken = '" + webtoken + '\'' + 
			",action = '" + action + '\'' + 
			",imei = '" + imei + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}