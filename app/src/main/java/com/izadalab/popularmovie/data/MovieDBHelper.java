package com.izadalab.popularmovie.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.izadalab.popularmovie.model.Movie;

/**
 * Created by DhytoDev on 12/8/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    public static final short VERSION = 3;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = " CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " ( " +
                MovieContract.MovieEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.ID + " INTEGER NOT NULL UNIQUE, " +
                MovieContract.MovieEntry.TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.POPULARITY + " FLOAT NOT NULL, " +
                MovieContract.MovieEntry.POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.VOTE_COUNT + " FLOAT NOT NULL, " +
                MovieContract.MovieEntry.VOTE_AVERAGE + " FLOAT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
