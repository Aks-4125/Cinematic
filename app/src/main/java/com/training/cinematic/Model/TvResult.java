package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dhruvisha on 6/11/2018.
 */

public class TvResult extends RealmObject {

        @SerializedName("original_name")

        private String originalName;
        @SerializedName("genre_ids")

        private RealmList<Integer> genreIds = null;
        @SerializedName("name")

        private String name;
        @SerializedName("popularity")

        private Double popularity;
        @SerializedName("origin_country")

        private RealmList<String> originCountry = null;
        @SerializedName("vote_count")

        private Integer voteCount;
        @SerializedName("first_air_date")

        private String firstAirDate;
        @SerializedName("backdrop_path")

        private String backdropPath;
        @SerializedName("original_language")

        private String originalLanguage;
        @SerializedName("id")

        private Integer id;
        @SerializedName("vote_average")

        private Double voteAverage;
        @SerializedName("overview")

        private String overview;
        @SerializedName("poster_path")

        private String posterPath;

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public RealmList<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(RealmList<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public RealmList<String> getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(RealmList<String> originCountry) {
            this.originCountry = originCountry;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public String getFirstAirDate() {
            return firstAirDate;
        }

        public void setFirstAirDate(String firstAirDate) {
            this.firstAirDate = firstAirDate;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

    }

