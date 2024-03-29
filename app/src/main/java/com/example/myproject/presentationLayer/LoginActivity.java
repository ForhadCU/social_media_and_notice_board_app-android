package com.example.myproject.presentationLayer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myproject.R;
import com.example.myproject.applicationLayer.interfaces.InputValidationInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, InputValidationInterface {
    private TextView signUpTextView;
    private Button signInButton;
    private TextInputEditText emailEditText, passEditText;
    private ProgressBar progressBar;

    private String uID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //hide title
//        getSupportActionBar().hide();

        mBind();
        mInit();
        mSetOnClick();
    }

    private void mSetOnClick() {
        signUpTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    private void mBind() {
        signUpTextView = findViewById(R.id.signUpTextId);
        signInButton = findViewById(R.id.signInButtonId);
        emailEditText = findViewById(R.id.emailEditTextId);
        passEditText = findViewById(R.id.passEditTextId);
        progressBar = findViewById(R.id.progressbar);
    }

    private void mInit() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpTextId:
                Intent signUpActivity = new Intent(LoginActivity.this, RegisterClientActivity.class);
                signUpActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signUpActivity);
                break;

            case R.id.signInButtonId:
                String email = emailEditText.getText().toString().trim();
                String password = passEditText.getText().toString().trim();
                mValidation(email, password);
        }
    }

    //sign in
    private void signInMethod(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please verify your email.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);

                } else
                    Toast.makeText(getApplicationContext(), "Error in Sign in", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void mValidation(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
            signInMethod(email, password);
        else
            Toast.makeText(getApplicationContext(), "Field is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mValidation(String email, String password, String username, String phoneNum) {
    }
}
