package com.example.rentfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StudentHomeActivity extends AppCompatActivity {

    private TextView tvHello;
    private TextView tvAvailable;

    private Button btnFindRooms;
    private Button btnSaved;
    private Button btnEnquiries;
    private Button btnVisits;
    private Button btnProfile;
    private Button btnLogout;

    private String studentEmail;
    private String studentName;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        // =====================================================
        // SHARED PREFERENCES
        // =====================================================

        preferences = getSharedPreferences(
                "RentalFinder",
                MODE_PRIVATE
        );

        // =====================================================
        // GET STUDENT DETAILS
        // =====================================================

        studentEmail = getIntent().getStringExtra("student_email");
        studentName = getIntent().getStringExtra("student_name");

        if (studentEmail == null || studentEmail.isEmpty()) {
            studentEmail = preferences.getString(
                    "student_email",
                    ""
            );
        }

        if (studentName == null || studentName.isEmpty()) {
            studentName = preferences.getString(
                    "student_name",
                    "Student"
            );
        }

        // Save details
        preferences.edit()
                .putString("student_email", studentEmail)
                .putString("student_name", studentName)
                .apply();

        // =====================================================
        // FIND VIEWS
        // =====================================================

        tvHello = findViewById(R.id.tvHello);
        tvAvailable = findViewById(R.id.tvAvailable);

        btnFindRooms = findViewById(R.id.btnFindRooms);
        btnSaved = findViewById(R.id.btnSaved);
        btnEnquiries = findViewById(R.id.btnEnquiries);
        btnVisits = findViewById(R.id.btnVisits);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // =====================================================
        // HEADER
        // =====================================================

        tvHello.setText(
                "Hello, " + studentName + " 👋"
        );

        tvAvailable.setText(
                "Find rooms and PGs near you"
        );

        // =====================================================
        // FIND ROOMS
        // =====================================================

        btnFindRooms.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentHomeActivity.this,
                    FindRoomsActivity.class
            );

            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // SAVED PROPERTIES
        // =====================================================

        btnSaved.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentHomeActivity.this,
                    SavedPropertiesActivity.class
            );

            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // MY ENQUIRIES
        // =====================================================

        btnEnquiries.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentHomeActivity.this,
                    StudentEnquiriesActivity.class
            );

            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // VISIT REQUESTS
        // =====================================================

        btnVisits.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentHomeActivity.this,
                    StudentVisitsActivity.class
            );

            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            startActivity(intent);
        });

        // =====================================================
        // PROFILE
        // =====================================================

        btnProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentHomeActivity.this,
                    StudentProfileActivity.class
            );

            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            intent.putExtra(
                    "student_name",
                    studentName
            );

            startActivity(intent);
        });

        // =====================================================
        // LOGOUT
        // =====================================================

        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    // =========================================================
    // LOGOUT DIALOG
    // =========================================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton(
                        "CANCEL",
                        null
                )
                .setPositiveButton(
                        "LOGOUT",
                        (dialog, which) -> logout()
                )
                .show();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        preferences.edit()
                .remove("student_email")
                .remove("student_name")
                .apply();

        Toast.makeText(
                this,
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                StudentHomeActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}
