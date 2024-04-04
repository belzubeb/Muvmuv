package com.example.muvmuv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnBoard2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board2);
    }

    public void onNextButtonClick(View view) {
        Intent intent = new Intent(this, OnBoard3.class);
        startActivity(intent);
    }

    public void onBacktButtonClick(View view) {
        Intent intent = new Intent(this, OnBoarding1.class);
        startActivity(intent);
    }
}