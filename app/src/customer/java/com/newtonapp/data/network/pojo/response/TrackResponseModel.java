package com.newtonapp.data.network.pojo.response;

import com.google.gson.annotations.SerializedName;
import com.newtonapp.data.database.entity.Problem;

public class TrackResponseModel{

	@SerializedName("data")
	private Problem data;

	@SerializedName("ack")
	private String ack;

	@SerializedName("message")
	private String message;

	@SerializedName("token")
	private String token;

	@SerializedName("status")
	private int status;

	public Problem getData() {
		return data;
	}

	public void setData(Problem data) {
		this.data = data;
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
			"TrackResponseModel{" + 
			"data = '" + data + '\'' + 
			",ack = '" + ack + '\'' + 
			",message = '" + message + '\'' + 
			",token = '" + token + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}