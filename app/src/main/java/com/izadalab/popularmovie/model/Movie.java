package com.izadalab.popularmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DhytoDev on 12/1/17.
 */

public class Movie implements Parcelable{
    private int id;
    private String title;
    private Double popularity;
    private String poster_path;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private Double voteCount;
    private Double voteAverage;




    public Movie(int id, String title, Double popularity, String poster_path, String backdropPath, String overview, String releaseDate, Double voteCount, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readDouble();
        }
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
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

    public Double getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Double voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeDouble(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        if (voteCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(voteCount);
        }
        if (voteAverage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(voteAverage);
        }
    }
}
