package com.randeep.myapp.popularmoviesstage2.apiCall;

import com.randeep.myapp.popularmoviesstage2.bean.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by randeep on 2/14/16.
 */
public interface MovieApi {

    @GET("/3/movie/{type}")
    Call<Movie> getMovie(
            @Path("type") String sortingType,
            @Query("api_key") String apiKey
    );
}
