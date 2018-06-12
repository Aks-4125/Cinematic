package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dhruvisha on 5/29/2018.
 */

public class MovieModel extends RealmObject {


    @SerializedName("results")
    private RealmList<MovieResult> results = null;
    @PrimaryKey
    private Integer page;
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

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
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


   /* public class Result extends RealmObject {

        @SerializedName("vote_count")

        private Integer voteCount;
        @SerializedName("id")

        private int id;
        @SerializedName("video")

        private Boolean video;
        @SerializedName("vote_average")

        private Double voteAverage;
        @SerializedName("title")

        private String title;
        @SerializedName("popularity")

        private Double popularity;
        @SerializedName("poster_path")

        private String posterPath;
        @SerializedName("original_language")

        private String originalLanguage;
        @SerializedName("original_title")

        private String originalTitle;
        @SerializedName("genre_ids")

        private List<Integer> genreIds = null;
        @SerializedName("backdrop_path")

        private String backdropPath;
        @SerializedName("adult")

        private Boolean adult;
        @SerializedName("overview")

        private String overview;
        @SerializedName("release_date")

        private String releaseDate;

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public int getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

    }
*/
   /* public class Dates{

        @SerializedName("maximum")

        private String maximum;
        @SerializedName("minimum")

        private String minimum;

        public String getMaximum() {
            return maximum;
        }

        public void setMaximum(String maximum) {
            this.maximum = maximum;
        }

        public String getMinimum() {
            return minimum;
        }

        public void setMinimum(String minimum) {
            this.minimum = minimum;
        }

    }*/
}
