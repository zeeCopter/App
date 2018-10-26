package com.example.hk.transport.Utilities.APIs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    static String BaseURL = "http://a212f1ee.ngrok.io";
    static Retrofit retrofit;
    static Interfaces interfaces;

    public static Retrofit getRetrofit()
    {
        if(retrofit == null)
            return retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
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
