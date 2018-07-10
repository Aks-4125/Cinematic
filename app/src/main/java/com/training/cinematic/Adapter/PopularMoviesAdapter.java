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
import com.training.cinematic.Model.PopularMovieResult;
import com.training.cinematic.R;
import com.training.cinematic.activity.CategoryEnum;
import com.training.cinematic.activity.detailscreen.DetailScreenActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.training.cinematic.Utils.ConstantHelper.CAT_NAME;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.MyHolder> {

    List<PopularMovieResult> movies;
    int rowlayout;
    Context context;
    String MOVIES_POSTER_URL;

    public PopularMoviesAdapter(List<PopularMovieResult> movies, int rowlayout, Context context) {
        this.movies = movies;
        this.rowlayout = rowlayout;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularMoviesAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myview = inflater.inflate(R.layout.movie_item, parent, false);
        return new PopularMoviesAdapter.MyHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.movieName.setText(movies.get(position).getTitle());
        String path = "https://image.tmdb.org/t/p/w500/";
        MOVIES_POSTER_URL = movies.get(position).getPosterPath();
        String imageurl = path.concat(MOVIES_POSTER_URL);
        String movieDate = movies.get(position).getReleaseDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        String convertedDate = "";
        try {
            date = dateFormat.parse(movieDate);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.movieDate.setText(convertedDate);
        holder.movieLanguage.setText("Language:" + movies.get(position).getOriginalLanguage());

        Picasso.with(context)
                .load(imageurl)
                .into(holder.movieImage);


        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailScreenActivity.class).putExtra("movieId", movies.get(position).getId());
                intent.putExtra(CAT_NAME, CategoryEnum.category.MOVIES.getValue());
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img)
        ImageView movieImage;
        @BindView(R.id.movie_name)
        TextView movieName;
        @BindView(R.id.movie_date)
        TextView movieDate;
        @BindView(R.id.txt_language)
        TextView movieLanguage;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
