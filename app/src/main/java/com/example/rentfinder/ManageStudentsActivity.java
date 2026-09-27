package com.example.rentfinder;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ManageStudentsActivity extends AppCompatActivity {

    LinearLayout studentContainer;
    Button btnBack;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        studentContainer = findViewById(R.id.studentContainer);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);

        loadStudents();

        btnBack.setOnClickListener(v -> finish());
    }


    // ==========================================
    // LOAD STUDENTS
    // ==========================================

    private void loadStudents() {

        studentContainer.removeAllViews();

        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery(
                        "SELECT id, name, email FROM users " +
                                "WHERE role = ?",
                        new String[]{"Student"}
                );

        if (cursor.getCount() == 0) {

            TextView empty = new TextView(this);

            empty.setText("No students found.");
            empty.setTextSize(18);
            empty.setPadding(20, 30, 20, 30);

            studentContainer.addView(empty);

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

            TextView tvStudent = new TextView(this);

            tvStudent.setText(
                    "Name: " + name +
                            "\nEmail: " + email
            );

            tvStudent.setTextSize(17);


            Button btnDelete = new Button(this);

            btnDelete.setText("Delete Student");


            btnDelete.setOnClickListener(v -> {

                new AlertDialog.Builder(this)
                        .setTitle("Delete Student")
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
                                            "Student deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    loadStudents();
                                }
                        )
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .show();
            });


            card.addView(tvStudent);
            card.addView(btnDelete);

            studentContainer.addView(card);
        }

        cursor.close();
    }
}