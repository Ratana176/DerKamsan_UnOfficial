package kh.edu.rupp.ckcc.derkamsan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private EditText txtEmail;
    private EditText txtSubject;
    private EditText txtMessage;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_us);

        toolbar = findViewById(R.id.ContactUsToolbar);
        setSupportActionBar(toolbar);

        txtEmail = findViewById(R.id.txtEmail);
        txtSubject = findViewById(R.id.txtSubject);
        txtMessage = findViewById(R.id.txtMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String emailTo = txtEmail.getText().toString();
        String subject = txtSubject.getText().toString();
        String message = txtMessage.getText().toString();
        Intent gmailFeedback = new Intent(Intent.ACTION_SEND);
        gmailFeedback.setType("text/email");
        gmailFeedback.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        gmailFeedback.putExtra(Intent.EXTRA_SUBJECT, subject);
        gmailFeedback.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(Intent.createChooser(gmailFeedback, "Sending feedback"));


    }
}
