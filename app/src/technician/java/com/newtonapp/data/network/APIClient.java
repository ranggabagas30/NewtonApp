package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.ApprovalRequestModel;
import com.newtonapp.data.network.pojo.request.ErrorLoggingRequestModel;
import com.newtonapp.data.network.pojo.request.FirebaseTokenSendingRequestModel;
import com.newtonapp.data.network.pojo.request.HoldRequestModel;
import com.newtonapp.data.network.pojo.request.KunjunganRequestModel;
import com.newtonapp.data.network.pojo.request.OutstandingRequestModel;
import com.newtonapp.data.network.pojo.request.ProductHistoryRequestModel;
import com.newtonapp.data.network.pojo.request.ReportsRequestModel;
import com.newtonapp.data.network.pojo.request.SolvedRequestModel;
import com.newtonapp.data.network.pojo.request.TakeJobRequestModel;
import com.newtonapp.data.network.pojo.request.TrackingRequestModel;
import com.newtonapp.data.network.pojo.request.VerificationRequestModel;
import com.newtonapp.data.network.pojo.response.ApprovalResponseModel;
import com.newtonapp.data.network.pojo.response.FirebaseTokenSendingResponseModel;
import com.newtonapp.data.network.pojo.response.HoldResponseModel;
import com.newtonapp.data.network.pojo.response.KunjunganResponseModel;
import com.newtonapp.data.network.pojo.response.OutstandingResponseModel;
import com.newtonapp.data.network.pojo.response.ProductHistoryResponseModel;
import com.newtonapp.data.network.pojo.response.ReportsResponseModel;
import com.newtonapp.data.network.pojo.response.SolvedResponseModel;
import com.newtonapp.data.network.pojo.response.TakingJobResponseModel;
import com.newtonapp.data.network.pojo.response.TrackingResponseModel;
import com.newtonapp.data.network.pojo.response.VerificationResponseModel;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIClient {

    // firebase token sending
    @POST("api_firebase.php")
    Single<FirebaseTokenSendingResponseModel> postFirebaseTokenSending(@Body FirebaseTokenSendingRequestModel requestBody);

    // error logging
    @POST("api_firebase.php")
    Completable postErrorLogging(@Body ErrorLoggingRequestModel requestBody);

    // technician verification
    @POST("api_verify.php")
    Single<VerificationResponseModel> postTechnicianVerification(@Body VerificationRequestModel requestBody);

    // retrieve outstanding job list
    @POST("teknisi.php")
    Single<OutstandingResponseModel> postRetrieveOutstandingJoblist(@Body OutstandingRequestModel requestBody);

    // taking outstanding job
    @POST("teknisi.php")
    Single<TakingJobResponseModel> postTakingOutstandingJob(@Body TakeJobRequestModel requestBody);

    // kunjungan solving in
    @POST("teknisi.php")
    Single<KunjunganResponseModel> postKunjunganSolvingIn(@Body KunjunganRequestModel requestBody);

    // solved
    @POST("teknisi.php")
    Single<SolvedResponseModel> postSolved(@Body SolvedRequestModel requestBody);

    // hold
    @POST("teknisi.php")
    Single<HoldResponseModel> postHold(@Body HoldRequestModel requestBody);

    // approval (finishing)
    @POST("teknisi.php")
    Single<ApprovalResponseModel> postApproval(@Body ApprovalRequestModel requestBody);

    // retrieve report job list
    @POST("teknisi.php")
    Single<ReportsResponseModel> postReports(@Body ReportsRequestModel requestBody);

    // retrieve product's history
    @POST("teknisi.php")
    Single<ProductHistoryResponseModel> postProductHistory(@Body ProductHistoryRequestModel requestBody);

    @POST("teknisi.php")
    Single<TrackingResponseModel> postTrack(@Body TrackingRequestModel requestBody);
}
