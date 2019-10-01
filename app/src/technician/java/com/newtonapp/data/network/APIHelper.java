package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.ApprovalRequestModel;
import com.newtonapp.data.network.pojo.request.HoldRequestModel;
import com.newtonapp.data.network.pojo.request.KunjunganRequestModel;
import com.newtonapp.data.network.pojo.request.OutstandingRequestModel;
import com.newtonapp.data.network.pojo.request.SolvedRequestModel;
import com.newtonapp.data.network.pojo.request.TakeJobRequestModel;
import com.newtonapp.data.network.pojo.request.VerificationRequestModel;
import com.newtonapp.data.network.pojo.response.ApprovalResponseModel;
import com.newtonapp.data.network.pojo.response.HoldResponseModel;
import com.newtonapp.data.network.pojo.response.KunjunganResponseModel;
import com.newtonapp.data.network.pojo.response.OutstandingResponseModel;
import com.newtonapp.data.network.pojo.response.SolvedResponseModel;
import com.newtonapp.data.network.pojo.response.TakingJobResponseModel;
import com.newtonapp.data.network.pojo.response.VerificationResponseModel;

import io.reactivex.Single;

public class APIHelper {

    public static Single<VerificationResponseModel> requestLogin(VerificationRequestModel loginBody) {
        return BaseAPIConfig.createService(APIClient.class).postTechnicianVerification(loginBody);
    }

    public static Single<OutstandingResponseModel> getOutstandingJoblist(OutstandingRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postRetrieveOutstandingJoblist(formBody);
    }

    public static Single<TakingJobResponseModel> takingOutstandingJob(TakeJobRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postTakingOutstandingJob(formBody);
    }

    public static Single<KunjunganResponseModel> kunjunganSolvingin(KunjunganRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postKunjunganSolvingIn(formBody);
    }

    public static Single<SolvedResponseModel> solved(SolvedRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postSolved(formBody);
    }

    public static Single<HoldResponseModel> hold(HoldRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postHold(formBody);
    }

    public static Single<ApprovalResponseModel> approve(ApprovalRequestModel formBody) {
        return BaseAPIConfig.createService(APIClient.class).postApproval(formBody);
    }
}
