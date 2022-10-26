package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class register extends AppCompatActivity implements View.OnClickListener{

    private Button cusReg, shedReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        cusReg = findViewById(R.id.cusRegBtn);
        shedReg = findViewById(R.id.shedRegBtn);

        cusReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, Customer_Registration.class);
                startActivity(intent);
            }
        });

        shedReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, shed_registration.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View view) {

    }
}