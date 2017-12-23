package com.izadalab.popularmovie.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by DhytoDev on 12/8/17.
 */

public class MovieContract {

    private MovieContract() {
    }
    public static final String AUTHORITY = "com.izadalab.popularmovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES ="movies";

    public  static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public  static final String TABLE_NAME = "mark_movie";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String OVERVIEW ="overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String VOTE_COUNT = "vote_count";
        public static final String  VOTE_AVERAGE ="vote_average";

    }
}
