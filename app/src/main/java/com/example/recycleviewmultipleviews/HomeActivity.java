package com.example.recycleviewmultipleviews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.LandingActivity;
import com.example.recycleviewmultipleviews.databinding.ActivityStartBinding;
import com.example.tmdb.TmdbMainActivity;

public class HomeActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        final Button btnAssignment1 = findViewById(R.id.btnAssignment1);
        btnAssignment1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        final Button btnAssignment2 = findViewById(R.id.btnAssignment2);
        btnAssignment2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(intent);
            }
        });

        final Button btnAssignment3 = findViewById(R.id.btnAssignment3);
        btnAssignment3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TmdbMainActivity.class);
                startActivity(intent);
            }
        });


    }
}