package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.data.network.pojo.response.ComplainResponseModel;

import io.reactivex.Observable;

public class APIHelper {

    /**
     * name : send complain
     * type : post
     * */
    public static Observable<ComplainResponseModel> sendComplain(ComplainRequestModel complainBody) {
        return BaseAPIConfig.createService(APIClient.class).sendComplain(complainBody);
    }
}
