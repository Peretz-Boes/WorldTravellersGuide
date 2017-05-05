package com.example.android.worldtravellersguide.network;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by Peretz on 2017-05-04.
 */

public class RetroApiClient {
    private static final String BASE_SERVER_URL="http://23.99.57.31/api/";
    private static Retrofit retrofit=null;

    public static Retrofit getClient(){
        if (retrofit==null){
            OkHttpClient client=new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).connectTimeout(60,TimeUnit.SECONDS).build();
            retrofit=new Retrofit.Builder().baseUrl(BASE_SERVER_URL).client(client).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.FINAL,Modifier.TRANSIENT,Modifier.STATIC).create())).build();
        }
        return retrofit;
    }

}
