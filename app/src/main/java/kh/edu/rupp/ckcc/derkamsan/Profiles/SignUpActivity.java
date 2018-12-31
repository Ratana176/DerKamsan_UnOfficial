package kh.edu.rupp.ckcc.derkamsan.Profiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kh.edu.rupp.ckcc.derkamsan.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText UserName, Email, Password, ConfirmPassw;
    private ImageButton ibSignUp, ibCancel;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout tilPass;
    private ImageView ivBack;
    private Profile[] profiles;
    private PhoneNumberUtils phoneNumberUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        UserName = findViewById(R.id.etUsername);
        Email = findViewById(R.id.et_email_phone);
        Password = findViewById(R.id.etPassword);
        ConfirmPassw  = findViewById(R.id.etConfirm_Password);
        ibSignUp = findViewById(R.id.ibSignUp);
        ibCancel = findViewById(R.id.ibCancel);
        tilPass = findViewById(R.id.tilPass);
        ivBack = findViewById(R.id.ivBack);
        firebaseAuth = FirebaseAuth.getInstance();

        ibCancel.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ConfirmPassw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tilPass.getEditText().getText()==ConfirmPassw.getText()){
                    ConfirmPassw.setError(null);
                }else
                    ConfirmPassw.setError("Password not match!");
            }
        });
        ibSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(signup()){
                   //upload data to database
                   String user_email_phone = Email.getText().toString().trim();
                   String user_password = Password.getText().toString().trim();
                   String user_name = UserName.getText().toString().trim();
                    loadProfilesFromFirestore();
                   for (Profile p :profiles) {
                       if(p.getUserName()==user_name)
                           Toast.makeText(SignUpActivity.this,"UserName exist ",Toast.LENGTH_LONG).show();
                   }



               }
            }
        });

    }
    private void insertUser(String userName,String userEmail_phone,String userPassword){

    }
    public boolean signup(){
        String name = UserName.getText().toString();
        String email = Email.getText().toString();
        String password  = Password.getText().toString();
        String confirmPass = ConfirmPassw.getText().toString();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
            Toast.makeText(this,"Please enter all the detail!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean validCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    private void loadProfilesFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Realtime query
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                profiles = new Profile[queryDocumentSnapshots.size()];
                int index = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Profile profile = documentSnapshot.toObject(Profile.class);
                    profile.setId(documentSnapshot.getId());
                    profiles[index] = profile;
                    index++;
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
            break;
           case R.id.ibCancel:
                onBackPressed();
                break;



        }
        }
    }


