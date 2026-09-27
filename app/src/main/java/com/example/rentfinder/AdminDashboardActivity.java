package com.example.rentfinder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    // =========================
    // TEXT VIEWS
    // =========================

    TextView tvAdminEmail;
    TextView tvStudents;
    TextView tvOwners;


    // =========================
    // DASHBOARD LAYOUTS
    // =========================

    LinearLayout layoutStudents;
    LinearLayout layoutOwners;
    LinearLayout layoutProperties;
    LinearLayout layoutComplaints;


    // =========================
    // BUTTON
    // =========================

    Button btnLogout;


    // =========================
    // DATABASE
    // =========================

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_dashboard);


        // =========================
        // FIND VIEWS
        // =========================

        tvAdminEmail = findViewById(R.id.tvAdminEmail);

        tvStudents = findViewById(R.id.tvStudents);

        tvOwners = findViewById(R.id.tvOwners);


        layoutStudents =
                findViewById(R.id.layoutStudents);

        layoutOwners =
                findViewById(R.id.layoutOwners);

        layoutProperties =
                findViewById(R.id.layoutProperties);

        layoutComplaints =
                findViewById(R.id.layoutComplaints);


        btnLogout =
                findViewById(R.id.btnLogout);


        // =========================
        // DATABASE
        // =========================

        dbHelper = new DBHelper(this);


        // =========================
        // GET ADMIN EMAIL
        // =========================

        String adminEmail =
                getIntent().getStringExtra("email");


        if (adminEmail != null &&
                !adminEmail.isEmpty()) {

            tvAdminEmail.setText(
                    "Welcome, Admin 👋\n" +
                            adminEmail
            );

        } else {

            tvAdminEmail.setText(
                    "Welcome, Admin 👋"
            );
        }


        // =========================
        // LOAD STATISTICS
        // =========================

        loadStatistics();


        // =========================
        // MANAGE STUDENTS
        // =========================

        layoutStudents.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageStudentsActivity.class
            );

            startActivity(intent);
        });


        // =========================
        // MANAGE OWNERS
        // =========================

        layoutOwners.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageOwnersActivity.class
            );

            startActivity(intent);
        });


        // =========================
        // APPROVE PROPERTIES
        // =========================

        layoutProperties.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ApprovePropertiesActivity.class
            );

            startActivity(intent);
        });


        // =========================
        // COMPLAINTS
        // =========================

        layoutComplaints.setOnClickListener(v -> {

            Toast.makeText(
                    AdminDashboardActivity.this,
                    "Complaints",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =========================
        // LOGOUT
        // =========================

        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    LoginActivity.class
            );


            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );


            startActivity(intent);


            Toast.makeText(
                    AdminDashboardActivity.this,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }


    // ==========================================
    // LOAD STATISTICS
    // ==========================================

    private void loadStatistics() {

        SQLiteCountResult result =
                getUserCounts();


        tvStudents.setText(
                String.valueOf(result.students)
        );


        tvOwners.setText(
                String.valueOf(result.owners)
        );

    }


    // ==========================================
    // GET STUDENT / OWNER COUNT
    // ==========================================

    private SQLiteCountResult getUserCounts() {

        int students = 0;
        int owners = 0;


        Cursor cursor =
                dbHelper.getReadableDatabase()
                        .rawQuery(
                                "SELECT role, COUNT(*) " +
                                        "FROM users " +
                                        "GROUP BY role",
                                null
                        );


        while (cursor.moveToNext()) {

            String role =
                    cursor.getString(0);

            int count =
                    cursor.getInt(1);


            if (role.equalsIgnoreCase("Student")) {

                students = count;

            }

            else if (role.equalsIgnoreCase("Owner")) {

                owners = count;

            }

        }


        cursor.close();


        return new SQLiteCountResult(
                students,
                owners
        );

    }


    // ==========================================
    // RESULT CLASS
    // ==========================================

    private static class SQLiteCountResult {

        int students;
        int owners;


        SQLiteCountResult(
                int students,
                int owners) {

            this.students = students;

            this.owners = owners;
        }

    }


    // ==========================================
    // REFRESH DASHBOARD
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();

        // Refresh student and owner
        // counts when returning to dashboard

        if (dbHelper != null) {

            loadStatistics();

        }

    }

}
