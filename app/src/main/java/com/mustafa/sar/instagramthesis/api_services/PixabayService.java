package com.mustafa.sar.instagramthesis.api_services;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PixabayService {

    private static final String BASE_URL = "https://pixabay.com/";


    public static PixabayApi createService(final Context context) {

        final File httpCacheDirectory = new File(context.getCacheDir(),  "responses");
       String dd = httpCacheDirectory.getAbsolutePath();
        final int cacheSize = 20 * 1024 * 1024; // 20 MB
        final Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        String cacheControl = originalResponse.header("Cache-Control");

                        if (cacheControl == null
                                || cacheControl.contains("no-store")
                                || cacheControl.contains("no-cache")
                                || cacheControl.contains("must-revalidate")
                                || cacheControl.contains("max-age=0")) {
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + 10)
                                    .build();
                        } else {
                            return originalResponse;
                        }
                    }
                }).addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if(!InternetCheck.isInternetAvailable(context)){
                            int maxStale = 60 * 60 * 24 * 28; // 4-weeks time cache data stays
                            request = request
                                    .newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(BASE_URL);

        return builder.build().create(PixabayApi.class);
    }
}
