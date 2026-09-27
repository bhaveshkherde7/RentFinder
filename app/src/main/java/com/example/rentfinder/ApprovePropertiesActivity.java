
        package com.example.rentfinder;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ApprovePropertiesActivity extends AppCompatActivity {

    LinearLayout propertyContainer;
    Button btnBack;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_approve_properties);

        // =========================
        // FIND VIEWS
        // =========================

        propertyContainer =
                findViewById(R.id.propertyContainer);

        btnBack =
                findViewById(R.id.btnBack);

        // =========================
        // DATABASE
        // =========================

        dbHelper = new DBHelper(this);

        // =========================
        // LOAD PROPERTIES
        // =========================

        loadProperties();

        // =========================
        // BACK
        // =========================

        btnBack.setOnClickListener(v -> finish());
    }


    // ==========================================
    // LOAD PENDING PROPERTIES
    // ==========================================

    private void loadProperties() {

        propertyContainer.removeAllViews();

        Cursor cursor = null;

        try {

            cursor = dbHelper.getPendingProperties();

            if (cursor == null || !cursor.moveToFirst()) {

                TextView empty = new TextView(this);

                empty.setText(
                        "No properties waiting for approval."
                );

                empty.setTextSize(18);

                empty.setTextColor(
                        Color.DKGRAY
                );

                empty.setGravity(
                        Gravity.CENTER
                );

                empty.setPadding(
                        20,
                        50,
                        20,
                        50
                );

                propertyContainer.addView(empty);

                return;
            }


            // ==========================================
            // READ EACH PROPERTY
            // ==========================================

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String ownerEmail = cursor.getString(
                        cursor.getColumnIndexOrThrow("owner_email")
                );

                String propertyName = cursor.getString(
                        cursor.getColumnIndexOrThrow("property_name")
                );

                String location = cursor.getString(
                        cursor.getColumnIndexOrThrow("location")
                );

                String rent = cursor.getString(
                        cursor.getColumnIndexOrThrow("rent")
                );

                String roomType = cursor.getString(
                        cursor.getColumnIndexOrThrow("room_type")
                );

                String food = cursor.getString(
                        cursor.getColumnIndexOrThrow("food")
                );

                String wifi = cursor.getString(
                        cursor.getColumnIndexOrThrow("wifi")
                );

                String ac = cursor.getString(
                        cursor.getColumnIndexOrThrow("ac")
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description")
                );


                // ==========================================
                // PROPERTY CARD
                // ==========================================

                LinearLayout card =
                        new LinearLayout(this);

                card.setOrientation(
                        LinearLayout.VERTICAL
                );

                card.setPadding(
                        25,
                        25,
                        25,
                        25
                );

                card.setBackgroundColor(
                        Color.WHITE
                );


                // ==========================================
                // PROPERTY DETAILS
                // ==========================================

                TextView tvProperty =
                        new TextView(this);

                String propertyDetails =
                        "🏠 " + propertyName +

                                "\n\n👤 Owner: " +
                                ownerEmail +

                                "\n📍 Location: " +
                                location +

                                "\n💰 Rent: ₹" +
                                rent +

                                "\n🛏 Room Type: " +
                                roomType +

                                "\n🍽 Food: " +
                                food +

                                "\n📶 Wi-Fi: " +
                                wifi +

                                "\n❄ AC: " +
                                ac +

                                "\n\n📝 Description:\n" +
                                description +

                                "\n\nStatus: Pending";

                tvProperty.setText(
                        propertyDetails
                );

                tvProperty.setTextSize(16);

                tvProperty.setTextColor(
                        Color.rgb(33, 26, 58)
                );

                tvProperty.setPadding(
                        5,
                        5,
                        5,
                        15
                );


                // ==========================================
                // APPROVE BUTTON
                // ==========================================

                Button btnApprove =
                        new Button(this);

                btnApprove.setText(
                        "Approve Property"
                );


                btnApprove.setOnClickListener(v -> {

                    new AlertDialog.Builder(
                            ApprovePropertiesActivity.this
                    )
                            .setTitle("Approve Property")
                            .setMessage(
                                    "Approve \"" +
                                            propertyName +
                                            "\"?"
                            )
                            .setPositiveButton(
                                    "Approve",
                                    (dialog, which) -> {

                                        boolean success =
                                                dbHelper.updatePropertyStatus(
                                                        id,
                                                        "Approved"
                                                );

                                        if (success) {

                                            Toast.makeText(
                                                    this,
                                                    "Property approved successfully",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            loadProperties();

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Failed to approve property",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    "Cancel",
                                    null
                            )
                            .show();
                });


                // ==========================================
                // REJECT BUTTON
                // ==========================================

                Button btnReject =
                        new Button(this);

                btnReject.setText(
                        "Reject Property"
                );


                btnReject.setOnClickListener(v -> {

                    new AlertDialog.Builder(
                            ApprovePropertiesActivity.this
                    )
                            .setTitle("Reject Property")
                            .setMessage(
                                    "Reject \"" +
                                            propertyName +
                                            "\"?"
                            )
                            .setPositiveButton(
                                    "Reject",
                                    (dialog, which) -> {

                                        boolean success =
                                                dbHelper.updatePropertyStatus(
                                                        id,
                                                        "Rejected"
                                                );

                                        if (success) {

                                            Toast.makeText(
                                                    this,
                                                    "Property rejected",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            loadProperties();

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Failed to reject property",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    "Cancel",
                                    null
                            )
                            .show();
                });


                // ==========================================
                // ADD VIEWS TO CARD
                // ==========================================

                card.addView(
                        tvProperty
                );

                card.addView(
                        btnApprove
                );

                card.addView(
                        btnReject
                );


                // ==========================================
                // CARD MARGIN
                // ==========================================

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(
                        0,
                        0,
                        0,
                        25
                );

                card.setLayoutParams(params);


                // ==========================================
                // ADD CARD
                // ==========================================

                propertyContainer.addView(card);

            } while (cursor.moveToNext());

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }


    // ==========================================
    // REFRESH WHEN RETURNING
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();

        if (dbHelper != null &&
                propertyContainer != null) {

            loadProperties();
        }
    }
}
