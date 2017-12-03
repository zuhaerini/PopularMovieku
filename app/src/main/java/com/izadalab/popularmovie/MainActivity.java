package com.izadalab.popularmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.izadalab.popularmovie.adapter.MovieAdapter;
import com.izadalab.popularmovie.model.Movie;
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

public class MainActivity extends AppCompatActivity implements RecyclerViewItemClickListener{
    @BindView(R.id.rv_movie) RecyclerView rvmovie;
    @BindView(R.id.loading_bar)
    ProgressBar loadingProgress;

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new MovieAdapter(this, movieList, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvmovie.setLayoutManager(layoutManager);
        rvmovie.setAdapter(adapter);
        loadData();
    }
    private void loadData(){
        URL url = NetworkUtils.buildUrl("popular");
        new getDataTask().execute(url);

    }

    @Override
    public void onItemClicked(int position) {
        //Toast.makeText(this, "position" + position, Toast.LENGTH_SHORT).show();
        Movie movie = movieList.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_INTENT,movie);
        startActivity(intent);
    }

    private class getDataTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingProgress.setVisibility(View.VISIBLE);
            rvmovie.setVisibility(View.GONE);
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
            super.onPostExecute(s);
            Log.e("result",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");

                for(int i = 0; i < results.length(); i++){
                    movieList.add(new Movie(
                            results.getJSONObject(i).getInt("id"),
                            results.getJSONObject(i).getDouble("vote_average"),
                            results.getJSONObject(i).getString("title"),
                            results.getJSONObject(i).getString("poster_path"),
                            results.getJSONObject(i).getString("overview"),
                            results.getJSONObject(i).getString("release_date"))
                    );
                }
                Log.e("json_array", results.toString());

                rvmovie.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
