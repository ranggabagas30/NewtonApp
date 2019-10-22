package com.newtonapp.data.network.pojo.response;

import com.google.gson.annotations.SerializedName;
import com.newtonapp.data.database.entity.Customer;

import java.util.List;

public class TrackingResponseModel{


	@SerializedName("ack")
	private String ack;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<Customer> data;

	@SerializedName("status")
	private int status;

	public List<Customer> getData() {
		return data;
	}

	public void setData(List<Customer> data) {
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

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"TrackingResponseModel{" + 
			"data = '" + data + '\'' + 
			",ack = '" + ack + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}