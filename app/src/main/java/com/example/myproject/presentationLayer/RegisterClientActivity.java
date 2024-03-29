package com.example.myproject.presentationLayer;
//Firebase Project: My-Project-Notifly

import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myproject.R;
import com.example.myproject.applicationLayer.builderDesignPattern.Registration;
import com.example.myproject.applicationLayer.builderDesignPattern.RegistrationBuilder;
import com.example.myproject.databaseLayer.models.Constants;
import com.example.myproject.databaseLayer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterClientActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Tag";
    private Toolbar toolbar;
    private EditText usernameEditText, emailEditText, phoneEditText, passEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private Map<String, Object> usersMap;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Create Account");

        mBind();
        mInit();
        mScreenConfig();
        mSetOnclick();
    }

    private void mSetOnclick() {
        signInTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void mInit() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(Constants.coll_users);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    private void mScreenConfig() {
        //set toolbar
        setSupportActionBar(toolbar);
        //add back up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void mBind() {
        usernameEditText = findViewById(R.id.usernameEditTextId);
        emailEditText = findViewById(R.id.emailEditTextId);
        phoneEditText = findViewById(R.id.phoneEditTextId);
        passEditText = findViewById(R.id.passEditTextId);
        signUpButton = findViewById(R.id.signUpButtonId);
        signInTextView = findViewById(R.id.signInTextId);
        toolbar = findViewById(R.id.toolbarId);
    }

    //add back up button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButtonId:

                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNum = phoneEditText.getText().toString().trim();
                String password = passEditText.getText().toString().trim();

                Registration registration = new RegistrationBuilder(password, email)
                        .setUsername(username)
                        .setPhone(phoneNum).Build();
                if (registration.inputValidation()) {
                    signUpMethod(username, email, phoneNum, password);
                }

                break;

            case R.id.signInTextId:
                Intent signInIntent = new Intent(RegisterClientActivity.this, LoginActivity.class);
                signInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signInIntent);
        }
    }

    //signUp method
    private void signUpMethod(final String username, final String email, final String phoneNum, final String password) {
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //verify email
                    mEmailVerification();

                    String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();   //String user_id = mAuth.getCurrentUser().getUid();
                    ArrayList<String> myGroups = new ArrayList<>();
                    ArrayList<String> joinedGroups = new ArrayList<>();

                    usersMap = new HashMap<>();
                    usersMap.put(Constants.username, username);
                    usersMap.put(Constants.password, password);
                    usersMap.put(Constants.email, email);
                    usersMap.put(Constants.phoneNum, phoneNum);
                    usersMap.put(Constants.userId, user_id);
                    usersMap.put(Constants.my_groups, myGroups);
                    usersMap.put(Constants.joined_groups, joinedGroups);

                    User user = new User(username, password, email, phoneNum, user_id);

                    collectionReference.document(user_id).set(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Intent mainIntent = new Intent(RegisterClientActivity.this, LoginActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+ e.toString());
                        }
                    });

                } else
                    Toast.makeText(getApplicationContext(), "Error in Signing Up", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        });
    }

    private void mEmailVerification() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email hasn't sent!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
