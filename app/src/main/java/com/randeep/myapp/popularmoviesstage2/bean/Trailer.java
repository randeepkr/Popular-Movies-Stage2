package com.randeep.myapp.popularmoviesstage2.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by randeep on 2/21/16.
 */
public class Trailer {

    @SerializedName("name")
    String name;

    @SerializedName("size")
    String size;

    @SerializedName("source")
    String source;

    @SerializedName("type")
    String type;

    public String getName(){ return name; }

    public String getSize() { return size; }

    public String getSource() { return source; }

    public String getType() { return type; }
}
