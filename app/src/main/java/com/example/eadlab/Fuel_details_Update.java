package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Fuel_details_Update extends AppCompatActivity {

    EditText date, fuel_time, arrival_time, litres_arriving;
    Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_details_update);

        //Get the Id of elements
        date = findViewById(R.id.dateid);
        fuel_time = findViewById(R.id.fuelid);
        arrival_time = findViewById(R.id.arrivalid);
        litres_arriving = findViewById(R.id.litresid);

        update_btn = findViewById(R.id.button_update);
    }


    }
