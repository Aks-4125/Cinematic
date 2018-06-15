package com.training.cinematic.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dhruvisha on 6/6/2018.
 */

public class MovieDetailModel extends RealmObject{
    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("backdrop_path")

    private String backdropPath;
   /* @SerializedName("belongs_to_collection")

    private BelongsToCollection belongsToCollection;*/
    @SerializedName("budget")

    private Integer budget;
  /*  @SerializedName("genres")

    private List<Genre> genres = null;*/
    @SerializedName("homepage")

    private String homepage;
   @PrimaryKey
   
    private Integer id;
    @SerializedName("imdb_id")

    private String imdbId;
    @SerializedName("original_language")

    private String originalLanguage;
    @SerializedName("original_title")

    private String originalTitle;
    @SerializedName("overview")

    private String overview;
    @SerializedName("popularity")

    private Double popularity;
    @SerializedName("poster_path")

    private String posterPath;
  /*  @SerializedName("production_companies")
   
    private List<ProductionCompany> productionCompanies = null;
    @SerializedName("production_countries")

    private List<ProductionCountry> productionCountries = null;*/
    @SerializedName("release_date")

    private String releaseDate;
    @SerializedName("revenue")
   
    private Integer revenue;
    @SerializedName("runtime")
   
    private Integer runtime;
   /* @SerializedName("spoken_languages")
   
    private List<SpokenLanguage> spokenLanguages = null;*/
    @SerializedName("status")
   
    private String status;
    @SerializedName("tagline")
   
    private String tagline;
    @SerializedName("title")
   
    private String title;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
   
    private Double voteAverage;
    @SerializedName("vote_count")
   
    private Integer voteCount;

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

  /*  public BelongsToCollection getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(BelongsToCollection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }*/

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

   /* public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }*/

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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

   /* public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }*/

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /*public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
   /* public class ProductionCompany {

        @SerializedName("id")
       
        private Integer id;
        @SerializedName("logo_path")
       
        private String logoPath;
        @SerializedName("name")
       
        private String name;
        @SerializedName("origin_country")
       
        private String originCountry;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(String originCountry) {
            this.originCountry = originCountry;
        }

    }
    public class ProductionCountry {

        @SerializedName("iso_3166_1")
       
        private String iso31661;
        @SerializedName("name")
       
        private String name;

        public String getIso31661() {
            return iso31661;
        }

        public void setIso31661(String iso31661) {
            this.iso31661 = iso31661;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class SpokenLanguage {

        @SerializedName("iso_639_1")
       
        private String iso6391;
        @SerializedName("name")
       
        private String name;

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class BelongsToCollection {

        @SerializedName("id")
       
        private Integer id;
        @SerializedName("name")
       
        private String name;
        @SerializedName("poster_path")
       
        private String posterPath;
        @SerializedName("backdrop_path")
       
        private String backdropPath;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

    }
    public class Genre {

        @SerializedName("id")
       
        private Integer id;
        @SerializedName("name")
       
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }*/
}
