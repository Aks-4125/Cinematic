package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dhruvisha on 5/29/2018.
 */

public class MovieModel extends RealmObject {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("results")
    private RealmList<MovieResult> results = null;
    @PrimaryKey
    private int page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("dates")
    private MovieDates dates;
    @SerializedName("total_pages")
    private Integer totalPages;

  /*  public int getId(){return id;}
    public void setId(int id){this.id=id;}*/

    public RealmList<MovieResult> getResults() {
        return results;
    }

    public void setResults(RealmList<MovieResult> results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public MovieDates getDates() {
        return dates;
    }

    public void setDates(MovieDates dates) {
        this.dates = dates;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}
