package com.example.rentfinder;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OwnerRequestsActivity extends AppCompatActivity {

    TextView tvTitle;
    ListView listRequests;

    ArrayList<String> requestList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_owner_requests);

        tvTitle = findViewById(R.id.tvTitle);
        listRequests = findViewById(R.id.listRequests);

        tvTitle.setText("Student Enquiries 💬");

        requestList = new ArrayList<>();

        // Temporary sample requests
        requestList.add(
                "👨‍🎓 Rahul Sharma\n" +
                        "Interested in: Good Vibes PG\n" +
                        "Message: Is a 2-sharing room available?\n" +
                        "Status: Pending"
        );

        requestList.add(
                "👩‍🎓 Priya Patil\n" +
                        "Interested in: Student Residency\n" +
                        "Message: Can I visit the property tomorrow?\n" +
                        "Status: Pending"
        );

        requestList.add(
                "👨‍🎓 Amit Kumar\n" +
                        "Interested in: Green View Hostel\n" +
                        "Message: Is food included in the rent?\n" +
                        "Status: Responded"
        );


        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                requestList
        );

        listRequests.setAdapter(adapter);
    }
}