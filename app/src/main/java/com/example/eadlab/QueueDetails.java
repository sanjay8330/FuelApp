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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;
import com.example.eadlab.Model.FuelModel;
import com.example.eadlab.Model.QueueModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QueueDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView txtView_updVehCount, txtView_remAmount, txtView_waitTime;
    private Button btn_exit, btn_fueled;
    private ImageButton imgBtn_refresh;

    String queueID;
    String fuelType;
    String shedName;

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    //Endpoints
    String getQueueByIdAPI = EndpointURL.GET_QUEUE_BY_ID;
    String getFuelAPI = EndpointURL.GET_FUEL_BY_SHEDNAME_FUELTYPE;
    String updateQueueByIdAPI = EndpointURL.UPDATE_QUEUE_BY_ID;

    //Endpoint Variables
    QueueModel queueModelOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_details);

        //Get the ID of all necessary elements
        txtView_updVehCount = findViewById(R.id.txtview_updVehCount);
        txtView_remAmount = findViewById(R.id.label_remainfuel);
        txtView_waitTime = findViewById(R.id.txtView_waitTime);

        btn_exit = findViewById(R.id.btn_exit);
        btn_fueled = findViewById(R.id.btn_fueled);

        imgBtn_refresh = findViewById(R.id.imgbtn_refresh);

        //Show the queue and fuel details
        queueID = "634f97552d6bab3a5d7ed2d5";
        fuelType = "petrol";
        shedName = "cde";
        getQueueDetails(queueID);
        getFuelDetails(fuelType, shedName);

        //Update the vehicle count in queue
        //updateQueueDetails(queueID);//Update the Database

        //Timer
        //startTimer();

        //Show the remaining fuel details
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        txtView_waitTime.setText(getTimerText());
                    }
                });

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText(){
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) % 60;
        int hours = ((rounded % 86400) % 3600) % 60;

        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtn_refresh:
                Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
                getFuelDetails(fuelType, shedName);
                break;
            case R.id.btn_exit:
                Toast.makeText(this, "Exited from the Queue", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_fueled:
                Toast.makeText(this, "Fueled the vehicle", Toast.LENGTH_LONG).show();
            default:
                break;
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
                            JSONObject jsonObject = new JSONObject(response);
                            QueueModel queueModel = new QueueModel(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("shedName"),
                                    jsonObject.getString("date"),
                                    jsonObject.getString("fuelType"),
                                    jsonObject.getInt("vehicleCount")
                            );

                            txtView_updVehCount.setText(String.valueOf(queueModel.getVehicleCount() + 1));
                            queueModelOuter = queueModel;

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

    public void updateQueueDetails(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        updateQueueByIdAPI = updateQueueByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, updateQueueByIdAPI,
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
                params.put("id", queueModelOuter.getId());
                params.put("shedName", queueModelOuter.getShedName());
                params.put("date", queueModelOuter.getDate());
                params.put("fuelType", queueModelOuter.getFuelType());
                params.put("vehicleCount", txtView_updVehCount.getText().toString());

                // at last we are
                // returning our params.
                return params;
            }
        };

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