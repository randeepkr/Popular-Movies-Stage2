package com.randeep.myapp.popularmoviesstage2.apiCall;

import android.util.Log;

import com.randeep.myapp.popularmoviesstage2.BuildConfig;
import com.randeep.myapp.popularmoviesstage2.GridLayoutAdapter;
import com.randeep.myapp.popularmoviesstage2.bean.Movie;
import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by randeep on 2/14/16.
 */
public class FetchMovieTask {

    List<MovieDetail> moviesList = new ArrayList<>();
    GridLayoutAdapter gridLayoutAdapter;

    public FetchMovieTask(GridLayoutAdapter gridLayoutAdapter){
        networkCall();
        this.gridLayoutAdapter = gridLayoutAdapter;
    }

    private void networkCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi movieApi = retrofit.create(MovieApi.class);
        Call<Movie> call = movieApi.getMovie("popular", BuildConfig.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Response<Movie> response) {
                Movie movie = response.body();
                moviesList = movie.getResults();

                if (moviesList.size() > 0){
                    gridLayoutAdapter.movieDetailArrayList.clear();
                    gridLayoutAdapter.movieDetailArrayList = (ArrayList<MovieDetail>) moviesList;
                    gridLayoutAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("FAIL",":(((((((((((");
            }
        });
    }
}
