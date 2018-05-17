package com.training.cinematic;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvOnboarding)
    TextView tvOnboarding;
    @BindView(R.id.btnClickMe)
    AppCompatButton btnClickMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnClickMe)
    public void onViewClicked() {
        Snackbar.make(btnClickMe, tvOnboarding.getText().toString(), Snackbar.LENGTH_SHORT).show();
    }
}
