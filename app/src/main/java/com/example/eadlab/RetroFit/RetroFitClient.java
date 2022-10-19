package com.example.eadlab.RetroFit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroFitClient {
    private static Retrofit instance;
    private static final String BASE_URL = "http://10.0.2.2:7135";

    public static Retrofit getInstance(){
        if(instance == null)
//            instance = new Retrofit.Builder()
//                    .baseUrl("http://10.0.2.2:7135/")
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
