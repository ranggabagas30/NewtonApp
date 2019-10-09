package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.ApprovalRequestModel;
import com.newtonapp.data.network.pojo.request.ErrorLoggingRequestModel;
import com.newtonapp.data.network.pojo.request.FirebaseTokenSendingRequestModel;
import com.newtonapp.data.network.pojo.request.HoldRequestModel;
import com.newtonapp.data.network.pojo.request.KunjunganRequestModel;
import com.newtonapp.data.network.pojo.request.LogoutRequestModel;
import com.newtonapp.data.network.pojo.request.OutstandingRequestModel;
import com.newtonapp.data.network.pojo.request.RememberPasswordRequestModel;
import com.newtonapp.data.network.pojo.request.ReportsRequestModel;
import com.newtonapp.data.network.pojo.request.SolvedRequestModel;
import com.newtonapp.data.network.pojo.request.TakeJobRequestModel;
import com.newtonapp.data.network.pojo.request.TrackingRequestModel;
import com.newtonapp.data.network.pojo.request.VerificationRequestModel;
import com.newtonapp.data.network.pojo.response.ApprovalResponseModel;
import com.newtonapp.data.network.pojo.response.FirebaseTokenSendingResponseModel;
import com.newtonapp.data.network.pojo.response.HoldResponseModel;
import com.newtonapp.data.network.pojo.response.KunjunganResponseModel;
import com.newtonapp.data.network.pojo.response.LogoutResponseModel;
import com.newtonapp.data.network.pojo.response.OutstandingResponseModel;
import com.newtonapp.data.network.pojo.response.RememberPasswordResponseModel;
import com.newtonapp.data.network.pojo.response.ReportsResponseModel;
import com.newtonapp.data.network.pojo.response.SolvedResponseModel;
import com.newtonapp.data.network.pojo.response.TakingJobResponseModel;
import com.newtonapp.data.network.pojo.response.TrackingResponseModel;
import com.newtonapp.data.network.pojo.response.VerificationResponseModel;
import com.newtonapp.utility.DebugUtil;

import io.reactivex.Completable;
import io.reactivex.Single;

public class APIHelper {

    public static Single<RememberPasswordResponseModel> forgetPassword(RememberPasswordRequestModel formBody) {
        DebugUtil.d(">> FORGET PASSWORD");
        return BaseAPIConfig.createService(APIClient.class).postRememberPassword(formBody);
    }

    public static Single<VerificationResponseModel> requestLogin(VerificationRequestModel loginBody) {
        DebugUtil.d(">> REQUEST LOGIN");
        return BaseAPIConfig.createService(APIClient.class).postTechnicianVerification(loginBody);
    }

    public static Single<FirebaseTokenSendingResponseModel> sendFirebaseToken(FirebaseTokenSendingRequestModel formBody) {
        DebugUtil.d(">> SEND FIREBASE TOKEN");
        return BaseAPIConfig.createService(APIClient.class).postFirebaseTokenSending(formBody);
    }

    public static Completable sendErrorLog(ErrorLoggingRequestModel formBody) {
        DebugUtil.d(">> SEND ERROR LOG");
        return BaseAPIConfig.createService(APIClient.class).postErrorLogging(formBody);
    }

    public static Single<OutstandingResponseModel> getOutstandingJoblist(OutstandingRequestModel formBody) {
        DebugUtil.d(">> RETRIEVE OUTSTANDING JOB LIST");
        return BaseAPIConfig.createService(APIClient.class).postRetrieveOutstandingJoblist(formBody);
    }

    public static Single<TakingJobResponseModel> takingOutstandingJob(TakeJobRequestModel formBody) {
        DebugUtil.d(">> TAKING OUTSTANDING JOB");
        return BaseAPIConfig.createService(APIClient.class).postTakingOutstandingJob(formBody);
    }

    public static Single<KunjunganResponseModel> kunjunganSolvingin(KunjunganRequestModel formBody) {
        DebugUtil.d(">> KUNJUNGAN SOLVING IN");
        return BaseAPIConfig.createService(APIClient.class).postKunjunganSolvingIn(formBody);
    }

    public static Single<SolvedResponseModel> solved(SolvedRequestModel formBody) {
        DebugUtil.d(">> SOLVED");
        return BaseAPIConfig.createService(APIClient.class).postSolved(formBody);
    }

    public static Single<HoldResponseModel> hold(HoldRequestModel formBody) {
        DebugUtil.d(">> HOLD");
        return BaseAPIConfig.createService(APIClient.class).postHold(formBody);
    }

    public static Single<ApprovalResponseModel> approve(ApprovalRequestModel formBody) {
        DebugUtil.d(">> APPROVE");
        return BaseAPIConfig.createService(APIClient.class).postApproval(formBody);
    }

    public static Single<ReportsResponseModel> getReportList(ReportsRequestModel formBody) {
        DebugUtil.d(">> RETRIEVE REPORT LIST");
        return BaseAPIConfig.createService(APIClient.class).postReports(formBody);
    }

    public static Single<TrackingResponseModel> track(TrackingRequestModel formBody) {
        DebugUtil.d(">> TRACK");
        return BaseAPIConfig.createService(APIClient.class).postTrack(formBody);
    }

    public static Single<LogoutResponseModel> logout(LogoutRequestModel formBody) {
        DebugUtil.d(">> LOGOUT");
        return BaseAPIConfig.createService(APIClient.class).postLogout(formBody);
    }
}
