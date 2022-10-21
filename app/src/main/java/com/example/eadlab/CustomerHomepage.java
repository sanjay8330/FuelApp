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
import com.example.eadlab.RetroFit.IMyService;
import com.example.eadlab.RetroFit.RetroFitClient;
import com.example.eadlab.Wrapper.SpinnerWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import io.reactivex.disposables.Disposables;

public class CustomerHomepage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView txtVehNumber, txtVehType, txtFuelType, txtNoShed;
    private Spinner spinnerLocation, spinnerShedName;
    private EditText edtTxtDate;
    private Button btnCheckFuel;

    private SpinnerWrapper selectedLocation, selectedShed;
    private String selectedShedId;

    //Endpoints
    String userDetailAPI = EndpointURL.GET_CUSTOMER_BY_ID + "634eba7a5e2da36177ba8640";
    String shedDetailAPI = EndpointURL.GET_ALL_SHEDS;
    String shedsForLocatAPI = EndpointURL.GET_SHEDS_FOR_LOCATION;

    //Endpoint Variables
    List<SpinnerWrapper> locations;
    List<SpinnerWrapper> sheds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        //Get all the components IDs
        txtVehNumber = findViewById(R.id.txt_veh_num);
        txtVehType = findViewById(R.id.txt_veh_type);
        txtFuelType = findViewById(R.id.txt_veh_fueltype);
        txtNoShed = findViewById(R.id.txt_noShed);

        spinnerLocation = findViewById(R.id.spinner_location);
        spinnerShedName = findViewById(R.id.spinner_fuelstation);

        edtTxtDate = findViewById(R.id.edittxt_date);

        btnCheckFuel = findViewById(R.id.btn_checkfuel);

        edtTxtDate.setText(this.getCurrentDate());

        locations = new ArrayList<>();
        locations.add(new SpinnerWrapper("01", "Select a location"));

        sheds = new ArrayList<>();
        sheds.add(new SpinnerWrapper("01", "Select a Fuel Station"));

        //Get the vehicle details based on logged in user id
        getLoggedInUserDetails();
        getAllSheds();

        //Hide the shed name spinner, date field and button
        this.hideUIElements();

        spinnerLocation.setOnItemSelectedListener(this);
        spinnerShedName.setOnItemSelectedListener(this);

        //Button click
        btnCheckFuel.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinner_location:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a location")){
                    selectedLocation = (SpinnerWrapper) adapterView.getSelectedItem();
                    getShedsForLocation(selectedLocation.getName());
                    this.showUIElements();
                }else{
                    selectedLocation = new SpinnerWrapper();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkfuel:
                if(!selectedLocation.getName().isEmpty() && !selectedShed.getName().isEmpty() && !edtTxtDate.getText().toString().isEmpty()){
                    Intent intent = new Intent(CustomerHomepage.this, ViewFuelDetails.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Please fill the required fields to continue!"+selectedShed, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

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

    //test
    private static final String TAG = "CustomerHomePage";

    public void getLoggedInUserDetails(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, userDetailAPI,
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
                                    jsonObject.getString("fuelType")
                            );

                            txtVehNumber.setText(userModel.getVehicleNumber());
                            txtVehType.setText(userModel.getVehicleType());
                            txtFuelType.setText(userModel.getFuelType());

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
                                locations.add(spinnerWrapper);
                            }


                            ArrayAdapter<SpinnerWrapper> spinnerArrayAdapter = new ArrayAdapter<SpinnerWrapper>(
                                    CustomerHomepage.this, android.R.layout.simple_spinner_item, locations);
                            spinnerLocation.setAdapter(spinnerArrayAdapter);
                            //spinnerLocation



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

    public void getShedsForLocation(String location){
        RequestQueue queue = Volley.newRequestQueue(this);

        shedsForLocatAPI = shedsForLocatAPI + location.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, shedsForLocatAPI,
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
                                spinnerWrapper.setName(shedModel.getShedName());
                                sheds.add(spinnerWrapper);
                            }

                            if(sheds.size() >= 1){
                                txtNoShed.setVisibility(TextView.INVISIBLE);
                                ArrayAdapter<SpinnerWrapper> spinnerArrayAdapter = new ArrayAdapter<SpinnerWrapper>(
                                        CustomerHomepage.this, android.R.layout.simple_spinner_item, sheds);
                                spinnerShedName.setAdapter(spinnerArrayAdapter);
                            }else{
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

    //Spinner class
//    public class SpinnerWrapper{
//        private String id;
//        private String name;
//
//        @Override
//        public String toString(){
//            return name;
//        }
//
//        public SpinnerWrapper(){
//            this.id = "";
//            this.name = "";
//        }
//
//        public SpinnerWrapper(String id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
}