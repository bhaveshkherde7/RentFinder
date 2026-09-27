package com.example.rentfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);

        // Find views
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        // Create adapter
        OnboardingAdapter adapter =
                new OnboardingAdapter(this);

        viewPager.setAdapter(adapter);

        // NEXT button
        btnNext.setOnClickListener(v -> {

            int currentPage = viewPager.getCurrentItem();

            if (currentPage < 2) {

                viewPager.setCurrentItem(
                        currentPage + 1,
                        true
                );

            } else {

                openLogin();

            }
        });

        // SKIP button
        btnSkip.setOnClickListener(v -> {

            openLogin();

        });

        // Change button text according to page
        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        if (position == 2) {

                            btnNext.setText("GET STARTED");

                        } else {

                            btnNext.setText("NEXT →");

                        }
                    }
                }
        );
    }

    private void openLogin() {

        Intent intent = new Intent(
                OnboardingActivity.this,
                LoginActivity.class
        );

        startActivity(intent);

        finish();
    }
}