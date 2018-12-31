package kh.edu.rupp.ckcc.derkamsan.Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Arrays;

import kh.edu.rupp.ckcc.derkamsan.Events.Event;
import kh.edu.rupp.ckcc.derkamsan.LibraryHelper;
import kh.edu.rupp.ckcc.derkamsan.MainActivity;
import kh.edu.rupp.ckcc.derkamsan.R;
import kh.edu.rupp.ckcc.derkamsan.Singleton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText EmailPhone, Password;
    private TextView ForgetPassword, Register;
    private ImageButton ibSignIn;
    private LoginButton btnLoginFB;
    private ImageView ivBack;
    private kh.edu.rupp.ckcc.derkamsan.Profiles.Profile profile;
    private Context context;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context =LoginActivity.this;

        EmailPhone = findViewById(R.id.etxt_email_phone);
        Password = findViewById(R.id.etxtPassword);
        ForgetPassword = findViewById(R.id.tvForgot_passw);
        ibSignIn = findViewById(R.id.ibSignIn);
        ivBack = findViewById(R.id.ivBack);
        Register = findViewById(R.id.tvRegister);
        btnLoginFB = findViewById(R.id.btnFacebookLogin);


        checkIfUserAlreadyLoggedIn();

        Register.setOnClickListener(this);
        ibSignIn.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ForgetPassword.setOnClickListener(this);
//        btnLoginFB.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();
        btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // Pass token to Firebase Auth to manage

                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            com.facebook.Profile profile1 = com.facebook.Profile.getCurrentProfile();
                            profile.setId(profile1.getId());
                            profile.setUserName(profile1.getName());
                            profile.setImageUrl(profile1.getProfilePictureUri(200, 200).toString());
                            Singleton.getInstance().setProfile(profile);
                            saveProfileInSharedPref(profile);
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        } else {
                            Log.d("Der_Kamsan", "Login with Firebase error: " + task.getException());
                        }
                    }
                });
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("LoginActivity", response.toString());
//
//                                // Application code
//                                try {
//                                    String email = object.getString("email");
//                                    String birthday = object.getString("birthday"); // 01/31/1980 format
//
//                                    Gson gson = new Gson();
//                                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                                    String gsonLogin = gson.toJson(loginResult);
//                                    intent.putExtra("LoginResultData", gsonLogin);
//                                    Log.v("LoginActivity", email);
//                                    intent.putExtra("email",email);
//                                    startActivity(intent);
//                                    finish();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void saveProfileInSharedPref(Profile profile){
        SharedPreferences sharedPreferences = getSharedPreferences("pro",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String profileJsonString = gson.toJson(profile);
        editor.putString("profile", profileJsonString);
        editor.apply();
    }
    private void checkIfUserAlreadyLoggedIn() {
        // Check login via username/password
        SharedPreferences preferences = getSharedPreferences("pro", MODE_PRIVATE);
        String profileJsonString = preferences.getString("profile", null);
        if (profileJsonString != null) {
            Gson gson = new Gson();
            Profile profile = gson.fromJson(profileJsonString, Profile.class);
            Singleton.getInstance().setProfile(profile);
            // Start MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            ProfileFragment fragment = new ProfileFragment();
            LibraryHelper.displayFragment(fragment,getSupportFragmentManager(),R.id.lyt_container);

            // Finish current activity
            finish();

            Log.d("check","check user with firestore: ");
        }
        // check login via Facebook
        else if (AccessToken.getCurrentAccessToken() != null) {
            // Start MainActivity
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
            ProfileFragment fragment = new ProfileFragment();
            LibraryHelper.displayFragment(fragment,getSupportFragmentManager(),R.id.lyt_container);
            // Finish current activity
            finish();
            Log.d("check","check user with facebook: ");
        }
        Log.d("check","Both check user : ");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
       finish();

    }


    public void login(String Email, String Phone, String Password) {
        if ((Email == "kkk") || (Phone == "098765432") && (Password == "123")) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.ibSignIn:
                signInWithFireBase();
                break;
            case R.id.tvForgot_passw:
                break;
        }
    }

    private void signInWithFireBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profiles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                kh.edu.rupp.ckcc.derkamsan.Profiles.Profile[] profiles = new kh.edu.rupp.ckcc.derkamsan.Profiles.Profile[queryDocumentSnapshots.size()];
                int index = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    kh.edu.rupp.ckcc.derkamsan.Profiles.Profile profile = documentSnapshot.toObject(kh.edu.rupp.ckcc.derkamsan.Profiles.Profile.class);

//                    if ((EmailPhone.getText().toString() == profile.getEmail() || ))

                }
                if (index>0){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Singleton.getInstance().setProfile(profile);
                }else {
//
//                    LibraryHelper.showAlertDialog("Message","Invalid Username or Password",context);
                }

            }
        });
    }

}
