package com.example.android.worldtravellersguide;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by Peretz on 2017-04-19.
 */

public class PlaceClient {

    public static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient.Builder httpClient=new OkHttpClient.Builder();
    public static <S> S createService(Class<S>serviceClass){
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original=chain.request();
                HttpUrl url=original.url().newBuilder().addQueryParameter(Constants.API_KEY_PARAM,Constants.API_KEY_PARAMETER).addQueryParameter(Constants.API_SECRET_PARAM,Constants.API_SECRET_PARAMETER).build();
                Request request=original.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit=builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}
