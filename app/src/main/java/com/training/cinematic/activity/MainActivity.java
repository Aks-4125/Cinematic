package com.training.cinematic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.training.cinematic.Fragment.PopularMoviesFragment;
import com.training.cinematic.Fragment.PopularTvFragment;
import com.training.cinematic.Fragment.UpComingMovieFragment;
import com.training.cinematic.R;

import java.util.ArrayList;
import java.util.List;

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
    private Realm realm;
    private ViewPagerAdapter adapter;
    PopularTvFragment popularTvFragment ;
    PopularMoviesFragment popularMoviesFragment;
    UpComingMovieFragment upComingMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
       /*  popularTvFragment=new PopularTvFragment() ;
         popularMoviesFragment=new PopularMoviesFragment();
        upComingMovieFragment=new UpComingMovieFragment();*/
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PopularMoviesFragment(), "Popular Movies");
        viewPager.setCurrentItem(0);
        adapter.addFrag(new UpComingMovieFragment(), "Upcoming Movies");
        viewPager.setCurrentItem(1);
        adapter.addFrag(new PopularTvFragment(), "Popular tv Shows");
        viewPager.setCurrentItem(2);
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences(FLAG, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
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


}
