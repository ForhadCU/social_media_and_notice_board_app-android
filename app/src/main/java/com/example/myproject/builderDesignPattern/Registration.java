package com.example.myproject.builderDesignPattern;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Registration {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String uId;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    public Registration() {
    }

    public Registration(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public Registration(String username, String password, String email, String phone, String uId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.uId = uId;
    }


    public boolean inputValidation() {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {

            return true;
        } else
            return false;
    }

}
