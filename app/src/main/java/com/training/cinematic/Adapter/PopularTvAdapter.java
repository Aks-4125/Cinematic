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

import com.training.cinematic.R;
import com.training.cinematic.activity.MovieDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class PopularTvAdapter extends RecyclerView.Adapter<PopularTvAdapter.MyHolder> {
    Context context;
    int[] img;
    String[] data;

    public PopularTvAdapter(Context context, int[] img, String[] data) {
        this.context = context;
        this.img = img;
        this.data = data;
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
        holder.movieimage.setImageResource(img[position]);
        // holder.moviename.setText(data[position]);


        holder.movieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, MovieDetailActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return img.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img)
        ImageView movieimage;
        @BindView(R.id.movie_name)
        TextView moviename;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}