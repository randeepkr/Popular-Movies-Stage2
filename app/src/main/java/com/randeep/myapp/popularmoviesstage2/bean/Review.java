package com.randeep.myapp.popularmoviesstage2.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by randeep on 2/21/16.
 */
public class Review {

    @SerializedName("id")
    String id;

    @SerializedName("author")
    String author;

    @SerializedName("content")
    String content;

    public String getId(){
        return id;
    }

    public String getAuthor(){
        return author;
    }

    public String getContent(){
        return content;
    }
}
