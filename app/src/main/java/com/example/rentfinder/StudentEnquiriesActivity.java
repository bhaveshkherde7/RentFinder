package com.example.rentfinder;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentEnquiriesActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private LinearLayout enquiryContainer;
    private TextView tvEmpty;

    private String studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_enquiries);

        dbHelper = new DBHelper(this);

        studentEmail = getIntent().getStringExtra(
                "student_email"
        );

        if (studentEmail == null) {
            studentEmail = "";
        }

        enquiryContainer =
                findViewById(R.id.enquiryContainer);

        tvEmpty =
                findViewById(R.id.tvEmpty);

        loadEnquiries();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {
            loadEnquiries();
        }
    }

    private void loadEnquiries() {

        enquiryContainer.removeAllViews();

        Cursor cursor =
                dbHelper.getStudentInquiries(studentEmail);

        if (cursor == null || cursor.getCount() == 0) {

            tvEmpty.setVisibility(View.VISIBLE);

            if (cursor != null) {
                cursor.close();
            }

            return;
        }

        tvEmpty.setVisibility(View.GONE);

        int propertyIdIndex =
                cursor.getColumnIndex("property_id");

        int questionIndex =
                cursor.getColumnIndex("question");

        int statusIndex =
                cursor.getColumnIndex("status");

        int idIndex =
                cursor.getColumnIndex("id");

        while (cursor.moveToNext()) {

            int propertyId =
                    cursor.getInt(propertyIdIndex);

            String question =
                    cursor.getString(questionIndex);

            String status =
                    cursor.getString(statusIndex);

            int enquiryId =
                    cursor.getInt(idIndex);

            addEnquiryCard(
                    enquiryId,
                    propertyId,
                    question,
                    status
            );
        }

        cursor.close();
    }

    private void addEnquiryCard(
            int enquiryId,
            int propertyId,
            String question,
            String status) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                20,
                20,
                20
        );

        card.setBackgroundResource(
                R.drawable.student_menu_background
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                15
        );

        card.setLayoutParams(params);

        TextView property =
                new TextView(this);

        property.setText(
                "🏠 Property ID: " + propertyId
        );

        property.setTextSize(17);
        property.setTextColor(
                getResources().getColor(
                        android.R.color.black
                )
        );

        property.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        TextView questionView =
                new TextView(this);

        questionView.setText(
                "💬 " + question
        );

        questionView.setTextSize(15);
        questionView.setPadding(
                0,
                12,
                0,
                12
        );

        TextView statusView =
                new TextView(this);

        statusView.setText(
                "Status: " + status
        );

        statusView.setTextSize(14);

        card.addView(property);
        card.addView(questionView);
        card.addView(statusView);

        enquiryContainer.addView(card);
    }
}
