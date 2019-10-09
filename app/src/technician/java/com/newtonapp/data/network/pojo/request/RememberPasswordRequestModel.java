package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class RememberPasswordRequestModel{

	@SerializedName("nik")
	private String nik;

	@SerializedName("hp")
	private String hp;

	@SerializedName("action")
	private String action = "remember";

	public void setNik(String nik){
		this.nik = nik;
	}

	public String getNik(){
		return nik;
	}

	public void setHp(String hp){
		this.hp = hp;
	}

	public String getHp(){
		return hp;
	}

	public void setAction(String action){
		this.action = action;
	}

	public String getAction(){
		return action;
	}

	@Override
 	public String toString(){
		return 
			"RememberPasswordRequestModel{" + 
			"nik = '" + nik + '\'' + 
			",hp = '" + hp + '\'' + 
			",action = '" + action + '\'' + 
			"}";
		}
}