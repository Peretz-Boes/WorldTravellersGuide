package com.example.android.worldtravellersguide.network;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Peretz on 2017-05-04.
 */

public class RetroApiClient {
    private static final String BASE_SERVER_URL="https://api.foursquare.com/v2/";
    private static Retrofit retrofit=null;

    public static Retrofit getClient(){
        if (retrofit==null){
            CustomRetrofitInterceptor interceptor=new CustomRetrofitInterceptor();
            HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client=new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).connectTimeout(60,TimeUnit.SECONDS).addInterceptor(interceptor).addInterceptor(httpLoggingInterceptor).build();
            retrofit=new Retrofit.Builder().baseUrl(BASE_SERVER_URL).client(client).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.FINAL,Modifier.TRANSIENT,Modifier.STATIC).create())).build();
        }
        return retrofit;
    }

}
