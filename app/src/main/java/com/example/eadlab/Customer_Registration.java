package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;

import java.util.HashMap;
import java.util.Map;

public class Customer_Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText usernameTxt, passwordTxt, vehicleNumTxt, contactTxt, confirmPasswordTxt;
    private Spinner vehicleType, fuelType;
    private Button registerBtn;

    String selectedVehicleType, selectedFuelType;

    String addUserAPI = EndpointURL.ADD_USER;
    String addCustomerAPI = EndpointURL.ADD_CUSTOMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration2);

        usernameTxt = findViewById(R.id.shedUsernameInput);
        passwordTxt = findViewById(R.id.shedPasswordInput);
        vehicleNumTxt = findViewById(R.id.locationInput);
        contactTxt = findViewById(R.id.loginPasswordInput);
        confirmPasswordTxt = findViewById(R.id.shedPasswordConfirmInput);

        vehicleType = findViewById(R.id.vehicleTypeInput);
        fuelType = findViewById(R.id.fuelTypeInput);

        registerBtn = findViewById(R.id.regBtn);

        vehicleType.setOnItemSelectedListener(this);
        fuelType.setOnItemSelectedListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.vehicleTypeInput:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a Vehicle Type")){
                    selectedVehicleType = adapterView.getItemAtPosition(i).toString();
                    Log.e(TAG, "onItemSelected: " + adapterView.getItemAtPosition(i).toString() );
                }
                break;

            case R.id.fuelTypeInput:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a Fuel Type")){
                    selectedFuelType = adapterView.getItemAtPosition(i).toString();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private static final String TAG = "CUSTOMER_REGISTRATION_LOG";

    public void addCustomer(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String addAPI = addCustomerAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(Customer_Registration.this, "Customer details added!", Toast.LENGTH_LONG).show();
                        addUser();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update user fuel quota details: "+error.getLocalizedMessage());
            }
        }) {

            @Override
            public byte[] getBody() {

                String body="{\"name\":" + "\"" + usernameTxt.getText().toString() + "\"," +
                        "\"vehicleType\":" + "\"" + selectedVehicleType + "\"," +
                        "\"vehicleNumber\":" + "\"" + vehicleNumTxt.getText().toString() + "\"," +
                        "\"fuelType\":" + "\"" + selectedFuelType + "\"," +
                        //"\"remainFuelQuota\":" + "\"" + updFuelQuota + "\"" +"}";
                        "\"remainFuelQuota\":" + calcFuelQuota(selectedVehicleType) +"}";
                Log.e(TAG, "getBody: "+body);

                return body.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers=new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    public int calcFuelQuota(String vehicleType){
        if(vehicleType.equals("Car")){
            return 20;
        }

        return 0;

    }

    public void addUser(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String addAPI = addUserAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(Customer_Registration.this, "User details added!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update user fuel quota details: "+error.getLocalizedMessage());
            }
        }) {

            @Override
            public byte[] getBody() {

                String body="{\"username\":" + "\"" + usernameTxt.getText().toString() + "\"," +
                        "\"password\":" + "\"" + passwordTxt.getText().toString() + "\"," +
                        "\"mobile\":" + "\"" + contactTxt.getText().toString() + "\"," +
                        "\"userrole\":" + "\"" + "customer" + "\"" +"}";
                Log.e(TAG, "getBody: "+body);

                return body.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers=new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.regBtn:
                if(!passwordTxt.getText().toString().equals(confirmPasswordTxt.getText().toString())){
                    Toast.makeText(Customer_Registration.this, "Passwords are mismatched", Toast.LENGTH_LONG).show();
                } else {
                    if(!usernameTxt.getText().toString().isEmpty() && !contactTxt.getText().toString().isEmpty() && !passwordTxt.getText().toString().isEmpty()){
                        addCustomer();
                    }
                }

        }
    }
}