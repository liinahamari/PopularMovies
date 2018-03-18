package com.example.guest.popularmovies.mvp.model;

// http://www.jsonschema2pojo.org/

/**
 * Created by guest on 2/19/18.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesArray {
    @SerializedName("results")
    @Expose
    private List<SingleMovie> results = null;

    public List<SingleMovie> getResults() {
        return results;
    }

    public void setResults(List<SingleMovie> results) {
        this.results = results;
    }
}

