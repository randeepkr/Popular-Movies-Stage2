package com.randeep.myapp.popularmoviesstage2.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randeep on 2/21/16.
 */
public class TrailerAndReviews {


    @SerializedName("trailers")
    YoutubeTrailers youtubeTrailers = new YoutubeTrailers();

    @SerializedName("reviews")
    ReviewsResult reviewsResult = new ReviewsResult();

    public YoutubeTrailers getInstance(){
        return youtubeTrailers;
    }

    public ReviewsResult getReviewsResult(){
        return reviewsResult;
    }

    public class YoutubeTrailers{

        @SerializedName("youtube")
        List<Trailer> trailersList = new ArrayList<>();

        public ArrayList<Trailer> getTrailers(){
            return (ArrayList<Trailer>) trailersList;
        }
    }

    public class ReviewsResult{
        @SerializedName("results")
        List<Review> reviews = new ArrayList<>();

        public ArrayList<Review> getReviews(){
            return (ArrayList<Review>) reviews;
        }

    }
}
