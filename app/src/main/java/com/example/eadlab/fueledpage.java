package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class fueledpage extends AppCompatActivity implements View.OnClickListener {

    TextView txtView_shedName, txtView_vehicleType, txtView_fuelType, txtView_fueledAmount, txtView_fueledCost;
    Button btn_logout, btn_returnHome;

    String shedName, vehType, fuelType, fuelAmount, fuelCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fueledpage);

        //Get the Id of elements
        txtView_shedName = findViewById(R.id.txtview_fueledShed);
        txtView_vehicleType = findViewById(R.id.txtview_fueledVehType);
        txtView_fuelType = findViewById(R.id.txtview_fueledFuelType);
        txtView_fueledAmount = findViewById(R.id.txtview_fueledAmount);
        txtView_fueledCost = findViewById(R.id.txtview_fueledCost);

        btn_logout = findViewById(R.id.btn_logout);
        btn_returnHome = findViewById(R.id.btn_home);

        //Get the parameters from last activity using intent
        Intent intent = getIntent();
        shedName = intent.getStringExtra("shedName");
        vehType = intent.getStringExtra("vehicleType");
        fuelType = intent.getStringExtra("fuelType");
        fuelAmount = intent.getStringExtra("fueledAmount");
        fuelCost = intent.getStringExtra("fuelAmount");

        setFueledDetails();

        btn_returnHome.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    public void setFueledDetails(){
        txtView_shedName.setText(shedName);
        txtView_vehicleType.setText(vehType);
        txtView_fuelType.setText(fuelType);
        txtView_fueledAmount.setText(fuelAmount);
        txtView_fueledCost.setText(fuelCost);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_home:
                Intent intent = new Intent(fueledpage.this, CustomerHomepage.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}