package com.example.guest.popularmovies.mvp.model;

// http://www.jsonschema2pojo.org/

/**
 * Created by guest on 2/19/18.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesArray {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<SingleMovie> results = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<SingleMovie> getResults() {
        return results;
    }

    public void setResults(List<SingleMovie> results) {
        this.results = results;
    }
}

