package com.example.rentfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterOTPActivity extends AppCompatActivity {

    // =========================================
    // VIEWS
    // =========================================

    private EditText etRegisterOtp;

    private Button btnVerifyRegisterOtp;
    private Button btnResendRegisterOtp;

    private TextView tvRegisterOtpEmail;


    // =========================================
    // REGISTER DATA
    // =========================================

    private String name;
    private String email;
    private String password;
    private String role;


    // =========================================
    // DATABASE
    // =========================================

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_otp);


        // =========================================
        // FIND VIEWS
        // =========================================

        etRegisterOtp =
                findViewById(R.id.etRegisterOtp);

        btnVerifyRegisterOtp =
                findViewById(R.id.btnVerifyRegisterOtp);

        btnResendRegisterOtp =
                findViewById(R.id.btnResendRegisterOtp);

        tvRegisterOtpEmail =
                findViewById(R.id.tvRegisterOtpEmail);


        // =========================================
        // DATABASE
        // =========================================

        dbHelper = new DBHelper(this);


        // =========================================
        // GET REGISTER DATA
        // =========================================

        name =
                getIntent().getStringExtra("name");

        email =
                getIntent().getStringExtra("email");

        password =
                getIntent().getStringExtra("password");

        role =
                getIntent().getStringExtra("role");


        // =========================================
        // VALIDATE DATA
        // =========================================

        if (name == null ||
                email == null ||
                password == null ||
                role == null) {

            Toast.makeText(
                    this,
                    "Registration information is missing",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }


        // =========================================
        // DISPLAY EMAIL
        // =========================================

        tvRegisterOtpEmail.setText(
                "OTP sent to\n" + email
        );


        // =========================================
        // VERIFY OTP
        // =========================================

        btnVerifyRegisterOtp.setOnClickListener(v -> {

            String enteredOtp =
                    etRegisterOtp
                            .getText()
                            .toString()
                            .trim();


            // =====================================
            // EMPTY OTP
            // =====================================

            if (enteredOtp.isEmpty()) {

                etRegisterOtp.setError(
                        "Enter OTP"
                );

                etRegisterOtp.requestFocus();

                return;
            }


            // =====================================
            // OTP LENGTH
            // =====================================

            if (!enteredOtp.matches("\\d{6}")) {

                etRegisterOtp.setError(
                        "Enter 6 digit OTP"
                );

                etRegisterOtp.requestFocus();

                return;
            }


            // =====================================
            // DISABLE BUTTON
            // =====================================

            btnVerifyRegisterOtp.setEnabled(false);


            // =====================================
            // VERIFY WITH SUPABASE
            // =====================================

            SupabaseAuth.verifyOTP(
                    email,
                    enteredOtp,
                    new SupabaseAuth.AuthCallback() {

                        @Override
                        public void onSuccess(
                                String response) {

                            runOnUiThread(() -> {

                                btnVerifyRegisterOtp
                                        .setEnabled(true);

                                registerUser();
                            });
                        }


                        @Override
                        public void onError(
                                String error) {

                            runOnUiThread(() -> {

                                btnVerifyRegisterOtp
                                        .setEnabled(true);

                                Toast.makeText(
                                        RegisterOTPActivity.this,
                                        "Invalid or expired OTP",
                                        Toast.LENGTH_LONG
                                ).show();

                                etRegisterOtp.setText("");
                            });
                        }
                    }
            );
        });


        // =========================================
        // RESEND OTP
        // =========================================

        btnResendRegisterOtp.setOnClickListener(v -> {

            btnResendRegisterOtp.setEnabled(false);


            SupabaseAuth.sendOTP(
                    email,
                    new SupabaseAuth.AuthCallback() {

                        @Override
                        public void onSuccess(
                                String response) {

                            runOnUiThread(() -> {

                                btnResendRegisterOtp
                                        .setEnabled(true);

                                etRegisterOtp.setText("");

                                Toast.makeText(
                                        RegisterOTPActivity.this,
                                        "New OTP sent to your email",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                        }


                        @Override
                        public void onError(
                                String error) {

                            runOnUiThread(() -> {

                                btnResendRegisterOtp
                                        .setEnabled(true);

                                Toast.makeText(
                                        RegisterOTPActivity.this,
                                        "Failed to resend OTP",
                                        Toast.LENGTH_LONG
                                ).show();
                            });
                        }
                    }
            );
        });
    }


    // =====================================================
    // REGISTER USER AFTER OTP VERIFICATION
    // =====================================================

    private void registerUser() {

        // =========================================
        // CHECK EMAIL
        // =========================================

        if (dbHelper.isEmailRegistered(email)) {

            Toast.makeText(
                    this,
                    "Email already registered",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        // =========================================
        // SAVE USER
        // =========================================

        boolean result =
                dbHelper.registerUser(
                        name,
                        email,
                        password,
                        role
                );


        // =========================================
        // SUCCESS
        // =========================================

        if (result) {

            Toast.makeText(
                    this,
                    "Account created successfully",
                    Toast.LENGTH_LONG
            ).show();


            Intent intent =
                    new Intent(
                            RegisterOTPActivity.this,
                            LoginActivity.class
                    );


            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );


            startActivity(intent);

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Registration failed",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =====================================================
    // DESTROY
    // =====================================================

    @Override
    protected void onDestroy() {

        if (dbHelper != null) {

            dbHelper.close();
        }

        super.onDestroy();
    }
}