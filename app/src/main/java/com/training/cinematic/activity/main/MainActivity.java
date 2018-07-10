package com.training.cinematic.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.training.cinematic.Adapter.MainAdapter;
import com.training.cinematic.Fragment.poplarmovie.PopularMoviesFragment;
import com.training.cinematic.Fragment.populartv.PopularTvFragment;
import com.training.cinematic.Fragment.upcomingmovie.UpComingMovieFragment;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;
import com.training.cinematic.activity.Profile.ProfileActivity;
import com.training.cinematic.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final String FLAG = "flag";
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    boolean doublePresstoExit = false;
    @BindView(R.id.constrainlayout)
    ConstraintLayout constraintLayout;
    private MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFrag(new PopularMoviesFragment(), "Popular ");
        viewPager.setCurrentItem(0);
        adapter.addFrag(new UpComingMovieFragment(), "Upcoming ");
        viewPager.setCurrentItem(1);
        adapter.addFrag(new PopularTvFragment(), "Tv Shows");
        viewPager.setCurrentItem(2);
        viewPager.setAdapter(adapter);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefsHelp.clearSharedPrefs(MainActivity.this);
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                LoginManager.getInstance().logOut();
                return true;
            case R.id.profile:
                Toast.makeText(this, "User Profile.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected((MenuItem) item);
        }

    }

    public void onBackPressed() {
        if (doublePresstoExit) {
            super.onBackPressed();
            return;
        }
        this.doublePresstoExit = true;
        Snackbar.make(constraintLayout, "Please Click Back again to Exit", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePresstoExit = false;
            }
        }, 2000);

    }

    public void onDestroy() {
        super.onDestroy();
    }
}
