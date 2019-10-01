package com.newtonapp.data.network.pojo.response;

import com.google.gson.annotations.SerializedName;

public class VerificationResponseModel extends BaseResponseModel {

    @SerializedName("token")
    private String token;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    @Override
    public String toString() {
        return
                "TeknisiVerifyResponse {" +
                        "ack = '" + ack + '\'' +
                        ",token = '" + token + '\'' +
                        ",message = '" + message + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
