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
import com.example.eadlab.Model.FuelModel;
import com.example.eadlab.Model.QueueModel;
import com.example.eadlab.Model.ShedModel;
import com.example.eadlab.Model.UserModel;
import com.example.eadlab.Wrapper.SpinnerWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewFuelDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView txtView_shedLocat, txtView_shedName, txtView_arrTime, txtView_arrAmount, txtView_remAmount, txtView_vehCount, txtView_noFuel;
    private Spinner spinner_fuelType;
    private ImageView imgView_fuelStatus, imgView_fuelQueue, imageView_noFuel;
    private Button btnEnterQueue;
    private TextView label_arrTime, label_arrLitres, label_remLitres, label_vehCount;

    SpinnerWrapper selectedFuelType;
    String shedName, enteredQueueId, vehicleType;

    //Endpoints
    String getShedByIdAPI = EndpointURL.GET_SHED_BY_ID;
    String getFuelAPI = EndpointURL.GET_FUEL_BY_SHEDNAME_FUELTYPE;
    String getQueueAPI = EndpointURL.GET_QUEUE_BY_SHEDNAME_FUELTYPE;

    //Endpoint Variables
    List<SpinnerWrapper> fuelTypes;

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

        txtView_noFuel = findViewById(R.id.txtview_nofuel);
        imageView_noFuel = findViewById(R.id.imageView_nofuel);

        //Hide no fuel tags
        txtView_noFuel.setVisibility(TextView.INVISIBLE);
        imageView_noFuel.setVisibility(TextView.INVISIBLE);

        //Default labels & images
        label_arrTime = findViewById(R.id.label_fueldetail1);
        label_arrLitres = findViewById(R.id.label_fueldetail2);
        label_remLitres = findViewById(R.id.label_fueldetail3);
        label_vehCount = findViewById(R.id.label_currentVehCount);
        imgView_fuelQueue = findViewById(R.id.imageView_fuelQueue);

        this.hideUIElements();


        //Get the petrol shed details based on selected shed id
        Intent intent = getIntent();
        String shedID = intent.getStringExtra("shedID");
        vehicleType = intent.getStringExtra("vehicleType");
        getShedsDetails(shedID);

        //Add standard values to fuel type spinner
        fuelTypes = new ArrayList<>();
        fuelTypes.add(new SpinnerWrapper("01", "Select a Fuel Type"));
        fuelTypes.add(new SpinnerWrapper("petrol", "Petrol 92 - Normal"));//Change the date to make it petrol92 as ID
        fuelTypes.add(new SpinnerWrapper("petrol95", "Petrol 95 - Super"));
        fuelTypes.add(new SpinnerWrapper("diesel92", "Diesel 92 - Normal"));
        fuelTypes.add(new SpinnerWrapper("diesel95", "Diesel 95 - Super"));
        ArrayAdapter<SpinnerWrapper> spinnerArrayAdapter = new ArrayAdapter<SpinnerWrapper>(
                ViewFuelDetails.this, android.R.layout.simple_spinner_dropdown_item, fuelTypes);
        spinner_fuelType.setAdapter(spinnerArrayAdapter);
        //Get the fuel and queue details for selected fuel type

        spinner_fuelType.setOnItemSelectedListener(this);

        btnEnterQueue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_enterQueue:
                Toast.makeText(this, "Entered to Queue", Toast.LENGTH_LONG).show();
                //Pass enteredQueueId to next activity, selectedFuelType.getName(), shedName
                Intent intent = new Intent(ViewFuelDetails.this, QueueDetails.class);
                intent.putExtra("queueID", enteredQueueId);
                intent.putExtra("fuelType", selectedFuelType.getId());
                intent.putExtra("shedName", shedName);
                intent.putExtra("vehicleType", vehicleType);
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
                    selectedFuelType = (SpinnerWrapper) adapterView.getSelectedItem();
                    getFuelAndQueueDetails(selectedFuelType.getId(), shedName);
                    //getFuelAndQueueDetails("petrol", "cde");
                }else{
                    selectedFuelType = new SpinnerWrapper();
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

    private static final String TAG = "VIEW_FUEL_DETAILS_PAGE_LOG";

    public void getShedsDetails(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        getShedByIdAPI = getShedByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getShedByIdAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        if(!response.isEmpty()){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                ShedModel shedModel = new ShedModel(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("shedName"),
                                        jsonObject.getString("location")
                                );

                                txtView_shedName.setText(shedModel.getShedName());
                                txtView_shedLocat.setText(shedModel.getLocation());
                                shedName = shedModel.getShedName();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Log.e(TAG, "Shed details cannot be retrieved!");
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

    public void getFuelAndQueueDetails(String fuelType, String shedName){
        RequestQueue queue = Volley.newRequestQueue(this);

        String getAPI = getFuelAPI + fuelType.trim() + "/" + shedName.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            if(!response.isEmpty()){
                                JSONObject jsonObject = new JSONObject(response);
                                FuelModel fuelModel = new FuelModel(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("fuelType"),
                                        jsonObject.getString("shedName"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("arrivalTime"),
                                        jsonObject.getString("arrivedLitres"),
                                        jsonObject.getString("remainLitres")
                                );

                                txtView_arrTime.setText(fuelModel.getArrivalTime());
                                txtView_arrAmount.setText(fuelModel.getArrivedLitres());
                                txtView_remAmount.setText(fuelModel.getRemainLitres());

                                txtView_noFuel.setVisibility(TextView.INVISIBLE);
                                imageView_noFuel.setVisibility(TextView.INVISIBLE);

                                showUIElements();

                                getQueueDetails("petrol", "abc");
                                //getQueueDetails(fuelType.trim(), shedName.trim());

                            }else{
                                hideUIElements();

                                //Show no fuel components
                                txtView_noFuel.setVisibility(TextView.VISIBLE);
                                imageView_noFuel.setVisibility(TextView.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());

                hideUIElements();

                //Show no fuel components
                txtView_noFuel.setVisibility(TextView.VISIBLE);
                imageView_noFuel.setVisibility(TextView.VISIBLE);
            }
        });

        queue.add(stringRequest);
    }

    public void getQueueDetails(String fuelType, String shedName){
        RequestQueue queue = Volley.newRequestQueue(this);

        getQueueAPI = getQueueAPI + fuelType.trim() + "/" + shedName.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getQueueAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            if(!response.isEmpty()){
                                JSONObject jsonObject = new JSONObject(response);
                                QueueModel queueModel = new QueueModel(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("shedName"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("fuelType"),
                                        jsonObject.getInt("vehicleCount")
                                );

                                txtView_vehCount.setText(String.valueOf(queueModel.getVehicleCount()));
                                enteredQueueId = queueModel.getId();
                            }else{
                                Toast.makeText(ViewFuelDetails.this, "Fuel Station has no queues!", Toast.LENGTH_LONG);
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
}