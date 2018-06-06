package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dhruvisha on 6/4/2018.
 */

public class SliderMovieImages {
    @SerializedName("id")

    private Integer id;
    @SerializedName("backdrops")

    private List<Backdrop> backdrops = null;
    @SerializedName("posters")

    private List<Poster> posters = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }
    public class Poster {

        @SerializedName("aspect_ratio")

        private Double aspectRatio;
        @SerializedName("file_path")

        private String filePath;
        @SerializedName("height")

        private Integer height;
        @SerializedName("iso_639_1")

        private String iso6391;
        @SerializedName("vote_average")

        private Double voteAverage;
        @SerializedName("vote_count")

        private Integer voteCount;
        @SerializedName("width")

        private Integer width;

        public Double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }
    public class Backdrop {

        @SerializedName("aspect_ratio")

        private Double aspectRatio;
        @SerializedName("file_path")

        private String filePath;
        @SerializedName("height")

        private Integer height;
        @SerializedName("iso_639_1")

        private String iso6391;
        @SerializedName("vote_average")

        private Double voteAverage;
        @SerializedName("vote_count")

        private Integer voteCount;
        @SerializedName("width")

        private Integer width;

        public Double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }
}
