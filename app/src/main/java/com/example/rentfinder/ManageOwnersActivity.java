package com.example.rentfinder;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ManageOwnersActivity extends AppCompatActivity {

    LinearLayout ownerContainer;
    Button btnBack;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_owners);

        ownerContainer = findViewById(R.id.ownerContainer);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);

        loadOwners();

        btnBack.setOnClickListener(v -> finish());
    }


    // ==========================================
    // LOAD OWNERS
    // ==========================================

    private void loadOwners() {

        ownerContainer.removeAllViews();

        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery(
                        "SELECT id, name, email FROM users " +
                                "WHERE role = ?",
                        new String[]{"Owner"}
                );


        if (cursor.getCount() == 0) {

            TextView empty = new TextView(this);

            empty.setText("No owners found.");
            empty.setTextSize(18);
            empty.setPadding(20, 30, 20, 30);

            ownerContainer.addView(empty);

            cursor.close();
            return;
        }


        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String email = cursor.getString(2);

            LinearLayout card = new LinearLayout(this);

            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(25, 20, 25, 20);


            TextView tvOwner = new TextView(this);

            tvOwner.setText(
                    "Name: " + name +
                            "\nEmail: " + email
            );

            tvOwner.setTextSize(17);


            Button btnDelete = new Button(this);

            btnDelete.setText("Delete Owner");


            btnDelete.setOnClickListener(v -> {

                new AlertDialog.Builder(this)
                        .setTitle("Delete Owner")
                        .setMessage(
                                "Are you sure you want to delete " +
                                        name + "?"
                        )
                        .setPositiveButton(
                                "Delete",
                                (dialog, which) -> {

                                    dbHelper.getWritableDatabase()
                                            .delete(
                                                    "users",
                                                    "id = ?",
                                                    new String[]{
                                                            String.valueOf(id)
                                                    }
                                            );

                                    Toast.makeText(
                                            this,
                                            "Owner deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    loadOwners();
                                }
                        )
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .show();
            });


            card.addView(tvOwner);
            card.addView(btnDelete);

            ownerContainer.addView(card);
        }

        cursor.close();
    }
}