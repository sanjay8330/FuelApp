package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;
import com.example.eadlab.Model.FuelModel;
import com.example.eadlab.Model.QueueModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QueueDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView txtView_updVehCount, txtView_remAmount, txtView_fuelAmount;
    private Button btn_exit, btn_fueled;
    private ImageButton imgBtn_minus, imgBtn_plus, imgBtn_refresh;

    String queueID;
    String fuelType;
    String shedName;
    int currVehicleCount = 0;

    //Endpoints
    String getQueueByIdAPI = EndpointURL.GET_QUEUE_BY_ID;
    String getFuelAPI = EndpointURL.GET_FUEL_BY_SHEDNAME_FUELTYPE;
    String updateQueueByIdAPI = EndpointURL.UPDATE_QUEUE_BY_ID;
    String updateFuelByIdAPI = EndpointURL.UPDATE_FUEL_BY_ID;

    //Endpoint Variables
    QueueModel queueModelOuter = new QueueModel();
    FuelModel fuelModelOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_details);

        //Get the ID of all necessary elements
        txtView_updVehCount = findViewById(R.id.txtview_updVehCount);
        txtView_remAmount = findViewById(R.id.label_remainfuel);
        txtView_fuelAmount = findViewById(R.id.txtView_fuelAmount);

        btn_exit = findViewById(R.id.btn_exit);
        btn_fueled = findViewById(R.id.btn_fueled);

        imgBtn_refresh = findViewById(R.id.imgbtn_refresh);
        imgBtn_minus = findViewById(R.id.imgBtn_minus);
        imgBtn_plus = findViewById(R.id.imgBtn_plus);

        //Set the fuel amount
        txtView_fuelAmount.setText(String.valueOf(0));

        //Show the queue and fuel details
        queueID = "634f97552d6bab3a5d7ed2d5";
        fuelType = "petrol";
        shedName = "cde";
        getQueueDetails(queueID);
        getFuelDetails(fuelType, shedName);

        //Update the vehicle count in queue
        //updateQueueDetails(queueID, currVehicleCount);//Update the Database

        imgBtn_minus.setOnClickListener(this);
        imgBtn_plus.setOnClickListener(this);
        imgBtn_refresh.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_fueled.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_minus:
                Toast.makeText(this, "Fuel Reduced!", Toast.LENGTH_LONG).show();
                reduceFuelAmount();
                break;
            case R.id.imgBtn_plus:
                Toast.makeText(this, "Fuel Increased!", Toast.LENGTH_LONG).show();
                increaseFuelAmount();
                break;
            case R.id.imgbtn_refresh:
                Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
                getFuelDetails(fuelType, shedName);
                break;
            case R.id.btn_exit:
                //updateQueueDetails(queueID, Integer.valueOf(txtView_updVehCount.getText().toString()) - 1);
                Toast.makeText(this, "Exited from the Queue", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_fueled:
                //updateQueueDetails(queueID, Integer.valueOf(txtView_updVehCount.getText().toString()) - 1);
                updateFuelDetails(fuelModelOuter.getId());
                Toast.makeText(this, "Fueled the vehicle", Toast.LENGTH_LONG).show();
            default:
                break;
        }
    }

    public void reduceFuelAmount() {
        String currFuelAmount = txtView_fuelAmount.getText().toString();
        if(Integer.valueOf(currFuelAmount) > 0){
            int newFuelAmount = Integer.valueOf(currFuelAmount) - 1;
            txtView_fuelAmount.setText(String.valueOf(newFuelAmount));
        }
    }

    public void increaseFuelAmount() {
        String currFuelAmount = txtView_fuelAmount.getText().toString();
        if(Integer.valueOf(currFuelAmount) < 50){
            int newFuelAmount = Integer.valueOf(currFuelAmount) + 1;
            txtView_fuelAmount.setText(String.valueOf(newFuelAmount));
        }
    }

    private static String TAG = "QueueDetailsPage";

    public void getQueueDetails(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        getQueueByIdAPI = getQueueByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getQueueByIdAPI,
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

                                txtView_updVehCount.setText(String.valueOf(queueModel.getVehicleCount() + 1));
                                currVehicleCount = queueModel.getVehicleCount();
                                queueModelOuter = queueModel;

                                //updateQueueDetails(queueID, String.valueOf(currVehicleCount));//Update Database
                                updateQueueDetails(queueID, String.valueOf(currVehicleCount + 1));
                            }else{
                                Toast.makeText(QueueDetails.this, "No queue in fuel station!", Toast.LENGTH_LONG);
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

    public void getFuelDetails(String fuelType, String shedName){
        RequestQueue queue = Volley.newRequestQueue(this);

        getFuelAPI = getFuelAPI + fuelType.trim() + "/" + shedName.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getFuelAPI,
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

                                txtView_remAmount.setText(fuelModel.getRemainLitres());
                                fuelModelOuter = fuelModel;
                            }else{
                                Toast.makeText(QueueDetails.this, "Fuel not available!", Toast.LENGTH_LONG);
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

    public void updateFuelDetails(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        updateFuelByIdAPI = updateFuelByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, updateFuelByIdAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(QueueDetails.this, "Database Updated!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id", fuelModelOuter.getId());
                params.put("fuelType", fuelModelOuter.getFuelType());
                params.put("shedName", fuelModelOuter.getShedName());
                params.put("date", fuelModelOuter.getDate());
                params.put("arrivalTime", fuelModelOuter.getArrivalTime());
                params.put("arrivedLitres", fuelModelOuter.getArrivedLitres());

                int updRemainAmount = updateFuelAmount(Integer.valueOf(fuelModelOuter.getRemainLitres()));
                params.put("remainLitres", String.valueOf(updRemainAmount));

                // at last we are
                // returning our params.
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public int updateFuelAmount(int originalAmount){
        return originalAmount - Integer.valueOf(txtView_fuelAmount.getText().toString());
    }

    //TestUpdate code
    public void updateQueueDetails(String id, String vehicleCount){
        updateQueueByIdAPI = updateQueueByIdAPI + id.trim();

        // Optional Parameters to pass as POST request
        JSONObject params = new JSONObject();
        try {
            params.put("id", queueModelOuter.getId());
            params.put("shedName", queueModelOuter.getShedName());
            params.put("date", String.valueOf(LocalDateTime.now()));
            params.put("fuelType", queueModelOuter.getFuelType());
            params.put("vehicleCount", vehicleCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, updateQueueByIdAPI, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: Updated Successfully!");
                        Toast.makeText(QueueDetails.this, "Updated Successfully", Toast.LENGTH_SHORT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onUpdateErrorResponse: "+error.getLocalizedMessage());
                Toast.makeText(QueueDetails.this, "Error Occurred", Toast.LENGTH_SHORT);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);
    }

}