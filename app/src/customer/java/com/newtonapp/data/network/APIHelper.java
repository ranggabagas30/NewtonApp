package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.data.network.pojo.request.TrackRequestModel;
import com.newtonapp.data.network.pojo.request.UpdateRequestModel;
import com.newtonapp.data.network.pojo.response.ComplainResponseModel;
import com.newtonapp.data.network.pojo.response.TrackResponseModel;
import com.newtonapp.data.network.pojo.response.UpdateResponseModel;

import io.reactivex.Single;

public class APIHelper {

    // complain
    public static Single<ComplainResponseModel> sendComplain(ComplainRequestModel complainBody) {
        return BaseAPIConfig.createService(APIClient.class).postComplain(complainBody);
    }

    // update
    public static Single<UpdateResponseModel> updateComplain(UpdateRequestModel updateBody) {
        return BaseAPIConfig.createService(APIClient.class).postUpdate(updateBody);
    }

    // track
    public static Single<TrackResponseModel> trackComplain(TrackRequestModel trackBody) {
        return BaseAPIConfig.createService(APIClient.class).postTrack(trackBody);
    }
}
