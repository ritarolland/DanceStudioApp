package com.example.dancestudioapp.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.dancestudioapp.databinding.ActivityAuthBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    ActivityAuthBinding activityAuthBinding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = activityAuthBinding.getRoot();
        setContentView(view);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(this, TheMostMainActivity.class));
        }
    }

    public void onClickSignUpNow(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickSignIn(View view) {
        if(!TextUtils.isEmpty(activityAuthBinding.emailEdittext.getText().toString())
                && !TextUtils.isEmpty(activityAuthBinding.passwordEdittext.getText().toString())) {

            mAuth.signInWithEmailAndPassword(activityAuthBinding.emailEdittext.getText().toString(),
                            activityAuthBinding.passwordEdittext.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(getApplicationContext(), TheMostMainActivity.class));
                            Toast.makeText(getApplicationContext(), "Signed in successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Завершаем текущую активность и все связанные активности в стеке
        finishAffinity();
    }

}