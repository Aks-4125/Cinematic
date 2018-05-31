package com.training.cinematic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.R;
import com.training.cinematic.activity.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.MyHolder> {

    List<MovieModel.Result>movies;
    int rowlayout;
    Context context;
    String MOVIES_POSTER_URL;
    int[] img;
    String[] data;

    public PopularMoviesAdapter(List<MovieModel.Result> movies, int rowlayout, Context context) {
        this.movies = movies;
        this.rowlayout = rowlayout;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularMoviesAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View myview=inflater.inflate(R.layout.movie_item,parent,false);
        return new PopularMoviesAdapter.MyHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.moviename.setText(movies.get(position).getTitle());
        String path = "https://image.tmdb.org/t/p/w200/";
        MOVIES_POSTER_URL = movies.get(position).getPosterPath();
        String imageurl = path.concat(MOVIES_POSTER_URL);
        holder.moviedate.setText(movies.get(position).getReleaseDate());
        Picasso.with(context)
                .load(imageurl)
                .into(holder.movieimage);



        holder.movieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context,MovieDetailActivity.class));

            }
        });
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img)
        ImageView movieimage;
        @BindView(R.id.movie_name)
        TextView moviename;
        @BindView(R.id.movie_date)
        TextView moviedate;
        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
