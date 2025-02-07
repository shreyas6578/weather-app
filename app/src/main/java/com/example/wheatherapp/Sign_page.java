package com.example.wheatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Sign_page extends AppCompatActivity {
    private static final String TAG = "Sign_page";
    private TextView login;
    private EditText email, password;
    private FirebaseAuth mAuth;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_page);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);
        login = findViewById(R.id.tv_sign_up);
        signUpButton = findViewById(R.id.btn_login);

        // Navigate to Login page
        login.setOnClickListener(v -> startActivity(new Intent(Sign_page.this, Login_page.class)));

        // Handle Sign-up Button Click
        signUpButton.setOnClickListener(v -> handleEmailPasswordSignUp());
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 ;
    }

    private void handleEmailPasswordSignUp() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordStrong(passwordText)) {
            Toast.makeText(this, "Password is too weak. It should be at least 8 characters long", Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog while registering
        ProgressDialog progressDialog = new ProgressDialog(Sign_page.this);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();  // Dismiss progress dialog
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "User registered: " + (user != null ? user.getEmail() : "Unknown"));
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        email.setText("");  // Clear email field
                        password.setText("");  // Clear password field
                        startActivity(new Intent(Sign_page.this, Login_page.class));
                        finish();
                    } else {
                        handleFirebaseError(task.getException());
                    }
                });
    }

    private void handleFirebaseError(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            switch (errorCode) {
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    Toast.makeText(this, "This email is already in use. Please use another email.", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_INVALID_EMAIL":
                    Toast.makeText(this, "Invalid email format. Please check and try again.", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_WEAK_PASSWORD":
                    Toast.makeText(this, "Weak password. Please use a stronger password.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "An unknown error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Registration error: ", exception);
        }
    }
}
