package com.example.dancestudioapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.dancestudioapp.databinding.ActivityRegisterBinding;
import com.example.dancestudioapp.models.User;
import com.example.dancestudioapp.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding activityRegisterBinding;
    private FirebaseDatabase DanceStudio;
    private FirebaseAuth mAuth;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = activityRegisterBinding.getRoot();
        setContentView(view);
        init();
        setListeners();
    }


    private void setListeners() {
        activityRegisterBinding.enter.setOnClickListener(v -> onClickSignUp());
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        DanceStudio = FirebaseDatabase.getInstance();
        users = DanceStudio.getReference(Constants.KEY_COLLECTION_USERS);
    }

    public void onClickSignInNow(View view) {
        finish();
    }

    public void onClickSignUp() {
        String email, password, name;
        email = activityRegisterBinding.emailEdittext.getText().toString();
        password = activityRegisterBinding.passwordEdittext.getText().toString();
        name = activityRegisterBinding.nameEdittext.getText().toString();
        if(!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(name)) {

            mAuth.createUserWithEmailAndPassword(activityRegisterBinding.emailEdittext.getText().toString(),
                            activityRegisterBinding.passwordEdittext.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if(currentUser != null) {
                            User newUser = new User(currentUser.getUid(), email, password, name, Constants.DEFAULT_ROLE);
                            users.child(currentUser.getUid()).setValue(newUser);
                            startActivity(new Intent(getApplicationContext(), TheMostMainActivity.class));
                        }
                        Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}