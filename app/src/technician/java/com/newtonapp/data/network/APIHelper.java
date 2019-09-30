package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.KunjunganSolvingInRequestModel;
import com.newtonapp.data.network.pojo.request.OutstandingJoblistRequestModel;
import com.newtonapp.data.network.pojo.request.TakingOutstandingRequestModel;
import com.newtonapp.data.network.pojo.request.TeknisiVerifyRequestModel;
import com.newtonapp.data.network.pojo.response.KunjunganSolvingInResponseModel;
import com.newtonapp.data.network.pojo.response.OutstandingJoblistResponseModel;
import com.newtonapp.data.network.pojo.response.TakingOutstandingResponseModel;
import com.newtonapp.data.network.pojo.response.TeknisiVerifyResponseModel;

import io.reactivex.Single;

public class APIHelper {

    public static Single<TeknisiVerifyResponseModel> requestLogin(TeknisiVerifyRequestModel loginBody) {
        return BaseAPIConfig.createService(APIClient.class).postTechnicianVerification(loginBody);
    }

    public static Single<OutstandingJoblistResponseModel> getOutstandingJoblist(OutstandingJoblistRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postRetrieveOutstandingJoblist(formBody);
    }

    public static Single<TakingOutstandingResponseModel> takingOutstandingJob(TakingOutstandingRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postTakingOutstandingJob(formBody);
    }

    public static Single<KunjunganSolvingInResponseModel> solvingIn(KunjunganSolvingInRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postKunjunganSolvingIn(formBody);
    }
}
