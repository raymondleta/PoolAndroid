package com.tosh.poolandroid.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AuthRetrofitClient {
    private static Retrofit instance;
    private static final String  BASE_URL = "http://10.0.2.2:7000/";

    public static Retrofit getInstance(){
        if (instance == null)
            instance = new Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build();
        return instance;
    }
}
