package com.aranirahan.myfavoritemovie.provider;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static com.aranirahan.myfavoritemovie.database.FavoriteTable.BACKDROP;
import static com.aranirahan.myfavoritemovie.database.FavoriteTable.OVERVIEW;
import static com.aranirahan.myfavoritemovie.database.FavoriteTable.POSTER;
import static com.aranirahan.myfavoritemovie.database.FavoriteTable.RELEASE_DATE;
import static com.aranirahan.myfavoritemovie.database.FavoriteTable.TITLE;
import static com.aranirahan.myfavoritemovie.database.FavoriteTable.VOTE;
import static com.aranirahan.myfavoritemovie.database.DatabaseContract.getColumnDouble;
import static com.aranirahan.myfavoritemovie.database.DatabaseContract.getColumnInt;
import static com.aranirahan.myfavoritemovie.database.DatabaseContract.getColumnString;

public class FavoriteModel {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("overview")
    private String overview;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public FavoriteModel() {
    }

    public FavoriteModel(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.posterPath = getColumnString(cursor, POSTER);
        this.backdropPath = getColumnString(cursor, BACKDROP);
        this.title = getColumnString(cursor, TITLE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.releaseDate = getColumnString(cursor, RELEASE_DATE);
        this.voteAverage = getColumnDouble(cursor, VOTE);
    }

    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        "overview = '" + overview + '\'' +
                        "}";
    }
}
