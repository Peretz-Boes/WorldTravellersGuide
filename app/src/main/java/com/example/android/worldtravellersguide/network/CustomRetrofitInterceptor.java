package com.example.android.worldtravellersguide.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Peretz on 2017-05-07.
 */

public class CustomRetrofitInterceptor implements Interceptor {

    public static final String LOG_TAG=CustomRetrofitInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orginalReqest=chain.request();
        Response response=chain.proceed(orginalReqest);
        if (response!=null){
            Log.d(LOG_TAG,response.toString());
        }
        return response;
    }
}
