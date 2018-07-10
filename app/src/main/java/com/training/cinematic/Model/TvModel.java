package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dhruvisha on 5/31/2018.
 */

public class TvModel extends RealmObject {

    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")

    private Integer totalResults;
    @SerializedName("total_pages")

    private Integer totalPages;
    @SerializedName("results")

    private RealmList<TvResult> results = null;


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

    public RealmList<TvResult> getResults() {
        return results;
    }

    public void setResults(RealmList<TvResult> results) {
        this.results = results;
    }


}
