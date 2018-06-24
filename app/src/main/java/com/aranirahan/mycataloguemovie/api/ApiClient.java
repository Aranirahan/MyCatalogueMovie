package com.aranirahan.mycataloguemovie.api;

import android.support.annotation.NonNull;

import com.aranirahan.mycataloguemovie.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

        private TheMovieDBApi apiCall;

        public ApiClient() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request original = chain.request();
                            HttpUrl httpUrl = original.url()
                                    .newBuilder()
                                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                                    .build();

                            original = original.newBuilder()
                                    .url(httpUrl)
                                    .build();

                            return chain.proceed(original);
                        }
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiCall = retrofit.create(TheMovieDBApi.class);
        }

        public TheMovieDBApi getService() {
            return apiCall;
        }

}
