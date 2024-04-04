package com.example.muvmuv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnBoarding1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding1);
    }

    public void onNextButtonClick(View view) {
        Intent intent = new Intent(this, OnBoard2.class);
        startActivity(intent);
    }
}