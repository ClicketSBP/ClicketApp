package be.nielsbril.clicket.app.api;

import be.nielsbril.clicket.app.helpers.Contract;
import be.nielsbril.clicket.app.helpers.LoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClicketInstance {

    private static Retrofit RETROFIT_INSTANCE;
    private static ClicketService CLICKETSERVICE_INSTANCE;

    private static final Object lock_retrofit = new Object();
    private static final Object lock_service = new Object();

    private static Retrofit getRetrofitInstance() {
        if (RETROFIT_INSTANCE == null) {
            synchronized (lock_retrofit) {
                if (RETROFIT_INSTANCE == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();

                    RETROFIT_INSTANCE = new Retrofit.Builder()
                            .baseUrl(Contract.API_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return RETROFIT_INSTANCE;
    }

    public static ClicketService getClicketserviceInstance() {
        if (CLICKETSERVICE_INSTANCE == null) {
            synchronized (lock_service) {
                if (CLICKETSERVICE_INSTANCE == null) {
                    CLICKETSERVICE_INSTANCE = getRetrofitInstance().create(ClicketService.class);
                }
            }
        }
        return CLICKETSERVICE_INSTANCE;
    }

}