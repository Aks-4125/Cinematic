package com.training.cinematic.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.cinematic.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruvisha on 5/26/2018.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView moviename;
    public ImageView movieimage;
    public TextView moviedate;
    public RecyclerViewHolders(View layoutview) {
        super(layoutview);
        itemView.setOnClickListener(this);
        moviedate=itemView.findViewById(R.id.movie_date);
        movieimage=itemView.findViewById(R.id.movie_img);
        moviename=itemView.findViewById(R.id.movie_name);
    }
    @Override
    public void onClick(View v) {

    }
}
