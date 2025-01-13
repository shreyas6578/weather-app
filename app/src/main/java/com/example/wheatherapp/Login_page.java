package com.example.wheatherapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.SharedPreferencesKt;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_page extends AppCompatActivity {
TextView Sign,email,password;
FirebaseAuth mAuth;
    SharedPreferences sharedPref;
    CheckBox saveCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
      sharedPref = getPreferences(Context.MODE_PRIVATE);
        // Initialize Firebase Auth and SharedPreferences
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        Sign = findViewById(R.id.tv_sign_up);
        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);
        saveCheckBox = findViewById(R.id.save);
        Button login_in = findViewById(R.id.btn_login);

        // Load saved credentials (if any)
        loadSavedCredentials();

        // Handle sign-up navigation
        Sign.setOnClickListener(click -> {
            Intent intent = new Intent(Login_page.this, Sign_page.class);
            startActivity(intent);
        });

        // Handle login button click
        login_in.setOnClickListener(click -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (saveCheckBox.isChecked()) {
                                    saveCredentials(emailText, passwordText);
                                } else {
                                    clearSavedCredentials();
                                }

                                Intent intent = new Intent(Login_page.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login_page.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
    private void loadSavedCredentials() {
        String savedEmail = sharedPref.getString("email", "");
        String savedPassword = sharedPref.getString("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            email.setText(savedEmail);
            password.setText(savedPassword);
            saveCheckBox.setChecked(true);
        }
    }
    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }

    }