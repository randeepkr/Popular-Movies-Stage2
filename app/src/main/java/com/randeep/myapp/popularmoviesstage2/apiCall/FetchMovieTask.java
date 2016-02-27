package com.randeep.myapp.popularmoviesstage2.apiCall;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.randeep.myapp.popularmoviesstage2.BuildConfig;
import com.randeep.myapp.popularmoviesstage2.R;
import com.randeep.myapp.popularmoviesstage2.bean.Movie;
import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;
import com.randeep.myapp.popularmoviesstage2.data.MovieContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by randeep on 2/14/16.
 */
public class FetchMovieTask {

    private static final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    List<MovieDetail> moviesList = new ArrayList<>();
    Context mContext;
    String sortingType;

    public FetchMovieTask(Context context, String sorting) {
        mContext = context;
        sortingType = sorting;
        networkCall();
    }

    private void networkCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi movieApi = retrofit.create(MovieApi.class);
        Call<Movie> call = movieApi.getMovie(sortingType, BuildConfig.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Response<Movie> response) {
                Movie movie = response.body();
                moviesList = movie.getResults();


                if (moviesList.size() > 0) {
                    addDataToDatabase(moviesList);
//                    gridLayoutAdapter.movieDetailArrayList.clear();
//                    gridLayoutAdapter.movieDetailArrayList = (ArrayList<MovieDetail>) moviesList;
//                    gridLayoutAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("FAIL", ":(((((((((((");
            }
        });
    }

    private void addDataToDatabase(List<MovieDetail> moviesList) {

        Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesList.size());

        for (int i=0; i<moviesList.size(); i++) {
            ContentValues contentValues = new ContentValues();

            MovieDetail movieDetail = moviesList.get(i);

            contentValues.put(MovieContract.Movies.POSTER_PATH, movieDetail.getPosterPath());
            contentValues.put(MovieContract.Movies.ADULT, movieDetail.getAdult());
            contentValues.put(MovieContract.Movies.OVERVIEW, movieDetail.getOverview());
            contentValues.put(MovieContract.Movies.RELEASE_DATE, movieDetail.getRelease_date());
            contentValues.put(MovieContract.Movies.MOVIE_ID, movieDetail.getId());
            contentValues.put(MovieContract.Movies.ORIGINAL_TITLE, movieDetail.getOriginalTitle());
            contentValues.put(MovieContract.Movies.ORIGINAL_LANGUAGE, movieDetail.getOriginalLanguage());
            contentValues.put(MovieContract.Movies.TITLE, movieDetail.getTitle());
            contentValues.put(MovieContract.Movies.BACKDROP_PATH, movieDetail.getBackdropPath());
            contentValues.put(MovieContract.Movies.POPULARITY, movieDetail.getPopularity());
            contentValues.put(MovieContract.Movies.VOTE_COUNT, movieDetail.getVoteCount());
            contentValues.put(MovieContract.Movies.VOTE_AVERAGE, movieDetail.getVoteAverage());
            contentValues.put(MovieContract.Movies.SORT_BY, sortingType);

            cVVector.add(contentValues);
        }

        int inserted = 0;

        if (cVVector.size() > 0){
            ContentValues[] contentValuesArray = new ContentValues[cVVector.size()];
            cVVector.toArray(contentValuesArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.Movies.CONTENT_URI, contentValuesArray);
        }

        Log.d(LOG_TAG, "FetchPopularMovie Task Complete. " + inserted + " Inserted");
    }
}
