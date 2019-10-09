package com.newtonapp.data.network.pojo.response;

import com.google.gson.annotations.SerializedName;

public class RememberPasswordResponseModel{

	@SerializedName("ack")
	private String ack;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setAck(String ack){
		this.ack = ack;
	}

	public String getAck(){
		return ack;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"RememberPasswordResponseModel{" + 
			"ack = '" + ack + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}