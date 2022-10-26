package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class Shed_fuel_registration extends AppCompatActivity {

    private EditText petrolNormal, petrolSuper, dieselNormal, dieselSuper;
    private CheckBox checkPetrolNormal, checkPetrolSuper, checkDieselNormal, checkDieselSuper;
    private Spinner obtained;
    private Button regisBtn;

    String selecetdObtained;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_fuel_registration2);

    }
}