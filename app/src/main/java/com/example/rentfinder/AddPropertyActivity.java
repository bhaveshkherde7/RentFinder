package com.example.rentfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddPropertyActivity extends AppCompatActivity {

    private EditText etPropertyName;
    private EditText etLocation;
    private EditText etRent;
    private EditText etDescription;

    private Spinner spinnerRoomType;
    private Spinner spinnerFood;
    private Spinner spinnerWifi;
    private Spinner spinnerAC;

    private Button btnAddRoomPhoto;
    private Button btnSaveProperty;

    private ImageView imgRoom1;
    private ImageView imgRoom2;
    private ImageView imgRoom3;

    private TextView tvPhotoStatus;

    private DBHelper dbHelper;

    private String ownerEmail = "";

    private final ArrayList<Uri> selectedPhotos =
            new ArrayList<>();

    // =====================================================
    // PHOTO PICKER
    // =====================================================

    private final ActivityResultLauncher<Intent>
            photoPickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts
                            .StartActivityForResult(),
                    result -> {

                        if (result.getResultCode()
                                != RESULT_OK) {
                            return;
                        }

                        if (result.getData() == null) {
                            return;
                        }

                        Intent data =
                                result.getData();

                        selectedPhotos.clear();

                        // Multiple images
                        if (data.getClipData() != null) {

                            int count =
                                    Math.min(
                                            data.getClipData()
                                                    .getItemCount(),
                                            3
                                    );

                            for (int i = 0;
                                 i < count;
                                 i++) {

                                Uri imageUri =
                                        data.getClipData()
                                                .getItemAt(i)
                                                .getUri();

                                takePermission(
                                        imageUri
                                );

                                selectedPhotos.add(
                                        imageUri
                                );
                            }

                        }

                        // Single image
                        else if (data.getData() != null) {

                            Uri imageUri =
                                    data.getData();

                            takePermission(
                                    imageUri
                            );

                            selectedPhotos.add(
                                    imageUri
                            );
                        }

                        displaySelectedPhotos();
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_property
        );

        dbHelper =
                new DBHelper(this);

        getOwnerEmail();

        initializeViews();

        setupSpinners();

        btnAddRoomPhoto.setOnClickListener(
                v -> openPhotoPicker()
        );

        btnSaveProperty.setOnClickListener(
                v -> saveProperty()
        );
    }

    // =====================================================
    // INITIALIZE VIEWS
    // =====================================================

    private void initializeViews() {

        etPropertyName =
                findViewById(
                        R.id.etPropertyName
                );

        etLocation =
                findViewById(
                        R.id.etLocation
                );

        etRent =
                findViewById(
                        R.id.etRent
                );

        etDescription =
                findViewById(
                        R.id.etDescription
                );

        spinnerRoomType =
                findViewById(
                        R.id.spinnerRoomType
                );

        spinnerFood =
                findViewById(
                        R.id.spinnerFood
                );

        spinnerWifi =
                findViewById(
                        R.id.spinnerWifi
                );

        spinnerAC =
                findViewById(
                        R.id.spinnerAC
                );

        btnAddRoomPhoto =
                findViewById(
                        R.id.btnAddRoomPhoto
                );

        btnSaveProperty =
                findViewById(
                        R.id.btnSaveProperty
                );

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

        tvPhotoStatus =
                findViewById(
                        R.id.tvPhotoStatus
                );
    }

    // =====================================================
    // SETUP SPINNERS
    // =====================================================

    private void setupSpinners() {

        String[] roomTypes = {
                "Select Room Type",
                "Single Room",
                "Double Room",
                "1 BHK",
                "2 BHK",
                "PG",
                "Hostel"
        };

        ArrayAdapter<String> roomAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_item,
                        roomTypes
                );

        roomAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinnerRoomType.setAdapter(
                roomAdapter
        );

        String[] foodOptions = {
                "Select Food Option",
                "Available",
                "Not Available"
        };

        ArrayAdapter<String> foodAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_item,
                        foodOptions
                );

        foodAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinnerFood.setAdapter(
                foodAdapter
        );

        String[] wifiOptions = {
                "Select WiFi",
                "Available",
                "Not Available"
        };

        ArrayAdapter<String> wifiAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_item,
                        wifiOptions
                );

        wifiAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinnerWifi.setAdapter(
                wifiAdapter
        );

        String[] acOptions = {
                "Select AC",
                "Available",
                "Not Available"
        };

        ArrayAdapter<String> acAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_item,
                        acOptions
                );

        acAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinnerAC.setAdapter(
                acAdapter
        );
    }

    // =====================================================
    // GET OWNER EMAIL
    // =====================================================

    private void getOwnerEmail() {

        ownerEmail =
                getIntent().getStringExtra(
                        "owner_email"
                );

        if (ownerEmail == null ||
                ownerEmail.trim().isEmpty()) {

            ownerEmail =
                    getIntent().getStringExtra(
                            "email"
                    );
        }

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

        if (ownerEmail == null) {
            ownerEmail = "";
        }

        ownerEmail =
                ownerEmail.trim();

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
    }

    // =====================================================
    // OPEN PHOTO PICKER
    // =====================================================

    private void openPhotoPicker() {

        Intent intent =
                new Intent(
                        Intent.ACTION_OPEN_DOCUMENT
                );

        intent.setType(
                "image/*"
        );

        intent.putExtra(
                Intent.EXTRA_ALLOW_MULTIPLE,
                true
        );

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        /*
         * Important for persistent access
         * to selected images.
         */
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );

        photoPickerLauncher.launch(
                intent
        );
    }

    // =====================================================
    // TAKE URI PERMISSION
    // =====================================================

    private void takePermission(Uri uri) {

        try {

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

        } catch (Exception ignored) {

            // Some providers do not support
            // persistable permissions.
        }
    }

    // =====================================================
    // DISPLAY SELECTED PHOTOS
    // =====================================================

    private void displaySelectedPhotos() {

        imgRoom1.setVisibility(
                ImageView.GONE
        );

        imgRoom2.setVisibility(
                ImageView.GONE
        );

        imgRoom3.setVisibility(
                ImageView.GONE
        );

        if (selectedPhotos.size() >= 1) {

            imgRoom1.setImageURI(
                    selectedPhotos.get(0)
            );

            imgRoom1.setVisibility(
                    ImageView.VISIBLE
            );
        }

        if (selectedPhotos.size() >= 2) {

            imgRoom2.setImageURI(
                    selectedPhotos.get(1)
            );

            imgRoom2.setVisibility(
                    ImageView.VISIBLE
            );
        }

        if (selectedPhotos.size() >= 3) {

            imgRoom3.setImageURI(
                    selectedPhotos.get(2)
            );

            imgRoom3.setVisibility(
                    ImageView.VISIBLE
            );
        }

        if (selectedPhotos.isEmpty()) {

            tvPhotoStatus.setText(
                    "No photos selected"
            );

        } else {

            tvPhotoStatus.setText(
                    selectedPhotos.size()
                            + " photo(s) selected"
            );
        }
    }

    // =====================================================
    // SAVE PROPERTY
    // =====================================================

    private void saveProperty() {

        String propertyName =
                etPropertyName
                        .getText()
                        .toString()
                        .trim();

        String location =
                etLocation
                        .getText()
                        .toString()
                        .trim();

        String rent =
                etRent
                        .getText()
                        .toString()
                        .trim();

        String roomType =
                spinnerRoomType
                        .getSelectedItem()
                        .toString();

        String food =
                spinnerFood
                        .getSelectedItem()
                        .toString();

        String wifi =
                spinnerWifi
                        .getSelectedItem()
                        .toString();

        String ac =
                spinnerAC
                        .getSelectedItem()
                        .toString();

        String description =
                etDescription
                        .getText()
                        .toString()
                        .trim();

        // ---------------------------------
        // VALIDATION
        // ---------------------------------

        if (ownerEmail.isEmpty()) {

            Toast.makeText(
                    this,
                    "Owner email not found. Please login again.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        if (TextUtils.isEmpty(propertyName)) {

            etPropertyName.setError(
                    "Enter property name"
            );

            etPropertyName.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(location)) {

            etLocation.setError(
                    "Enter location"
            );

            etLocation.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(rent)) {

            etRent.setError(
                    "Enter monthly rent"
            );

            etRent.requestFocus();

            return;
        }

        if (roomType.equals(
                "Select Room Type")) {

            Toast.makeText(
                    this,
                    "Please select room type",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (food.equals(
                "Select Food Option")) {

            Toast.makeText(
                    this,
                    "Please select food option",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (wifi.equals(
                "Select WiFi")) {

            Toast.makeText(
                    this,
                    "Please select WiFi option",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (ac.equals(
                "Select AC")) {

            Toast.makeText(
                    this,
                    "Please select AC option",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (TextUtils.isEmpty(
                description)) {

            etDescription.setError(
                    "Enter property description"
            );

            etDescription.requestFocus();

            return;
        }

        // ---------------------------------
        // GET PHOTO URI
        // ---------------------------------

        String photo1 = "";
        String photo2 = "";
        String photo3 = "";

        if (selectedPhotos.size() >= 1) {

            photo1 =
                    selectedPhotos
                            .get(0)
                            .toString();
        }

        if (selectedPhotos.size() >= 2) {

            photo2 =
                    selectedPhotos
                            .get(1)
                            .toString();
        }

        if (selectedPhotos.size() >= 3) {

            photo3 =
                    selectedPhotos
                            .get(2)
                            .toString();
        }

        // ---------------------------------
        // SAVE TO DATABASE
        // ---------------------------------

        boolean inserted =
                dbHelper.addProperty(
                        ownerEmail,
                        propertyName,
                        location,
                        rent,
                        roomType,
                        food,
                        wifi,
                        ac,
                        description,
                        photo1,
                        photo2,
                        photo3
                );

        if (inserted) {

            Toast.makeText(
                    this,
                    "Property submitted successfully!\nWaiting for admin approval.",
                    Toast.LENGTH_LONG
            ).show();

            clearForm();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to add property",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

        etPropertyName.setText("");

        etLocation.setText("");

        etRent.setText("");

        etDescription.setText("");

        spinnerRoomType.setSelection(0);

        spinnerFood.setSelection(0);

        spinnerWifi.setSelection(0);

        spinnerAC.setSelection(0);

        selectedPhotos.clear();

        imgRoom1.setVisibility(
                ImageView.GONE
        );

        imgRoom2.setVisibility(
                ImageView.GONE
        );

        imgRoom3.setVisibility(
                ImageView.GONE
        );

        tvPhotoStatus.setText(
                "No photos selected"
        );
    }
}