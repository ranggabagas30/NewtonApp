package com.newtonapp.customer.data.network;

import com.newtonapp.customer.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.customer.data.network.pojo.response.ComplainResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIClient {

    // customer verify and complain
    @POST("api_verify.php")
    Observable<ComplainResponseModel> sendComplain(@Body ComplainRequestModel complainBody);


}
