package com.example.hk.transport.Utilities.APIs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    static String BaseURL = "http://x2y.moon-enterprises.com";
    static Retrofit retrofit;
    static Interfaces interfaces;

    public static Retrofit getRetrofit()
    {
        if(retrofit == null)
        {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();

            return retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        else
            return retrofit;
    }

    public static Interfaces getWebServices()
    {
        if(interfaces == null)
            return interfaces = getRetrofit().create(Interfaces.class);
        else
            return interfaces;
    }
}
