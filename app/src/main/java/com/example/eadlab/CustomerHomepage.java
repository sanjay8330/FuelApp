/*
* Developer     :   Sanjay Sakthivel (IT19158228)
* Purpose       :   Handle Customer Homepage Activity
* Created Date  :   18th October 2022
*/
package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;
import com.example.eadlab.Model.ShedModel;
import com.example.eadlab.Model.UserModel;
import com.example.eadlab.Wrapper.SpinnerWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CustomerHomepage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //List of variables to hold UI elements
    private TextView txtVehNumber, txtVehType, txtFuelType, txtNoShed;
    private Spinner spinnerLocation, spinnerShedName;
    private EditText edtTxtDate;
    private Button btnCheckFuel;
    private ImageView imageView_shed, imageView_date;

    private SpinnerWrapper selectedShed;

    //Local variables
    String userId, vehicleType, selectedLocation;
    boolean isFuelQuotaExceeded = false;

    //Endpoints
    String userDetailAPI = EndpointURL.GET_CUSTOMER_BY_ID;
    String shedDetailAPI = EndpointURL.GET_ALL_SHEDS;
    String shedsForLocatAPI = EndpointURL.GET_SHEDS_FOR_LOCATION;

    //Variables related to endpoints
    List<SpinnerWrapper> sheds;
    Set<String> locationsSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        //Get all UI components to variables
        txtVehNumber = findViewById(R.id.txt_veh_num);
        txtVehType = findViewById(R.id.txt_veh_type);
        txtFuelType = findViewById(R.id.txt_veh_fueltype);
        txtNoShed = findViewById(R.id.txt_noShed);

        spinnerLocation = findViewById(R.id.spinner_location);
        spinnerShedName = findViewById(R.id.spinner_fuelstation);

        edtTxtDate = findViewById(R.id.edittxt_date);

        btnCheckFuel = findViewById(R.id.btn_checkfuel);

        imageView_shed = findViewById(R.id.imgView_shed);
        imageView_date = findViewById(R.id.imgView_date);

        //Handle TextViews
        txtNoShed.setVisibility(TextView.INVISIBLE);

        //Handle Date Field
        edtTxtDate.setText(this.getCurrentDate());
        edtTxtDate.setEnabled(false);

        //Allocate memory for Location Spinner
        locationsSet = new HashSet<>();

        //Allocate memory for Fuel Station Spinner
        sheds = new ArrayList<>();

        //Get the vehicle details based on logged in user id
        userId = "634eba7a5e2da36177ba8640";
        //Intent intent = getIntent();
        //userId = intent.getStringExtra("userid");
        getLoggedInUserDetails(userId);

        //Get the list of locations
        getAllSheds();

        //Hide the shed name spinner, date field and button
        this.hideUIElements();

        spinnerLocation.setOnItemSelectedListener(this);
        spinnerShedName.setOnItemSelectedListener(this);

        //Button click
        btnCheckFuel.setOnClickListener(this);
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Handle on item selection for spinners (Location, Fuel Station)
     **********************************************************************************/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinner_location:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a location")){
                    Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i).toString() );
                    selectedLocation = adapterView.getItemAtPosition(i).toString();
                    getShedsForLocation(selectedLocation);//Get sheds for selected location
                    this.showUIElements();
                }else{
                    Toast.makeText(this, "Please select a location!", Toast.LENGTH_LONG);
                    this.hideUIElements();
                }
                break;
            case R.id.spinner_fuelstation:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a Fuel Station")){
                    selectedShed = (SpinnerWrapper) adapterView.getSelectedItem();
                    Log.e(TAG, "Selected Shed ID: "+selectedShed.getId());
                }else{
                    selectedShed = new SpinnerWrapper();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Handle on click for button (Check Fuel)
     **********************************************************************************/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkfuel:
                if(!selectedLocation.isEmpty() && !selectedShed.getName().isEmpty() && !edtTxtDate.getText().toString().isEmpty()){
                    Intent intent = new Intent(CustomerHomepage.this, ViewFuelDetails.class);
                    intent.putExtra("shedID", selectedShed.getId());
                    intent.putExtra("vehicleType", vehicleType);
                    intent.putExtra("userID", userId);
                    intent.putExtra("isQuoteExceeded", isFuelQuotaExceeded);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Please fill the required fields to continue!"+selectedShed, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Show UI components once location is selected
     **********************************************************************************/
    public void showUIElements(){
        spinnerShedName.setVisibility(View.VISIBLE);
        edtTxtDate.setVisibility(View.VISIBLE);
        btnCheckFuel.setVisibility(View.VISIBLE);
        imageView_date.setVisibility(View.VISIBLE);
        imageView_shed.setVisibility(View.VISIBLE);
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Hide UI elements (Fuel station spinner, date) once page loaded
     **********************************************************************************/
    public void hideUIElements(){
        spinnerShedName.setVisibility(View.INVISIBLE);
        edtTxtDate.setVisibility(View.INVISIBLE);
        btnCheckFuel.setVisibility(View.INVISIBLE);
        imageView_date.setVisibility(View.INVISIBLE);
        imageView_shed.setVisibility(View.INVISIBLE);
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Populate the current date to date field
     **********************************************************************************/
    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

    private static final String TAG = "CUSTOMER_HOMEPAGE_LOG";

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the details of logged in user
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void getLoggedInUserDetails(String userId){
        RequestQueue queue = Volley.newRequestQueue(this);

        String getUserAPI = userDetailAPI + userId.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUserAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            //JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            UserModel userModel = new UserModel(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("vehicleType"),
                                    jsonObject.getString("vehicleNumber"),
                                    jsonObject.getString("fuelType"),
                                    jsonObject.getInt("remainFuelQuota")
                            );

                            txtVehNumber.setText(userModel.getVehicleNumber());
                            txtVehType.setText(userModel.getVehicleType());
                            txtFuelType.setText(userModel.getFuelType());
                            vehicleType = userModel.getVehicleType().toLowerCase();

                            //Check if fuel quota exceeded
                            if(userModel.getRemainFuelQuota() == 0){
                                isFuelQuotaExceeded = true;
                            }else{
                                isFuelQuotaExceeded = false;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            }
        });

        queue.add(stringRequest);
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the list of locations based on all sheds
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void getAllSheds(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, shedDetailAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ShedModel shedModel = new ShedModel(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("shedName"),
                                        jsonObject.getString("location")
                                );

                                SpinnerWrapper spinnerWrapper = new SpinnerWrapper();
                                spinnerWrapper.setId(shedModel.getId());
                                spinnerWrapper.setName(shedModel.getLocation());

                                locationsSet.add(spinnerWrapper.getName());
                            }

                            List<String> locationsList = new ArrayList<>();
                            locationsList.add("Select a location");

                            for(String val: locationsSet){
                                locationsList.add(val);
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    CustomerHomepage.this, android.R.layout.simple_spinner_item, locationsList);
                            spinnerLocation.setAdapter(spinnerArrayAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            }
        });

        queue.add(stringRequest);
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the list of sheds for selected location
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void getShedsForLocation(String location){
        RequestQueue queue = Volley.newRequestQueue(this);

        String shedsAPI = shedsForLocatAPI + location.trim();
        sheds.clear();
        sheds.add(new SpinnerWrapper("01", "Select a Fuel Station"));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, shedsAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            if(!response.isEmpty()){
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ShedModel shedModel = new ShedModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("shedName"),
                                            jsonObject.getString("location")
                                    );

                                    SpinnerWrapper spinnerWrapper = new SpinnerWrapper();
                                    spinnerWrapper.setId(shedModel.getId());
                                    spinnerWrapper.setName(shedModel.getShedName());
                                    sheds.add(spinnerWrapper);
                                }

                                if(sheds.size() >= 1){
                                    txtNoShed.setVisibility(TextView.INVISIBLE);
                                    ArrayAdapter<SpinnerWrapper> spinnerArrayAdapter = new ArrayAdapter<SpinnerWrapper>(
                                            CustomerHomepage.this, android.R.layout.simple_spinner_item, sheds);
                                    spinnerShedName.setAdapter(spinnerArrayAdapter);
                                }else{
                                    hideUIElements();
                                    txtNoShed.setVisibility(TextView.VISIBLE);
                                }
                            }else{
                                hideUIElements();
                                txtNoShed.setVisibility(TextView.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            }
        });

        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}