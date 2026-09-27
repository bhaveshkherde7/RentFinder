package com.example.rentfinder;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FindRoomsActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView tvResultTitle;
    private RecyclerView recyclerProperties;

    private DBHelper dbHelper;

    private ArrayList<Property> propertyList;
    private PropertyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_rooms);

        etSearch = findViewById(R.id.etSearch);
        tvResultTitle = findViewById(R.id.tvResultTitle);
        recyclerProperties = findViewById(R.id.recyclerProperties);

        dbHelper = new DBHelper(this);

        propertyList = new ArrayList<>();

        adapter = new PropertyAdapter(
                propertyList,
                this
        );

        recyclerProperties.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerProperties.setAdapter(adapter);

        // Load all approved properties
        loadProperties("");

        // Search by location
        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        loadProperties(
                                s.toString().trim()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );
    }

    private void loadProperties(String location) {

        propertyList.clear();

        Cursor cursor;

        if (location.isEmpty()) {

            cursor =
                    dbHelper.getApprovedPropertiesWithOwner();

        } else {

            cursor =
                    dbHelper.searchPropertiesByLocation(
                            location
                    );
        }

        if (cursor != null) {

            try {

                while (cursor.moveToNext()) {

                    int id =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "id"
                                    )
                            );

                    String ownerName =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "owner_name"
                                    )
                            );

                    String ownerEmail =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "owner_email"
                                    )
                            );

                    String propertyName =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "property_name"
                                    )
                            );

                    String locationValue =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "location"
                                    )
                            );

                    String rent =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "rent"
                                    )
                            );

                    String roomType =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "room_type"
                                    )
                            );

                    String food =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "food"
                                    )
                            );

                    String wifi =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "wifi"
                                    )
                            );

                    String ac =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "ac"
                                    )
                            );

                    String description =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "description"
                                    )
                            );

                    String photo1 =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "photo1"
                                    )
                            );

                    String photo2 =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "photo2"
                                    )
                            );

                    String photo3 =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "photo3"
                                    )
                            );

                    Property property =
                            new Property(
                                    id,
                                    ownerName,
                                    ownerEmail,
                                    propertyName,
                                    locationValue,
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

                    propertyList.add(property);
                }

            } finally {

                cursor.close();
            }
        }

        adapter.notifyDataSetChanged();

        if (propertyList.isEmpty()) {

            tvResultTitle.setText(
                    "No Properties Found"
            );

        } else {

            tvResultTitle.setText(
                    propertyList.size()
                            + " Properties Found"
            );
        }
    }
}