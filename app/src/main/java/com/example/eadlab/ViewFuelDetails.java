package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewFuelDetails extends AppCompatActivity {

    private TextView txtView_shedLocat, txtView_shedName, txtView_arrTime, txtView_arrAmount, txtView_remAmount, txtView_vehCount;
    private Spinner spinner_fuelType;
    private ImageView imgView_fuelStatus;
    private Button btnEnterQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fuel_details);

        //Get the ID of all elements
        txtView_shedLocat = findViewById(R.id.txtview_shedLocat);
        txtView_shedName = findViewById(R.id.txtview_shedName);

        txtView_arrTime = findViewById(R.id.txtview_arrTime);
        txtView_arrAmount = findViewById(R.id.txtview_arrAmount);
        txtView_remAmount = findViewById(R.id.txtview_remAmount);
        txtView_vehCount = findViewById(R.id.txtview_vehCount);

        spinner_fuelType = findViewById(R.id.spinner_fuelType);

        imgView_fuelStatus = findViewById(R.id.imageView_fuelStatus);

        btnEnterQueue = findViewById(R.id.btn_enterQueue);

        //Get the petrol shed details based on selected shed name

        //Get the fuel and queue details for selected fuel type

        btnEnterQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFuelDetails.this, QueueDetails.class);
                startActivity(intent);
            }
        });
    }
}