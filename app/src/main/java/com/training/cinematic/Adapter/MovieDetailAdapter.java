package com.training.cinematic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.training.cinematic.R;

import java.util.ArrayList;

/**
 * Created by dhruvisha on 5/28/2018.
 */

public class MovieDetailAdapter extends PagerAdapter {
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;


    public MovieDetailAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

    public Object instantiateItem(ViewGroup view, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myimage = (ImageView) myImageLayout.findViewById(R.id.image);
        String path = "https://image.tmdb.org/t/p/w500/";
        String img = "";
        img = images.get(position);
        Log.d("slider", "slider imagepath" + path + img);
        Picasso.with(context)
                .load(path + img)
                .into(myimage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
