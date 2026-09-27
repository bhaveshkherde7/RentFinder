package com.example.rentfinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    // =========================================
    // VIEWS
    // =========================================

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;

    private Spinner spinnerRole;

    private Button btnRegister;
    private Button btnGoLogin;

    private ImageButton btnShowPassword;


    // =========================================
    // DATABASE
    // =========================================

    private DBHelper dbHelper;


    // =========================================
    // PASSWORD VISIBILITY
    // =========================================

    private boolean passwordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        // =========================================
        // FIND VIEWS
        // =========================================

        etName = findViewById(R.id.etName);

        etEmail = findViewById(R.id.etEmail);

        etPassword = findViewById(R.id.etPassword);

        spinnerRole = findViewById(R.id.spinnerRole);

        btnRegister = findViewById(R.id.btnRegister);

        btnGoLogin = findViewById(R.id.btnGoLogin);

        btnShowPassword = findViewById(R.id.btnShowPassword);


        // =========================================
        // DATABASE
        // =========================================

        dbHelper = new DBHelper(this);


        // =========================================
        // ROLE SPINNER
        // =========================================

        String[] roles = {
                "Student",
                "Owner"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        roles
                );


        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerRole.setAdapter(adapter);


        // =========================================
        // SHOW / HIDE PASSWORD
        // =========================================

        btnShowPassword.setOnClickListener(
                v -> togglePassword()
        );


        // =========================================
        // REGISTER
        // =========================================

        btnRegister.setOnClickListener(
                v -> registerUser()
        );


        // =========================================
        // LOGIN
        // =========================================

        btnGoLogin.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();
        });
    }


    // =========================================
    // TOGGLE PASSWORD
    // =========================================

    private void togglePassword() {

        if (passwordVisible) {

            etPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            );

            btnShowPassword.setImageResource(
                    R.drawable.ic_visibility_off
            );

            btnShowPassword.setContentDescription(
                    "Show password"
            );

            passwordVisible = false;

        } else {

            etPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );

            btnShowPassword.setImageResource(
                    R.drawable.ic_visibility
            );

            btnShowPassword.setContentDescription(
                    "Hide password"
            );

            passwordVisible = true;
        }


        etPassword.setSelection(
                etPassword.length()
        );
    }


    // =========================================
    // REGISTER USER
    // =========================================

    private void registerUser() {

        // =========================================
        // GET DATA
        // =========================================

        String name =
                etName.getText()
                        .toString()
                        .trim();


        String email =
                etEmail.getText()
                        .toString()
                        .trim();


        String password =
                etPassword.getText()
                        .toString()
                        .trim();


        String role;


        if (spinnerRole.getSelectedItem() != null) {

            role =
                    spinnerRole
                            .getSelectedItem()
                            .toString();
        } else {
            role = "Student";
        }


        // =========================================
        // NAME VALIDATION
        // =========================================

        if (name.isEmpty()) {

            etName.setError(
                    "Enter your full name"
            );

            etName.requestFocus();

            return;
        }


        // =========================================
        // EMAIL VALIDATION
        // =========================================

        if (email.isEmpty()) {

            etEmail.setError(
                    "Enter your email"
            );

            etEmail.requestFocus();

            return;
        }


        if (!android.util.Patterns
                .EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError(
                    "Enter a valid email address"
            );

            etEmail.requestFocus();

            return;
        }


        // =========================================
        // PASSWORD VALIDATION
        // =========================================

        if (password.isEmpty()) {

            etPassword.setError(
                    "Enter password"
            );

            etPassword.requestFocus();

            return;
        }


        if (password.length() < 6) {

            etPassword.setError(
                    "Password must be at least 6 characters"
            );

            etPassword.requestFocus();

            return;
        }


        // =========================================
        // CHECK EMAIL
        // =========================================

        if (dbHelper.isEmailRegistered(email)) {

            Toast.makeText(
                    this,
                    "Email already registered",
                    Toast.LENGTH_LONG
            ).show();

            etEmail.requestFocus();

            return;
        }


        btnRegister.setEnabled(false);

        SupabaseAuth.sendOTP(email, new SupabaseAuth.AuthCallback() {

            @Override
            public void onSuccess(String response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        btnRegister.setEnabled(true);

                        Toast.makeText(
                                RegisterActivity.this,
                                "OTP sent to your email",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent = new Intent(
                                RegisterActivity.this,
                                RegisterOTPActivity.class
                        );

                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        intent.putExtra("role", role);

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(String error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        btnRegister.setEnabled(true);

                        Toast.makeText(
                                RegisterActivity.this,
                                "Failed to send OTP: " + error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (btnRegister != null) {

            btnRegister.setEnabled(true);
        }
    }


    @Override
    protected void onDestroy() {

        if (dbHelper != null) {

            dbHelper.close();
        }

        super.onDestroy();
    }
}