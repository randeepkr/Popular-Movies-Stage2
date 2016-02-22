package com.randeep.myapp.popularmoviesstage2.apiCall;

import com.randeep.myapp.popularmoviesstage2.bean.Movie;
import com.randeep.myapp.popularmoviesstage2.bean.TrailerAndReviews;

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
   // http://api.themoviedb.org/3/movie/135397?api_key=a1a316a459ddc1464c60bf797c366c16&append_to_response=trailers%2Creviews
    @GET("/3/movie/{movieId}?append_to_response=trailers%2Creviews")
    Call<TrailerAndReviews> getMovieDetails(
            @Path("movieId") String movieId,
            @Query("api_key") String apiKey
    );
}
