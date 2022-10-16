package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewFuelDetails extends AppCompatActivity {

    private Button btnEnterQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fuel_details);

        btnEnterQueue = findViewById(R.id.btn_enterQueue);

        btnEnterQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFuelDetails.this, QueueDetails.class);
                startActivity(intent);
            }
        });
    }
}