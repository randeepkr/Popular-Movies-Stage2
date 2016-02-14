package com.randeep.myapp.popularmoviesstage2.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by randeep on 2/14/16.
 */
public class MovieDetail implements Parcelable {

    @SerializedName("poster_path")

    private String posterPath;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("genre_ids")
    private ArrayList<Integer> genreIds = new ArrayList<>();

    @SerializedName("id")
    private int id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;


    protected MovieDetail(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(voteAverage);
    }

    public String getPosterPath(){
        return "http://image.tmdb.org/t/p/w300"+posterPath;
    }

    public boolean getAdult(){
        return adult;
    }

    public String getOverview(){
        return overview;
    }

    public String getRelease_date(){
        return release_date;
    }

    public int getId(){
        return id;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public String getTitle(){
        return title;
    }

    public String getBackdropPath(){
        return "http://image.tmdb.org/t/p/original"+backdropPath;
    }

    public double getPopularity(){
        return popularity;
    }

    public int getVoteCount(){
        return voteCount;
    }

    public boolean getVideo(){
        return video;
    }

    public double getVoteAverage(){
        return voteAverage;
    }
}
