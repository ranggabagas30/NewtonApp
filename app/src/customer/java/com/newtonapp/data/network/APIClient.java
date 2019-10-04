package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.data.network.pojo.request.TrackRequestModel;
import com.newtonapp.data.network.pojo.request.UpdateRequestModel;
import com.newtonapp.data.network.pojo.response.ComplainResponseModel;
import com.newtonapp.data.network.pojo.response.TrackResponseModel;
import com.newtonapp.data.network.pojo.response.UpdateResponseModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIClient {

    // customer verify and complain
    @POST("api_verify.php")
    Single<ComplainResponseModel> postComplain(@Body ComplainRequestModel complainBody);

    // customer update complain
    @POST("api_verify.php")
    Single<UpdateResponseModel> postUpdate(@Body UpdateRequestModel updateBody);

    // customer tracking
    @POST("api_verify.php")
    Single<TrackResponseModel> postTrack(@Body TrackRequestModel trackBody);

}
