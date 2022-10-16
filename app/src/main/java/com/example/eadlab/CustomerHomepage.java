package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerHomepage extends AppCompatActivity {

    private Button btnCheckFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        btnCheckFuel = findViewById(R.id.btn_checkfuel);

        btnCheckFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomepage.this, ViewFuelDetails.class);
                startActivity(intent);
            }
        });
    }
}