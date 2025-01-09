package com.example.wheatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_page extends AppCompatActivity {
    TextView login;
    EditText email, password;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);
        login = findViewById(R.id.tv_sign_up);
        Button signUpButton = findViewById(R.id.btn_login);

        // Set onClickListener for login (navigate to Login page)
        login.setOnClickListener(v -> {
            Intent intent = new Intent(Sign_page.this, Login_page.class);
            startActivity(intent);
        });

        // Set onClickListener for sign-up (authenticate with Firebase)
        signUpButton.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            // Check if email and password are not empty
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(Sign_page.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sign-in with Firebase Authentication
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign-in successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("Sign_page", "User signed in: " + user.getEmail());
                            Intent intent = new Intent(Sign_page.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign-in failed
                            Toast.makeText(Sign_page.this, "Authentication failed. Please check your credentials.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}