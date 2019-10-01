package com.newtonapp.utility;

import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtil {

    public static String handleApiError(Throwable error) {

        final int API_STATUS_CODE_LOCAL_ERROR = 0;
        String errorMessage = error.getLocalizedMessage();
        if (error instanceof HttpException) {
            switch (((HttpException) error).code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    errorMessage = "Unauthorised User";
                    break;
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    errorMessage = "Forbidden";
                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    errorMessage = "Internal Server Error";
                    break;
                case HttpsURLConnection.HTTP_BAD_REQUEST:
                    errorMessage = "Bad Request";
                    break;
                case API_STATUS_CODE_LOCAL_ERROR:
                    errorMessage = "No Internet Connection";
                    break;
                default:
                    errorMessage = error.getLocalizedMessage();

            }
        } else if (error instanceof JsonSyntaxException) {
            errorMessage = "Something Went Wrong API is not responding properly!";
        } else if (error instanceof UnknownHostException) {
            errorMessage = "No internet connection";
        } else{
            errorMessage = error.getMessage();
        }

        DebugUtil.d("error (" + errorMessage + ")");
        return errorMessage;
    }
}
