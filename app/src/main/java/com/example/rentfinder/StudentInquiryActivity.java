package com.example.rentfinder;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentInquiryActivity extends AppCompatActivity {

    private EditText etQuestion;
    private Button btnSend;

    private DBHelper dbHelper;

    private int propertyId;
    private String studentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_student_inquiry
        );

        dbHelper = new DBHelper(this);

        propertyId =
                getIntent().getIntExtra(
                        "property_id",
                        -1
                );

        studentEmail =
                getIntent().getStringExtra(
                        "student_email"
                );

        if (studentEmail == null) {
            studentEmail = "";
        }

        if (studentEmail.trim().isEmpty()) {

            studentEmail =
                    getSharedPreferences(
                            "RentalFinder",
                            MODE_PRIVATE
                    ).getString(
                            "student_email",
                            ""
                    );
        }

        etQuestion =
                findViewById(
                        R.id.etQuestion
                );

        btnSend =
                findViewById(
                        R.id.btnSend
                );

        btnSend.setOnClickListener(
                v -> sendInquiry()
        );
    }

    private void sendInquiry() {

        String question =
                etQuestion
                        .getText()
                        .toString()
                        .trim();

        if (propertyId == -1) {

            Toast.makeText(
                    this,
                    "Property not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (studentEmail.isEmpty()) {

            Toast.makeText(
                    this,
                    "Student email not found. Please login again.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        if (TextUtils.isEmpty(question)) {

            etQuestion.setError(
                    "Enter your question"
            );

            etQuestion.requestFocus();

            return;
        }

        boolean result =
                dbHelper.addInquiry(
                        propertyId,
                        studentEmail,
                        question
                );

        if (result) {

            Toast.makeText(
                    this,
                    "Inquiry sent successfully!",
                    Toast.LENGTH_LONG
            ).show();

            etQuestion.setText("");

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to send inquiry",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}