package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFuelDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView txtView_shedLocat, txtView_shedName, txtView_arrTime, txtView_arrAmount, txtView_remAmount, txtView_vehCount;
    private Spinner spinner_fuelType;
    private ImageView imgView_fuelStatus, imgView_fuelQueue;
    private Button btnEnterQueue;
    private TextView label_arrTime, label_arrLitres, label_remLitres, label_vehCount;

    String selectFuelType;

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

        //Default labels & images
        label_arrTime = findViewById(R.id.label_fueldetail1);
        label_arrLitres = findViewById(R.id.label_fueldetail2);
        label_remLitres = findViewById(R.id.label_fueldetail3);
        label_vehCount = findViewById(R.id.label_currentVehCount);
        imgView_fuelQueue = findViewById(R.id.imageView_fuelQueue);

        this.hideUIElements();

        spinner_fuelType.setOnItemSelectedListener(this);

        //Get the petrol shed details based on selected shed name

        //Get the fuel and queue details for selected fuel type

        btnEnterQueue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_enterQueue:
                Toast.makeText(this, "Entered to Queue", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewFuelDetails.this, QueueDetails.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinner_fuelType:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a Fuel Type")){
                    selectFuelType = adapterView.getItemAtPosition(i).toString();
                    this.showUIElements();
                }else{
                    selectFuelType = "";
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void hideUIElements(){
        txtView_arrTime.setVisibility(View.INVISIBLE);
        txtView_arrAmount.setVisibility(View.INVISIBLE);
        txtView_remAmount.setVisibility(View.INVISIBLE);
        txtView_vehCount.setVisibility(View.INVISIBLE);

        label_arrTime.setVisibility(View.INVISIBLE);
        label_arrLitres.setVisibility(View.INVISIBLE);
        label_remLitres.setVisibility(View.INVISIBLE);
        label_vehCount.setVisibility(View.INVISIBLE);
        imgView_fuelQueue.setVisibility(View.INVISIBLE);

        imgView_fuelStatus.setVisibility(View.INVISIBLE);
        imgView_fuelQueue.setVisibility(View.INVISIBLE);

        btnEnterQueue.setVisibility(View.INVISIBLE);
    }

    public void showUIElements(){
        txtView_arrTime.setVisibility(View.VISIBLE);
        txtView_arrAmount.setVisibility(View.VISIBLE);
        txtView_remAmount.setVisibility(View.VISIBLE);
        txtView_vehCount.setVisibility(View.VISIBLE);

        label_arrTime.setVisibility(View.VISIBLE);
        label_arrLitres.setVisibility(View.VISIBLE);
        label_remLitres.setVisibility(View.VISIBLE);
        label_vehCount.setVisibility(View.VISIBLE);
        imgView_fuelQueue.setVisibility(View.VISIBLE);

        imgView_fuelStatus.setVisibility(View.VISIBLE);
        imgView_fuelQueue.setVisibility(View.VISIBLE);

        btnEnterQueue.setVisibility(View.VISIBLE);
    }
}