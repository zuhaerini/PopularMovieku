package com.izadalab.popularmovie.model;

/**
 * Created by DhytoDev on 12/1/17.
 */

public class Movie {
    private int id;
    private String title;
    private float popularity;
    private String poster_path;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private int voteCount;
    private float voteAverage;

    public Movie(String posterPath) {
        this.poster_path = posterPath;
    }

    public Movie(int id, String title, float popularity, String posterPath, String backdropPath, String overview, String releaseDate, int voteCount, float voteAverage) {
        this.id = id;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
