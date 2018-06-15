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
import com.training.cinematic.Model.MovieResult;
import com.training.cinematic.R;
import com.training.cinematic.activity.MovieDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by dhruvisha on 5/28/2018.
 */

public class UpComingMovieAdapter extends RecyclerView.Adapter<UpComingMovieAdapter.MyHolder> {
    Context context;
    private String MOVIES_POSTER_URL;
    int layout;


    List<MovieResult> movieResponse;

    public UpComingMovieAdapter(Context context, List<MovieResult> movieResponse, int layout) {
        this.context = context;
        this.layout = layout;
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
        MovieResult movie = movieResponse.get(position);

        holder.moviename.setText(movie.getTitle());

        String dateString = movie.getReleaseDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        String convertedDate = "";
        try {
            date = dateFormat.parse(dateString);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d("date", "converted dateee" + convertedDate.toString());
        holder.moviedate.setText(convertedDate);
     /*   Boolean isAdult=movieDetail.getAdult();
        Log.d("adult","adulttttt"+isAdult);
        if (isAdult==true){
            holder.adult.setVisibility(View.VISIBLE);
        }*/

        holder.language.setText("Language:" + movie.getOriginalLanguage());
        String path = "https://image.tmdb.org/t/p/w500/";
        MOVIES_POSTER_URL = movie.getPosterPath();
        String imageurl = path.concat(MOVIES_POSTER_URL);

        Log.d("image path", imageurl);
        Picasso.with(context)
                .load(imageurl)
                .into(holder.movieimage);


        holder.movieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MovieDetailActivity.class).putExtra("movieId", movie.getId()));

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
        @BindView(R.id.txt_language)
        TextView language;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

    }
}
