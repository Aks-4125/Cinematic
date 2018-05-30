package com.training.cinematic.activity;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.training.cinematic.Adapter.Slider_adapter;
import com.training.cinematic.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class sliderActivity extends AppCompatActivity {
  private static  ViewPager viewPager;

    private static int currentpage = 0;
    private static final Integer[] abc = {R.drawable.blackba, R.drawable.pin, R.drawable.blur, R.drawable.bg_screen, R.drawable.newback};
    private ArrayList<Integer> array = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        init();
    }

    private void init() {
        for (int i = 0; i < abc.length; i++)
            array.add(abc[i]);
            viewPager=(ViewPager)findViewById(R.id.viewpager1);
            CircleIndicator indicator=(CircleIndicator)findViewById(R.id.circleindicator);
            viewPager.setAdapter(new Slider_adapter(sliderActivity.this, array));
            indicator.setViewPager(viewPager);

            final Handler handeer = new Handler();
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    if (currentpage == abc.length) {
                        currentpage = 0;
                    }
                    viewPager.setCurrentItem(currentpage++, true);
                }
            };

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handeer.post(run);
                }
            }, 2500, 2500);
        }

}
