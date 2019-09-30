package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;

public class TakingOutstandingJoblistRequestModel{

	@SerializedName("prob")
	private String prob;

	@SerializedName("flag")
	private String flag = "2";

	@SerializedName("action")
	private String action = "outstanding";

	@SerializedName("category")
	private String category = "technician";

	@SerializedName("token")
	private String token;

	public void setProb(String prob){
		this.prob = prob;
	}

	public String getProb(){
		return prob;
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

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"TakingOutstandingJoblistRequestModel{" + 
			"prob = '" + prob + '\'' + 
			",flag = '" + flag + '\'' + 
			",action = '" + action + '\'' + 
			",category = '" + category + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}