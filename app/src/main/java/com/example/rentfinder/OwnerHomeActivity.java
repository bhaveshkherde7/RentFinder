package com.example.rentfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerHomeActivity extends AppCompatActivity {

    // =========================================================
    // VIEWS
    // =========================================================

    TextView tvHello;
    TextView tvProperties;
    TextView tvEnquiries;
    TextView tvVisits;

    Button btnAddProperty;
    Button btnLogout;

    LinearLayout layoutProperties;
    LinearLayout layoutEnquiries;
    LinearLayout layoutVisits;
    LinearLayout layoutReviews;

    // =========================================================
    // OWNER EMAIL
    // =========================================================

    private String ownerEmail;

    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_owner_home
        );

        // =====================================================
        // FIND VIEWS
        // =====================================================

        tvHello =
                findViewById(R.id.tvHello);

        tvProperties =
                findViewById(R.id.tvProperties);

        tvEnquiries =
                findViewById(R.id.tvEnquiries);

        tvVisits =
                findViewById(R.id.tvVisits);

        btnAddProperty =
                findViewById(R.id.btnAddProperty);

        btnLogout =
                findViewById(R.id.btnLogout);

        layoutProperties =
                findViewById(R.id.layoutProperties);

        layoutEnquiries =
                findViewById(R.id.layoutEnquiries);

        layoutVisits =
                findViewById(R.id.layoutVisits);

        layoutReviews =
                findViewById(R.id.layoutReviews);

        // =====================================================
        // GET OWNER EMAIL
        // =====================================================

        getOwnerEmail();

        // =====================================================
        // SHOW OWNER DETAILS
        // =====================================================

        if (!ownerEmail.isEmpty()) {

            tvHello.setText(
                    "Hello, Owner 👋\n" + ownerEmail
            );

        } else {

            tvHello.setText(
                    "Hello, Owner 👋"
            );
        }

        // =====================================================
        // SAVE EMAIL IN SHARED PREFERENCES
        // =====================================================

        if (!ownerEmail.isEmpty()) {

            getSharedPreferences(
                    "RentalFinder",
                    MODE_PRIVATE
            )
                    .edit()
                    .putString(
                            "owner_email",
                            ownerEmail
                    )
                    .apply();
        }

        // =====================================================
        // DASHBOARD DATA
        // =====================================================

        // Temporary values
        // These can later be loaded from SQLite.

        tvProperties.setText("3");

        tvEnquiries.setText("12");

        tvVisits.setText("5");

        // =====================================================
        // ADD NEW PROPERTY
        // =====================================================

        btnAddProperty.setOnClickListener(v -> {

            if (ownerEmail.isEmpty()) {

                Toast.makeText(
                        OwnerHomeActivity.this,
                        "Owner email not found. Please login again.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            OwnerHomeActivity.this,
                            AddPropertyActivity.class
                    );

            // Send using BOTH names
            // This makes it compatible with AddPropertyActivity.

            intent.putExtra(
                    "email",
                    ownerEmail
            );

            intent.putExtra(
                    "owner_email",
                    ownerEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // MY PROPERTIES
        // =====================================================

        layoutProperties.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            OwnerHomeActivity.this,
                            MyPropertiesActivity.class
                    );

            intent.putExtra(
                    "owner_email",
                    ownerEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // STUDENT ENQUIRIES
        // =====================================================

        layoutEnquiries.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            OwnerHomeActivity.this,
                            OwnerRequestsActivity.class
                    );

            intent.putExtra(
                    "owner_email",
                    ownerEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // VISIT REQUESTS
        // =====================================================

        layoutVisits.setOnClickListener(v -> {

            Toast.makeText(
                    OwnerHomeActivity.this,
                    "Visit Requests",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =====================================================
        // REVIEWS
        // =====================================================

        layoutReviews.setOnClickListener(v -> {

            Toast.makeText(
                    OwnerHomeActivity.this,
                    "Reviews & Ratings",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =====================================================
        // LOGOUT
        // =====================================================

        btnLogout.setOnClickListener(v -> {

            // Remove owner login information
            getSharedPreferences(
                    "RentalFinder",
                    MODE_PRIVATE
            )
                    .edit()
                    .remove("owner_email")
                    .apply();

            // Go to Login
            Intent intent =
                    new Intent(
                            OwnerHomeActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            Toast.makeText(
                    OwnerHomeActivity.this,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }

    // =========================================================
    // GET OWNER EMAIL
    // =========================================================

    private void getOwnerEmail() {

        // -----------------------------------------------------
        // First: get from Intent using "owner_email"
        // -----------------------------------------------------

        ownerEmail =
                getIntent().getStringExtra(
                        "owner_email"
                );

        // -----------------------------------------------------
        // Second: try "email"
        // -----------------------------------------------------

        if (ownerEmail == null ||
                ownerEmail.trim().isEmpty()) {

            ownerEmail =
                    getIntent().getStringExtra(
                            "email"
                    );
        }

        // -----------------------------------------------------
        // Third: SharedPreferences
        // -----------------------------------------------------

        if (ownerEmail == null ||
                ownerEmail.trim().isEmpty()) {

            SharedPreferences preferences =
                    getSharedPreferences(
                            "RentalFinder",
                            MODE_PRIVATE
                    );

            ownerEmail =
                    preferences.getString(
                            "owner_email",
                            ""
                    );
        }

        // -----------------------------------------------------
        // Final safety
        // -----------------------------------------------------

        if (ownerEmail == null) {
            ownerEmail = "";
        }

        ownerEmail =
                ownerEmail.trim();
    }

    // =========================================================
    // REFRESH EMAIL WHEN RETURNING TO SCREEN
    // =========================================================

    @Override
    protected void onResume() {
        super.onResume();

        if (ownerEmail == null ||
                ownerEmail.isEmpty()) {

            getOwnerEmail();
        }
    }
}
