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
import com.training.cinematic.Model.TvModel;
import com.training.cinematic.R;
import com.training.cinematic.activity.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class PopularTvAdapter extends RecyclerView.Adapter<PopularTvAdapter.MyHolder> {
    Context context;
    int layout;
    List<TvModel.Result>popularTv;
    String TV_POSTAR_URL;
    /*int[] img;
    String[] data;*/

    public PopularTvAdapter(Context context, int layout, List<TvModel.Result> popularTv) {
        this.context = context;
        this.layout = layout;
        this.popularTv = popularTv;
    }

    @NonNull
    @Override
    public PopularTvAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myview = inflater.inflate(R.layout.movie_item, parent, false);
        return new PopularTvAdapter.MyHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularTvAdapter.MyHolder holder, int position) {
     //   holder.movieImage.setImageResource(img[position]);
        // holder.movieName.setText(data[position]);
        holder.tvName.setText(popularTv.get(position).getName());
        Log.d("TV","POPULATTV NAMES"+popularTv.size());
        holder.tvDate.setText(popularTv.get(position).getFirstAirDate());
        String path = "https://image.tmdb.org/t/p/w200/";
        TV_POSTAR_URL=popularTv.get(position).getPosterPath();
        String imageUrl=path.concat(TV_POSTAR_URL);
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.tvImage);


        holder.tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, MovieDetailActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return popularTv.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img)
        ImageView tvImage;
        @BindView(R.id.movie_name)
        TextView tvName;
        @BindView(R.id.movie_date)
        TextView tvDate;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
