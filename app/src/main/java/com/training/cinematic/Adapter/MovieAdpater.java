package com.training.cinematic.Adapter;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.cinematic.R;
import com.training.cinematic.activity.RecyclerViewHolders;

import java.util.List;

/**
 * Created by dhruvisha on 5/26/2018.
 */

public class MovieAdpater extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List itemlist;
    private Context context;

    public MovieAdpater(List itemlist, Context context) {
        this.itemlist = itemlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutview= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,null);
        RecyclerViewHolders rcv=new RecyclerViewHolders(layoutview);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
      /*  holder.moviename.setText(itemlist.get(position).getName());
        holder.moviedate.setText(itemlist.get(position).get);
        holder.movieimage.setImageResource(itemlist.get(position).getPhoto());*/
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}
