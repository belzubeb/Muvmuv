package com.example.muvmuv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class custom_dialog extends AppCompatActivity {

    private boolean isAddedToWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Button addWatchlistButton = findViewById(R.id.addWatchlist);
        addWatchlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ubah drawable button sesuai dengan status isAddedToWatchlist
                if (isAddedToWatchlist) {
                    addWatchlistButton.setBackgroundResource(R.drawable.addfill);
                } else {
                    addWatchlistButton.setBackgroundResource(R.drawable.add);
                }
                // Toggle nilai isAddedToWatchlist
                isAddedToWatchlist = !isAddedToWatchlist;
            }
        });

    }
}
