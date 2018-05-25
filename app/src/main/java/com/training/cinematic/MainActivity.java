package com.training.cinematic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.training.cinematic.Model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Optional;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvOnboarding)
    TextView tvOnboarding;
    @BindView(R.id.btnClickMe)
    AppCompatButton btnClickMe;
    private static final String TAG = "Main Activitity";
    private static final String FLAG = "flag";
    String KEY_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
                editor.remove(KEY_NAME);
                editor.clear();
                editor.commit();
                finish();
                return true;
            case R.id.profile:
                Toast.makeText(this, "User Profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected((MenuItem) item);

        }

    }

    @OnClick(R.id.btnClickMe)
    public void onViewClicked() {


        Snackbar.make(btnClickMe, tvOnboarding.getText().toString(), Snackbar.LENGTH_SHORT).show();
    }
}
