package com.example.rentfinder;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class PropertyDetailsActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private int propertyId;

    private String studentEmail = "";

    private ImageView imgRoom1;
    private ImageView imgRoom2;
    private ImageView imgRoom3;

    private TextView tvPropertyName;
    private TextView tvOwner;
    private TextView tvLocation;
    private TextView tvRent;
    private TextView tvRoomType;
    private TextView tvFood;
    private TextView tvWifi;
    private TextView tvAC;
    private TextView tvDescription;

    private Button btnInquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_property_details
        );

        dbHelper = new DBHelper(this);

        // ---------------------------------
        // GET PROPERTY ID
        // ---------------------------------

        propertyId =
                getIntent().getIntExtra(
                        "property_id",
                        -1
                );

        // ---------------------------------
        // GET STUDENT EMAIL
        // ---------------------------------

        studentEmail =
                getIntent().getStringExtra(
                        "student_email"
                );

        if (studentEmail == null) {
            studentEmail = "";
        }

        // Get email from SharedPreferences
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

        // ---------------------------------
        // INITIALIZE VIEWS
        // ---------------------------------

        initializeViews();

        // ---------------------------------
        // CHECK PROPERTY ID
        // ---------------------------------

        if (propertyId == -1) {

            Toast.makeText(
                    this,
                    "Property ID not received",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }

        // ---------------------------------
        // LOAD PROPERTY
        // ---------------------------------

        loadPropertyDetails();

        // ---------------------------------
        // INQUIRY BUTTON
        // ---------------------------------

        btnInquiry.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            PropertyDetailsActivity.this,
                            StudentInquiryActivity.class
                    );

            intent.putExtra(
                    "property_id",
                    propertyId
            );

            intent.putExtra(
                    "student_email",
                    studentEmail
            );

            startActivity(intent);
        });
    }

    // =====================================================
    // INITIALIZE VIEWS
    // =====================================================

    private void initializeViews() {

        imgRoom1 =
                findViewById(
                        R.id.imgRoom1
                );

        imgRoom2 =
                findViewById(
                        R.id.imgRoom2
                );

        imgRoom3 =
                findViewById(
                        R.id.imgRoom3
                );

        tvPropertyName =
                findViewById(
                        R.id.tvPropertyName
                );

        tvOwner =
                findViewById(
                        R.id.tvOwner
                );

        tvLocation =
                findViewById(
                        R.id.tvLocation
                );

        tvRent =
                findViewById(
                        R.id.tvRent
                );

        tvRoomType =
                findViewById(
                        R.id.tvRoomType
                );

        tvFood =
                findViewById(
                        R.id.tvFood
                );

        tvWifi =
                findViewById(
                        R.id.tvWifi
                );

        tvAC =
                findViewById(
                        R.id.tvAC
                );

        tvDescription =
                findViewById(
                        R.id.tvDescription
                );

        btnInquiry =
                findViewById(
                        R.id.btnInquiry
                );
    }

    // =====================================================
    // LOAD PROPERTY DETAILS
    // =====================================================

    private void loadPropertyDetails() {

        Cursor cursor = null;

        try {

            cursor =
                    dbHelper.getPropertyById(
                            propertyId
                    );

            if (cursor == null) {

                Toast.makeText(
                        this,
                        "Unable to load property",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (!cursor.moveToFirst()) {

                Toast.makeText(
                        this,
                        "Property details not found",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            // ---------------------------------
            // PROPERTY NAME
            // ---------------------------------

            String propertyName =
                    getColumnValue(
                            cursor,
                            "property_name"
                    );

            tvPropertyName.setText(
                    propertyName
            );

            // ---------------------------------
            // OWNER
            // ---------------------------------

            String ownerName =
                    getColumnValue(
                            cursor,
                            "owner_name"
                    );

            String ownerEmail =
                    getColumnValue(
                            cursor,
                            "owner_email"
                    );

            if (ownerName.isEmpty()) {
                ownerName = ownerEmail;
            }

            tvOwner.setText(
                    "Owner: " + ownerName
            );

            // ---------------------------------
            // LOCATION
            // ---------------------------------

            String location =
                    getColumnValue(
                            cursor,
                            "location"
                    );

            tvLocation.setText(
                    "📍 " + location
            );

            // ---------------------------------
            // RENT
            // ---------------------------------

            String rent =
                    getColumnValue(
                            cursor,
                            "rent"
                    );

            tvRent.setText(
                    "₹ " + rent + " / Month"
            );

            // ---------------------------------
            // ROOM TYPE
            // ---------------------------------

            String roomType =
                    getColumnValue(
                            cursor,
                            "room_type"
                    );

            tvRoomType.setText(
                    "Room Type: " + roomType
            );

            // ---------------------------------
            // FOOD
            // ---------------------------------

            String food =
                    getColumnValue(
                            cursor,
                            "food"
                    );

            tvFood.setText(
                    "Food: " + food
            );

            // ---------------------------------
            // WIFI
            // ---------------------------------

            String wifi =
                    getColumnValue(
                            cursor,
                            "wifi"
                    );

            tvWifi.setText(
                    "WiFi: " + wifi
            );

            // ---------------------------------
            // AC
            // ---------------------------------

            String ac =
                    getColumnValue(
                            cursor,
                            "ac"
                    );

            tvAC.setText(
                    "AC: " + ac
            );

            // ---------------------------------
            // DESCRIPTION
            // ---------------------------------

            String description =
                    getColumnValue(
                            cursor,
                            "description"
                    );

            tvDescription.setText(
                    description
            );

            // ---------------------------------
            // PHOTO 1
            // ---------------------------------

            String photo1 =
                    getColumnValue(
                            cursor,
                            "photo1"
                    );

            loadImage(
                    imgRoom1,
                    photo1
            );

            // ---------------------------------
            // PHOTO 2
            // ---------------------------------

            String photo2 =
                    getColumnValue(
                            cursor,
                            "photo2"
                    );

            loadImage(
                    imgRoom2,
                    photo2
            );

            // ---------------------------------
            // PHOTO 3
            // ---------------------------------

            String photo3 =
                    getColumnValue(
                            cursor,
                            "photo3"
                    );

            loadImage(
                    imgRoom3,
                    photo3
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            if (cursor != null &&
                    !cursor.isClosed()) {

                cursor.close();
            }
        }
    }

    // =====================================================
    // GET DATABASE COLUMN
    // =====================================================

    private String getColumnValue(
            Cursor cursor,
            String columnName) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index == -1) {
            return "";
        }

        String value =
                cursor.getString(index);

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    // =====================================================
    // LOAD IMAGE
    // =====================================================

    private void loadImage(
            ImageView imageView,
            String imageUriString) {

        if (imageUriString == null ||
                imageUriString.trim().isEmpty()) {

            imageView.setVisibility(
                    View.GONE
            );

            return;
        }

        try {

            Uri imageUri =
                    Uri.parse(
                            imageUriString
                    );

            /*
             * First try normal URI loading.
             */
            imageView.setImageURI(
                    imageUri
            );

            imageView.setVisibility(
                    View.VISIBLE
            );

        } catch (Exception e) {

            /*
             * If URI cannot be loaded,
             * hide the image instead of crashing.
             */

            imageView.setVisibility(
                    View.GONE
            );
        }
    }
}