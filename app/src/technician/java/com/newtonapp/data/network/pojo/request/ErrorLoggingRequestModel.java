package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class ErrorLoggingRequestModel{

	@SerializedName("error_message")
	private String errorMessage;

	@SerializedName("error_datetime")
	private String errorDatetime;

	@SerializedName("error_description")
	private String errorDescription;

	@SerializedName("webtoken")
	private String webtoken;

	@SerializedName("action")
	private String action = "log_error";

	@SerializedName("imei")
	private String imei;

	@SerializedName("username")
	private String username;

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setErrorDatetime(String errorDatetime){
		this.errorDatetime = errorDatetime;
	}

	public String getErrorDatetime(){
		return errorDatetime;
	}

	public void setErrorDescription(String errorDescription){
		this.errorDescription = errorDescription;
	}

	public String getErrorDescription(){
		return errorDescription;
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
			"ErrorLoggingRequestModel{" + 
			"error_message = '" + errorMessage + '\'' + 
			",error_datetime = '" + errorDatetime + '\'' + 
			",error_description = '" + errorDescription + '\'' + 
			",webtoken = '" + webtoken + '\'' + 
			",action = '" + action + '\'' + 
			",imei = '" + imei + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}