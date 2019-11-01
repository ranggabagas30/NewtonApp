package com.newtonapp.data.network.pojo.response;

import com.google.gson.annotations.SerializedName;

public class VerificationResponseModel extends BaseResponseModel {

    @SerializedName("token")
    private String token;

    @SerializedName("profile")
    private Profile profile;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public class Profile {

        private String name;
        private String pic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
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
