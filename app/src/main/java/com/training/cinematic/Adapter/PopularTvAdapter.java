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
import com.training.cinematic.Utils.Utils;
import com.training.cinematic.activity.CategoryEnum;
import com.training.cinematic.activity.detailscreen.DetailScreenActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.training.cinematic.Utils.ConstantHelper.CAT_NAME;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class PopularTvAdapter extends RecyclerView.Adapter<PopularTvAdapter.MyHolder> {
    Context context;
    int layout;
    List<TvResult> popularTv;
    String TV_POSTAR_URL;
    Utils utils;
    String convertedDate = "";


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
        utils = new Utils(context);
        return new PopularTvAdapter.MyHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularTvAdapter.MyHolder holder, int position) {

        holder.tvName.setText(popularTv.get(position).getName());
        Log.d("TV", "POPULATTV NAMES" + popularTv.size());

        String tvDate = popularTv.get(position).getFirstAirDate();
        convertedDate = utils.convertDate(tvDate, context);
        holder.tvDate.setText(convertedDate);
        holder.language.setText("Language:" + popularTv.get(position).getOriginalLanguage());
        String path = "https://image.tmdb.org/t/p/w500/";
        TV_POSTAR_URL = popularTv.get(position).getPosterPath();
        String imageUrl = path.concat(TV_POSTAR_URL);
        Log.d("image ", "image path for browser" + imageUrl);


        Picasso.with(context)
                .load(imageUrl)
                .into(holder.tvImage);


        holder.tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailScreenActivity.class).putExtra("tvId", popularTv.get(position).getId());
                intent.putExtra(CAT_NAME, CategoryEnum.category.TV.getValue());
                view.getContext().startActivity(intent);
                Log.d("populat tv", "id ---->>>>" + popularTv.get(position).getId());
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
