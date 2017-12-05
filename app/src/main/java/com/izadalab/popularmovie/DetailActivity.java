package com.izadalab.popularmovie;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.izadalab.popularmovie.adapter.ReviewAdapter;
import com.izadalab.popularmovie.adapter.TrailerAdapter;
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

    private List<Trailer> trailerList = new ArrayList<>();
    private TrailerAdapter adapterTrailer;

    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter adapterReview;

    public static final String MOVIE_INTENT = "movie.intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Movie movie = getIntent().getParcelableExtra(MOVIE_INTENT);
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

    @Override
    public void onItemClicked(int position) {
        Trailer trailer = trailerList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
        //Toast.makeText(this, "position" +position, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
