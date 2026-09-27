package com.example.rentfinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Spinner spinnerRole;
    Button btnLogin, btnRegister;
    ImageButton btnShowPassword;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnShowPassword = findViewById(R.id.btnShowPassword);

        dbHelper = new DBHelper(this);

        // SHOW / HIDE PASSWORD
        btnShowPassword.setOnClickListener(v -> {

            if (etPassword.getInputType()
                    == (InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {

                // Show password
                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                btnShowPassword.setImageResource(
                        R.drawable.ic_visibility
                );

                btnShowPassword.setContentDescription(
                        "Hide password"
                );

            } else {

                // Hide password
                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                btnShowPassword.setImageResource(
                        R.drawable.ic_visibility_off
                );

                btnShowPassword.setContentDescription(
                        "Show password"
                );
            }

            // Keep cursor at the end
            etPassword.setSelection(etPassword.length());
        });

        // Role Spinner
        String[] roles = {"Student", "Owner", "Admin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                roles
        );

        spinnerRole.setAdapter(adapter);

        // LOGIN
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                        LoginActivity.this,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // ADMIN LOGIN
            if (role.equals("Admin")) {

                if (email.equals("admin@gmail.com")
                        && password.equals("admin123")) {

                    Toast.makeText(
                            LoginActivity.this,
                            "Admin Login Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            LoginActivity.this,
                            AdminDashboardActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            LoginActivity.this,
                            "Invalid Admin Login",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return;
            }

            // STUDENT / OWNER LOGIN
            boolean result = dbHelper.checkLogin(
                    email,
                    password,
                    role
            );

            if (result) {

                Toast.makeText(
                        LoginActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                ).show();

                if (role.equals("Student")) {

                    Intent intent = new Intent(
                            LoginActivity.this,
                            StudentHomeActivity.class
                    );

                    intent.putExtra("email", email);
                    startActivity(intent);

                } else if (role.equals("Owner")) {

                    Intent intent = new Intent(
                            LoginActivity.this,
                            OwnerHomeActivity.class
                    );

                    intent.putExtra("email", email);
                    startActivity(intent);
                }

                finish();

            } else {

                Toast.makeText(
                        LoginActivity.this,
                        "Invalid email, password or role",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // REGISTER
        btnRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }
}
