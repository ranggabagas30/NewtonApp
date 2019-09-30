package com.newtonapp.data.network.pojo.request;

import com.google.gson.annotations.SerializedName;
import com.newtonapp.utility.Constants;

public class SolvedRequestModel{

	@SerializedName("note")
	private String note;

	@SerializedName("prob")
	private String prob;

	@SerializedName("flag")
	private String flag = Constants.FLAG_SOLVED;

	@SerializedName("action")
	private String action = "solving";

	@SerializedName("solve_opt")
	private String solveOpt;

	@SerializedName("id_cust")
	private String idCust;

	@SerializedName("sn")
	private String sn;

	@SerializedName("category")
	private String category = "technician";

	@SerializedName("token")
	private String token;

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

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

	public void setSolveOpt(String solveOpt){
		this.solveOpt = solveOpt;
	}

	public String getSolveOpt(){
		return solveOpt;
	}

	public void setIdCust(String idCust){
		this.idCust = idCust;
	}

	public String getIdCust(){
		return idCust;
	}

	public void setSn(String sn){
		this.sn = sn;
	}

	public String getSn(){
		return sn;
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
			"SolvedRequestModel{" + 
			"note = '" + note + '\'' + 
			",prob = '" + prob + '\'' + 
			",flag = '" + flag + '\'' + 
			",action = '" + action + '\'' + 
			",solve_opt = '" + solveOpt + '\'' + 
			",id_cust = '" + idCust + '\'' + 
			",sn = '" + sn + '\'' + 
			",category = '" + category + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}