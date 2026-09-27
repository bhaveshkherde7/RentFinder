package com.example.rentfinder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyPropertiesActivity extends AppCompatActivity {

    ListView listProperties;
    TextView tvTitle;

    DBHelper dbHelper;

    ArrayList<String> propertyList;
    ArrayAdapter<String> adapter;

    String ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_properties);

        listProperties = findViewById(R.id.listProperties);
        tvTitle = findViewById(R.id.tvTitle);

        dbHelper = new DBHelper(this);

        ownerEmail =
                getIntent().getStringExtra("owner_email");

        tvTitle.setText("My Properties 🏠");

        propertyList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                propertyList
        );

        listProperties.setAdapter(adapter);

        loadProperties();
    }


    private void loadProperties() {

        propertyList.clear();

        SQLiteDatabase db =
                dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT property_name, location, rent, " +
                        "room_type, status " +
                        "FROM properties " +
                        "WHERE owner_email=?",
                new String[]{ownerEmail}
        );

        if (cursor.getCount() == 0) {

            propertyList.add(
                    "No properties added yet."
            );

        } else {

            while (cursor.moveToNext()) {

                String name =
                        cursor.getString(0);

                String location =
                        cursor.getString(1);

                String rent =
                        cursor.getString(2);

                String roomType =
                        cursor.getString(3);

                String status =
                        cursor.getString(4);

                String property =
                        "🏠 " + name +
                                "\n📍 " + location +
                                "\n💰 ₹" + rent + "/month" +
                                "\n🛏 " + roomType +
                                "\nStatus: " + status;

                propertyList.add(property);
            }
        }

        cursor.close();

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {
            loadProperties();
        }
    }
}