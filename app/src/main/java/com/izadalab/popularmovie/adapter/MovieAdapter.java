package com.izadalab.popularmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.izadalab.popularmovie.R;
import com.izadalab.popularmovie.model.Movie;
import com.izadalab.popularmovie.utils.NetworkUtils;
import com.izadalab.popularmovie.utils.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DhytoDev on 12/1/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context ;
    private List<Movie> movieList;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public MovieAdapter(Context context, List<Movie> movieList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.movie = movieList.get(position);
        Glide.with(context)
                .load(NetworkUtils.buildMoviePosterUrl(holder.movie.getPoster_path()))
                .into(holder.moviePoster);
        holder.itemView.setOnClickListener(view -> {
            int itemPosition = holder.getAdapterPosition();
            recyclerViewItemClickListener.onItemClicked(itemPosition);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_poster)
        ImageView moviePoster;
        Movie movie;
        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
