package com.mustafa.sar.instagramthesis.api_services;
import com.mustafa.sar.instagramthesis.models.ImageList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {

    @GET("/api/")
    Call<ImageList> getImages(@Query("key") String key,
                              @Query("q") String query,
                              @Query("page") int page);
}
