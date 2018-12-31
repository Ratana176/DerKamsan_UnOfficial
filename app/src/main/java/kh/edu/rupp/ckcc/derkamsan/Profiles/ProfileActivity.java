package kh.edu.rupp.ckcc.derkamsan.Profiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import kh.edu.rupp.ckcc.derkamsan.R;


public class ProfileActivity extends AppCompatActivity {
    private TextView  Name,phoneNumber_eamil,gender;
    private SimpleDraweeView Cover;
    private ImageButton Back;
    private SimpleDraweeView imgProfile;
    private Profile profile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        Name = findViewById(R.id.tvName);
        Cover = findViewById(R.id.ivCover);
        imgProfile = findViewById(R.id.ivProfile);
        phoneNumber_eamil = findViewById(R.id.tvPH_email);
        gender = findViewById(R.id.tvGender);

        Intent intent = getIntent();
        String loginResultData = intent.getStringExtra("LoginResultData");
        String email = intent.getStringExtra("email");
        phoneNumber_eamil.setText(email);
//        SharedPreferences preferences = getSharedPreferences("pro", MODE_PRIVATE);
//        String profileJsonString = preferences.getString("profile", null);
//        Gson gson = new Gson();
//        profile = gson.fromJson(profileJsonString, Profile.class);
//        Name.setText(profile.getUserName());
//        imgProfile.setImageURI(profile.getImageUrl());

        Gson gson = new Gson();
        LoginResult loginResult = gson.fromJson(loginResultData,LoginResult.class);
        setFacebookData(loginResult);
    }


    private void setFacebookData(LoginResult loginResult) {
        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
        Name.setText(profile.getName());
        imgProfile.setImageURI(profile.getProfilePictureUri(500,500));
    }
}
