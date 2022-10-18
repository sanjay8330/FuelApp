package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class CustomerHomepage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView txtVehNumber, txtVehType, txtFuelType;
    private Spinner spinnerLocation, spinnerShedName;
    private EditText edtTxtDate;
    private Button btnCheckFuel;

    private String selectedLocation, selectedShed;


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinner_location:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a location")){
                    //Toast.makeText(this, "Selected Location is : "+adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
                    selectedLocation = adapterView.getItemAtPosition(i).toString();
                    this.showUIElements();
                }else{
                    selectedLocation = "";
                    this.hideUIElements();
                }
                break;
            case R.id.spinner_fuelstation:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a Fuel Station")){
                    selectedShed = adapterView.getItemAtPosition(i).toString();
                }else{
                    selectedShed = "";
                }
                break;
                //Toast.makeText(this, "Selected Shed Name is"+adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkfuel:
                if(!selectedLocation.isEmpty() && !selectedShed.isEmpty() && !edtTxtDate.getText().toString().isEmpty()){
//                    Intent intent = new Intent(CustomerHomepage.this, ViewFuelDetails.class);
//                    startActivity(intent);
                    String url = "http://10.0.2.2:3001/convertToJson/getTableStructure";
                    new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            txtVehNumber.setText("Data received");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            txtVehNumber.setText("Error"+statusCode);
                        }
                    });
                }else{
                    Toast.makeText(this, "Please fill the required fields to continue!"+selectedShed, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        //Get all the components IDs
        txtVehNumber = findViewById(R.id.txt_veh_num);
        txtVehType = findViewById(R.id.txt_veh_type);
        txtFuelType = findViewById(R.id.txt_veh_fueltype);

        spinnerLocation = findViewById(R.id.spinner_location);
        spinnerShedName = findViewById(R.id.spinner_fuelstation);

        edtTxtDate = findViewById(R.id.edittxt_date);

        btnCheckFuel = findViewById(R.id.btn_checkfuel);

        edtTxtDate.setText(this.getCurrentDate());

        //Get the vehicle details based on logged in user id

        //Hide the shed name spinner, date field and button
        this.hideUIElements();

        spinnerLocation.setOnItemSelectedListener(this);
        spinnerShedName.setOnItemSelectedListener(this);

        //Button click
        btnCheckFuel.setOnClickListener(this);
    }

    public void showUIElements(){
        spinnerShedName.setVisibility(View.VISIBLE);
        edtTxtDate.setVisibility(View.VISIBLE);
        btnCheckFuel.setVisibility(View.VISIBLE);
    }

    public void hideUIElements(){
        spinnerShedName.setVisibility(View.INVISIBLE);
        edtTxtDate.setVisibility(View.INVISIBLE);
        btnCheckFuel.setVisibility(View.INVISIBLE);
    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

}