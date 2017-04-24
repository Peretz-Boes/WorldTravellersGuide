package com.example.android.worldtravellersguide;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Peretz on 2017-04-19.
 */

public class PlaceListResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Place> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
