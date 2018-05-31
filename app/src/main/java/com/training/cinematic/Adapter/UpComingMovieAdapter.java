package com.training.cinematic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class UpComingMovieAdapter extends RecyclerView.Adapter<UpComingMovieAdapter.MyHolder> {
    Context context;
    private static String MOVIES_POSTER_URL;


    List<MovieModel.Result> movieResponse;

    public UpComingMovieAdapter(Context activity, List<MovieModel.Result> movieResponse) {
        this.context = activity;
        this.movieResponse = movieResponse;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View myview = inflater.inflate(R.layout.movie_item, parent, false);
        return new MyHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        /*holder.movieImage.setImageResource(img[position]);
        holder.movieName.setText(data[position]);



      */
        MovieModel.Result movie = movieResponse.get(position);
        holder.moviename.setText(movie.getTitle());
        holder.moviedate.setText(movie.getReleaseDate());
        //    holder.movieImage.setImageResource(movie.getPosterPath("https://image.tmdb.org/t/p/w200"));
        //  movie.setPosterPath("https://image.tmdb.org/t/p/w200");

        String path = "https://image.tmdb.org/t/p/w200/";
        MOVIES_POSTER_URL = movie.getPosterPath();
        String imageurl = path.concat(MOVIES_POSTER_URL);

        Log.d("image path", imageurl);
        Picasso.with(context)
                .load(imageurl)
                .into(holder.movieimage);
        holder.movieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MovieDetailActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return movieResponse.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_name)
        TextView moviename;
        @BindView(R.id.movie_img)
        ImageView movieimage;
        @BindView(R.id.movie_date)
        TextView moviedate;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
