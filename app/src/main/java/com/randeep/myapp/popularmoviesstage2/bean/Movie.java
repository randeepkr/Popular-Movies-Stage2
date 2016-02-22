package com.randeep.myapp.popularmoviesstage2.bean;

import com.google.gson.annotations.SerializedName;
import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randeep on 2/14/16.
 */
public class Movie {

    @SerializedName("results")
    private List<MovieDetail> results = new ArrayList<MovieDetail>();


    public List<MovieDetail> getResults(){
        return results;
    }


}
