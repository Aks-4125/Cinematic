package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dhruvisha on 6/15/2018.
 */

public class PoplarMovieModel extends RealmObject {
   @PrimaryKey
    
    private Integer page;
    @SerializedName("total_results")
    
    private Integer totalResults;
    @SerializedName("total_pages")
    
    private Integer totalPages;
    @SerializedName("results")
    
    private RealmList<PopularMovieResult> results = null;

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

    public RealmList<PopularMovieResult> getResults() {
        return results;
    }

    public void setResults(RealmList<PopularMovieResult> results) {
        this.results = results;
    }

}
