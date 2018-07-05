package com.training.cinematic.activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.image1)
    ImageView profilePic;
    @BindView(R.id.image)
    ImageView proPic;
    @BindView(R.id.name)
    TextView fullname;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.email)
    TextView email;
   /* @BindView(R.id.password)
    TextView password;*/
    Intent intent;
    private static final String TAG = ProfileActivity.class.getName();
    JSONObject response, profile_pic_data, profile_pic_url;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        intent = getIntent();
        realm = Realm.getDefaultInstance();
        String jsonData = SharedPrefsHelp.getString(ProfileActivity.this, "userProfile", null);
        email.setText(SharedPrefsHelp.getString(ProfileActivity.this, getString(R.string.get_email_pref), ""));
        String stringEmail = email.getText().toString();
        if (jsonData!=null) {
            try {
                response = new JSONObject(jsonData);
                if (response.get("email").toString()!=null){
                    email.setText(response.get("email").toString());
                }
                else {
                    email.setText("Not Avalible!");
                }
                fullname.setText(response.get("name").toString());
                profile_pic_data = new JSONObject(response.get("picture").toString());
                profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                Picasso.with(this).load(profile_pic_url.getString("url"))
                        .into(profilePic);
                Picasso.with(this).load(profile_pic_url.getString("url"))
                        .into(proPic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        realm.beginTransaction();
        User user = realm.where(User.class)
                .equalTo("emailId", stringEmail)
                .findFirst();
        if (user != null) {
            number.setText(user.getPhoneNumber());
            fullname.setText(user.getFullName());
          //  password.setText(user.getPassword());
        }
        realm.commitTransaction();

    }
}
