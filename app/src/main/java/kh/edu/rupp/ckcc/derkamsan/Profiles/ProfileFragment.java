package kh.edu.rupp.ckcc.derkamsan.Profiles;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import kh.edu.rupp.ckcc.derkamsan.R;


public class ProfileFragment extends Fragment {
    private TextView  Name,phoneNumber_eamil,gender;
    private SimpleDraweeView Cover;
    private ImageButton Back;
    private SimpleDraweeView imgProfile;
    private Profile profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Name = view.findViewById(R.id.tvName);
        Cover = view.findViewById(R.id.ivCover);
        imgProfile = view.findViewById(R.id.ivProfile);
        phoneNumber_eamil = view.findViewById(R.id.tvPH_email);
        gender = view.findViewById(R.id.tvGender);

        Intent intent = getActivity().getIntent();
        String loginResultData = intent.getStringExtra("LoginResultData");
        String email = intent.getStringExtra("email");
        phoneNumber_eamil.setText(email);
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
