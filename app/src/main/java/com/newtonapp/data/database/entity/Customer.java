package com.newtonapp.data.database.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Customer {

	@SerializedName("id_cust")
	private String idCust;

	@SerializedName("nama")
	private String nama;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("kota")
	private String kota;

	@SerializedName("kodepos")
	private String kodepos;

	@SerializedName("no_telp")
	private String noTelp;

	@SerializedName("pic")
	private String pic;

	@SerializedName("tgl_registrasi")
	private String tglRegistrasi;

	@SerializedName("idstate")
	private String idstate;

	@SerializedName("problems")
	private List<Problem> problems;

	public void setKota(String kota){
		this.kota = kota;
	}

	public String getKota(){
		return kota;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setTglRegistrasi(String tglRegistrasi){
		this.tglRegistrasi = tglRegistrasi;
	}

	public String getTglRegistrasi(){
		return tglRegistrasi;
	}

	public void setKodepos(String kodepos){
		this.kodepos = kodepos;
	}

	public String getKodepos(){
		return kodepos;
	}

	public void setIdCust(String idCust){
		this.idCust = idCust;
	}

	public String getIdCust(){
		return idCust;
	}

	public void setNoTelp(String noTelp){
		this.noTelp = noTelp;
	}

	public String getNoTelp(){
		return noTelp;
	}

	public void setPic(String pic){
		this.pic = pic;
	}

	public String getPic(){
		return pic;
	}

	public void setIdstate(String idstate){
		this.idstate = idstate;
	}

	public String getIdstate(){
		return idstate;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setProblems(List<Problem> problems){
		this.problems = problems;
	}

	public List<Problem> getProblems(){
		return problems;
	}

	@Override
 	public String toString(){
		return 
			"Customer{" +
			"kota = '" + kota + '\'' + 
			",nama = '" + nama + '\'' + 
			",tgl_registrasi = '" + tglRegistrasi + '\'' + 
			",kodepos = '" + kodepos + '\'' + 
			",id_cust = '" + idCust + '\'' + 
			",no_telp = '" + noTelp + '\'' + 
			",pic = '" + pic + '\'' + 
			",idstate = '" + idstate + '\'' + 
			",alamat = '" + alamat + '\'' + 
			",problems = '" + problems + '\'' + 
			"}";
		}
}