package com.newtonapp.data.network.pojo.response;

import com.google.gson.annotations.SerializedName;

public class UpdateResponseModel{

	@SerializedName("note")
	private Object note;

	@SerializedName("ack")
	private String ack;

	@SerializedName("message")
	private String message;

	@SerializedName("token")
	private String token;

	@SerializedName("status")
	private int status;

	public void setNote(Object note){
		this.note = note;
	}

	public Object getNote(){
		return note;
	}

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

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
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
			"UpdateResponseModel{" + 
			"note = '" + note + '\'' + 
			",ack = '" + ack + '\'' + 
			",message = '" + message + '\'' + 
			",token = '" + token + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}