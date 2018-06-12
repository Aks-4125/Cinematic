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
import com.training.cinematic.Model.TvResult;
import com.training.cinematic.R;
import com.training.cinematic.activity.MovieDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class PopularTvAdapter extends RecyclerView.Adapter<PopularTvAdapter.MyHolder> {
    Context context;
    int layout;
    List<TvResult> popularTv;
    String TV_POSTAR_URL;
    /*int[] img;
    String[] data;*/

    public PopularTvAdapter(Context context, int layout, List<TvResult> popularTv) {
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
        Log.d("TV", "POPULATTV NAMES" + popularTv.size());

        String tvDatee = popularTv.get(position).getFirstAirDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        String convertedDate = "";
        try {
            date = dateFormat.parse(tvDatee);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvDate.setText(convertedDate);
        holder.language.setText("Language:" + popularTv.get(position).getOriginalLanguage());
        //holder.tvDate.setText(popularTv.get(position).getFirstAirDate());
        String path = "https://image.tmdb.org/t/p/w200/";
        TV_POSTAR_URL = popularTv.get(position).getPosterPath();
        String imageUrl = path.concat(TV_POSTAR_URL);
        Log.d("image ", "image path for browser" + imageUrl);


        Picasso.with(context)
                .load(imageUrl)
                .into(holder.tvImage);


        holder.tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, MovieDetailActivity.class).putExtra("tvId", popularTv.get(position).getId()));
                Log.d("populat tv","id ---->>>>"+popularTv.get(position).getId());
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
        @BindView(R.id.txt_language)
        TextView language;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
