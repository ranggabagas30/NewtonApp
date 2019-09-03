package com.newtonapp.customer.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConfig {

    private static final String TAG = APIConfig.class.getSimpleName();
    private static final String API_BASE_URL = "https://dev.pramadhan.com/api/";

    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

    public static Gson gson = new GsonBuilder().setLenient().create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson));

    public static Retrofit retrofit = retrofitBuilder.build();

    public static <S> S createService(Class<S> serviceClass) {

        if (!okHttpClientBuilder.interceptors().contains(httpLoggingInterceptor)) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            retrofitBuilder.client(okHttpClientBuilder.build());
            retrofit = retrofitBuilder.build();
        }
        return retrofit.create(serviceClass);
    }
}
