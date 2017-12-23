package com.izadalab.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.izadalab.popularmovie.adapter.ReviewAdapter;
import com.izadalab.popularmovie.adapter.TrailerAdapter;
import com.izadalab.popularmovie.data.MovieContract;
import com.izadalab.popularmovie.model.Movie;
import com.izadalab.popularmovie.model.Review;
import com.izadalab.popularmovie.model.Trailer;
import com.izadalab.popularmovie.utils.NetworkUtils;
import com.izadalab.popularmovie.utils.RecyclerViewItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements RecyclerViewItemClickListener{
    //@BindView(R.id.tv_movie_title) TextView movieTitle;
    @BindView(R.id.iv_poster_path)
    ImageView posterPath;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.tv_vote_average)
    TextView voteAverage;
    @BindView(R.id.btn_mark)
    Button markFavorite;
    @BindView(R.id.tv_detail_overview) TextView detailOverview;
    @BindView(R.id.iv_backdrop) ImageView backdrop;
    @BindView(R.id.rv_trailer)
    RecyclerView rvTrailer;
    @BindView(R.id.rv_review) RecyclerView rvReview;
    @BindView(R.id.btn_unmark) Button unMarkFavorite;

    private List<Trailer> trailerList = new ArrayList<>();
    private TrailerAdapter adapterTrailer;

    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter adapterReview;
    private Movie movie;

    public static final String MOVIE_INTENT = "movie.intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie = getIntent().getParcelableExtra(MOVIE_INTENT);
        getSupportActionBar().setTitle(movie.getTitle());

        adapterTrailer = new TrailerAdapter(this, trailerList, this);
        RecyclerView.LayoutManager layoutManagerTrailer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvTrailer.setLayoutManager(layoutManagerTrailer);
        rvTrailer.setAdapter(adapterTrailer);

        adapterReview = new ReviewAdapter (this, reviewList, this);
        RecyclerView.LayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReview.setLayoutManager(layoutManagerReview);
        rvReview.setAdapter(adapterReview);


        //movieTitle.setText(movie.getTitle());
        Glide.with(this)
                .load(NetworkUtils.buildMoviePosterUrl(movie.getPoster_path()))
                .into(posterPath);
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(String.valueOf(movie.getVoteAverage()));
        detailOverview.setText(movie.getOverview());
        Glide.with(this)
                .load(NetworkUtils.buildMoviePosterUrl(movie.getBackdropPath()))
                .into(backdrop);
        loadData(String.valueOf(movie.getId()));
        isExistInFavorite(movie.getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadData (String key) {
        URL vidoeurl = NetworkUtils.buildMovieVideoUrl(key);
        URL reviewurl = NetworkUtils.buildMovieReviewUrl(key);
        new getDataTrailer().execute(vidoeurl);
        new getDataReview().execute(reviewurl);

    }

    public class getDataReview extends AsyncTask <URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String results = null;

            try {
                results = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("result",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");

                Log.i("Review", results.toString());
                for(int i = 0; i < results.length(); i++){
                    reviewList.add(new Review(
                            results.getJSONObject(i).getString("author"),
                            results.getJSONObject(i).getString("content")));
                }
                Log.e("json_array", results.toString());

                rvReview.setVisibility(View.VISIBLE);
                adapterReview.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class getDataTrailer extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String results = null;

            try {
                results = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.e("result",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");

                Log.i("Review", results.toString());
                for(int i = 0; i < results.length(); i++){
                    trailerList.add(new Trailer(
                            results.getJSONObject(i).getString("id"),
                            results.getJSONObject(i).getString("key"),
                            results.getJSONObject(i).getString("name")));

                }
                Log.e("json_array", results.toString());

                rvReview.setVisibility(View.VISIBLE);
                adapterReview.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    private void isExistInFavorite(int idMovie){
        int num = 0;
        Cursor cursor;

        cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[] {MovieContract.MovieEntry.ID},
                MovieContract.MovieEntry.ID + " = ?",
                new String[] {String.valueOf(idMovie)},
                null);

        if (cursor.getCount()>0){
            markFavorite.setVisibility(View.GONE);
            unMarkFavorite.setVisibility(View.VISIBLE);
        }
        cursor.close();
    }

    @Override
    public void onItemClicked(int position) {
        Trailer trailer = trailerList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
        //Toast.makeText(this, "position" +position, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
    public void save_movie(View v){
        ContentValues contentValues = new ContentValues();
        Log.e("dsa","sdaada");
        Log.e("sda", movie.getId()+"");
        contentValues.put(MovieContract.MovieEntry.ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.POPULARITY, movie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.POSTER_PATH, movie.getPoster_path());
        contentValues.put(MovieContract.MovieEntry.BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.VOTE_COUNT, movie.getVoteCount());
        contentValues.put(MovieContract.MovieEntry.VOTE_AVERAGE, movie.getVoteAverage());
        // Insert the content values via a ContentResolver
        Log.i("Data Object", contentValues.toString());
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            Toast.makeText(getBaseContext(), "Telah Menambahkan ke Daftar Favorite.", Toast.LENGTH_LONG).show();
            markFavorite.setVisibility(View.GONE);
            unMarkFavorite.setVisibility(View.VISIBLE);
        }
    }

    public void delete_movie(View view){
        String[] stringArray = {String.valueOf(movie.getId())};

        markFavorite.setVisibility(View.VISIBLE);
        unMarkFavorite.setVisibility(View.GONE);

        // Insert the content values via a ContentResolver
        int res = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, stringArray);
        Log.i("DATA", String.valueOf(res));

        if(res > 0) {
            Toast.makeText(getBaseContext(), "Dihapus Dari Daftar Favorite.", Toast.LENGTH_LONG).show();
        }

    }

}
