package com.randeep.myapp.popularmoviesstage2.apiCall;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.randeep.myapp.popularmoviesstage2.BuildConfig;
import com.randeep.myapp.popularmoviesstage2.bean.Review;
import com.randeep.myapp.popularmoviesstage2.bean.TrailerAndReviews;
import com.randeep.myapp.popularmoviesstage2.bean.Trailer;
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
 * Created by randeep on 2/21/16.
 */
public class FetchMovieTrailersAndReviewsTask {

    Context mContext;
    String movieId;
    private static final String LOG_TAG = FetchMovieTrailersAndReviewsTask.class.getSimpleName();
    List<Trailer> trailers = new ArrayList<>();
    List<Review> reviews = new ArrayList<>();

    public FetchMovieTrailersAndReviewsTask(Context context, String movieId){

        mContext = context;
        this.movieId = movieId;
        networkCall();

    }

    private void networkCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi movieApi = retrofit.create(MovieApi.class);
        Call<TrailerAndReviews> call = movieApi.getMovieDetails(movieId, BuildConfig.API_KEY);
        call.enqueue(new Callback<TrailerAndReviews>() {
            @Override
            public void onResponse(Response<TrailerAndReviews> response) {
                TrailerAndReviews trailersAndReviews = response.body();
                TrailerAndReviews.YoutubeTrailers youtubeTrailers = trailersAndReviews.getInstance();
                TrailerAndReviews.ReviewsResult reviewResult = trailersAndReviews.getReviewsResult();
                trailers = youtubeTrailers.getTrailers();
                reviews = reviewResult.getReviews();

                addDatatoDatabase();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("FAIL", ":(((((((((((");
            }
        });

    }

    private void addDatatoDatabase() {

        Vector<ContentValues> cVVectorTrailers = new Vector<>(trailers.size());

        for (int i=0; i<trailers.size(); i++){

            ContentValues contentValues = new ContentValues();

            Trailer trailerDetail = trailers.get(i);

            contentValues.put(MovieContract.Trailers.NAME, trailerDetail.getName());
            contentValues.put(MovieContract.Trailers.SIZE, trailerDetail.getSize());
            contentValues.put(MovieContract.Trailers.SOURCE, trailerDetail.getSource());
            contentValues.put(MovieContract.Trailers.TYPE, trailerDetail.getType());
            contentValues.put(MovieContract.Trailers.MOVIE_ID, movieId);

            cVVectorTrailers.add(contentValues);
        }

        int inserted = 0;

        if (cVVectorTrailers.size() > 0){
            ContentValues[] contentValuesArray = new ContentValues[cVVectorTrailers.size()];
            cVVectorTrailers.toArray(contentValuesArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.Trailers.CONTENT_URI, contentValuesArray);
        }

        Log.d(LOG_TAG, "FetchPopularMovie Trailers Complete. " + inserted + " Inserted");

        Vector<ContentValues> cVVectorReviews = new Vector<>();

        for (int i=0; i<reviews.size(); i++){

            ContentValues contentValues = new ContentValues();

            Review reviewDetail = reviews.get(i);

            contentValues.put(MovieContract.Reviews.ID_REVIEWS, reviewDetail.getId());
            contentValues.put(MovieContract.Reviews.AUTHOR, reviewDetail.getAuthor());
            contentValues.put(MovieContract.Reviews.CONTENT, reviewDetail.getContent());
            contentValues.put(MovieContract.Reviews.MOVIE_ID, movieId);

            cVVectorReviews.add(contentValues);
        }

        inserted = 0;

        if (cVVectorReviews.size() > 0){
            ContentValues[] contentValuesArray = new ContentValues[cVVectorReviews.size()];
            cVVectorReviews.toArray(contentValuesArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.Reviews.CONTENT_URI, contentValuesArray);
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.Movies.DOWNLOADED,"1");

        int rowsUpdated = mContext.getContentResolver().update(MovieContract.Movies.CONTENT_URI,
                contentValues,
                MovieContract.Movies.MOVIE_ID + " = ?",
                new String[]{movieId});


        Log.d(LOG_TAG, "FetchPopularMovie Reviews Complete. " + inserted + " Inserted" + "rowsUpdated: "+rowsUpdated);

    }
}
