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

    private TextView txtVehNumber, txtVehType, txtFuelType;
    private Spinner spinnerLocation, spinnerShedName;
    private EditText edtTxtDate;
    private Button btnCheckFuel;

    private String selectedLocation, selectedShed;
    //test
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    String api = "http://192.168.1.30:7135/api/Customer";
    String userDetailAPI = EndpointURL.GET_CUSTOMER_BY_ID;
    String shedDetailAPI = EndpointURL.GET_ALL_SHEDS;
    ArrayList<ShedModel> shedModelList;
    List<String> locations;

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
                    getAllData();
//                    Intent intent = new Intent(CustomerHomepage.this, ViewFuelDetails.class);
//                    startActivity(intent);
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

        shedModelList = new ArrayList<>();
        locations = new ArrayList<>();
        locations.add("Select a location");

        //Get the vehicle details based on logged in user id
        getLoggedInUserDetails();
        getAllSheds();

        //Hide the shed name spinner, date field and button
        this.hideUIElements();

        spinnerLocation.setOnItemSelectedListener(this);
        spinnerShedName.setOnItemSelectedListener(this);

        //Button click
        btnCheckFuel.setOnClickListener(this);
        //test
        Retrofit retrofitClient = RetroFitClient.getInstance();
        iMyService  = retrofitClient.create(IMyService.class);
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

                                shedModelList.add(shedModel);
                                locations.add(shedModel.getLocation());
                            }


                            String[] shedLocations = locations.toArray(new String[0]);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    CustomerHomepage.this, android.R.layout.simple_spinner_item, shedLocations);
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

    public void getAllData(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response.toString());
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
        compositeDisposable.clear();
        super.onStop();
    }
}