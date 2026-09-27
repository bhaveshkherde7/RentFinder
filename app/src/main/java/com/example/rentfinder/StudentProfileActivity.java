package com.example.rentfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_student_profile
        );

        tvName =
                findViewById(R.id.tvProfileName);

        tvEmail =
                findViewById(R.id.tvProfileEmail);

        SharedPreferences preferences =
                getSharedPreferences(
                        "RentalFinder",
                        MODE_PRIVATE
                );

        String name =
                getIntent().getStringExtra(
                        "student_name"
                );

        String email =
                getIntent().getStringExtra(
                        "student_email"
                );

        if (name == null || name.isEmpty()) {

            name = preferences.getString(
                    "student_name",
                    "Student"
            );
        }

        if (email == null || email.isEmpty()) {

            email = preferences.getString(
                    "student_email",
                    ""
            );
        }

        tvName.setText(name);
        tvEmail.setText(email);
    }
}
