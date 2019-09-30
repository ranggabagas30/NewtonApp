package com.newtonapp.data.network;

import com.newtonapp.data.network.pojo.request.FinishingSolvingOkRequestModel;
import com.newtonapp.data.network.pojo.request.HoldSolvingRequestModel;
import com.newtonapp.data.network.pojo.request.KunjunganSolvingInRequestModel;
import com.newtonapp.data.network.pojo.request.OutstandingJoblistRequestModel;
import com.newtonapp.data.network.pojo.request.RetrieveHistoryProductRequestModel;
import com.newtonapp.data.network.pojo.request.RetrieveReportJoblistRequestModel;
import com.newtonapp.data.network.pojo.request.TakingOutstandingRequestModel;
import com.newtonapp.data.network.pojo.request.TeknisiVerifyRequestModel;
import com.newtonapp.data.network.pojo.response.FinishinGSolvingOkResponseModel;
import com.newtonapp.data.network.pojo.response.HoldSolvingResponseModel;
import com.newtonapp.data.network.pojo.response.KunjunganSolvingInResponseModel;
import com.newtonapp.data.network.pojo.response.OutstandingJoblistResponseModel;
import com.newtonapp.data.network.pojo.response.RetrieveHistoryProductResponseModel;
import com.newtonapp.data.network.pojo.response.RetrieveReportJoblistResponseModel;
import com.newtonapp.data.network.pojo.response.TakingOutstandingResponseModel;
import com.newtonapp.data.network.pojo.response.TeknisiVerifyResponseModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIClient {

    // technician verification
    @POST("api_verify.php")
    Single<TeknisiVerifyResponseModel> postTechnicianVerification(@Body TeknisiVerifyRequestModel requestBody);

    // retrieve outstanding job list
    @POST("teknisi.php")
    Single<OutstandingJoblistResponseModel> postRetrieveOutstandingJoblist(@Body OutstandingJoblistRequestModel requestBody);

    // taking outstanding job
    @POST("teknisi.php")
    Single<TakingOutstandingResponseModel> postTakingOutstandingJob(@Body TakingOutstandingRequestModel requestBody);

    // kunjungan solving in
    @POST("teknisi.php")
    Single<KunjunganSolvingInResponseModel> postKunjunganSolvingIn(@Body KunjunganSolvingInRequestModel requestBody);

    // finishing solving ok
    @POST("teknisi.php")
    Single<FinishinGSolvingOkResponseModel> postFinishingSolvingOk(@Body FinishingSolvingOkRequestModel requestBody);

    // hold solving
    @POST("teknisi.php")
    Single<HoldSolvingResponseModel> postHoldSolving(@Body HoldSolvingRequestModel requestBody);

    // retrieve report joblist
    @POST("teknisi.php")
    Single<RetrieveReportJoblistResponseModel> postRetrieveReportJoblist(@Body RetrieveReportJoblistRequestModel requestBody);

    // retrieve history product
    @POST("teknisi.php")
    Single<RetrieveHistoryProductResponseModel> postRetrieveHistoryProduct(@Body RetrieveHistoryProductRequestModel requestBody);

}
