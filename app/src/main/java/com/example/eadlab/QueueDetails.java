/*
 * Developer     :   Sanjay Sakthivel (IT19158228)
 * Purpose       :   Handle Queue Details page
 * Created Date  :   18th October 2022
 */
package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class QueueDetails extends AppCompatActivity implements View.OnClickListener {

    //List of variables to hold UI elements
    private TextView txtView_updVehCount, txtView_remAmount, txtView_fuelAmount, txtView_fuelCost;
    private Button btn_exit, btn_fueled;
    private ImageButton imgBtn_minus, imgBtn_plus, imgBtn_refresh;

    //Local variables
    String queueID, fuelID, fuelType, shedName, vehicleType, userID;
    int currVehicleCount = 0;
    int maxFuelToPump = 0;
    double fuelCost = 0.0;

    //Endpoints
    String getQueueByIdAPI = EndpointURL.GET_QUEUE_BY_ID;
    String getFuelAPI = EndpointURL.GET_FUEL_BY_SHEDNAME_FUELTYPE;
    String updateQueueByIdAPI = EndpointURL.UPDATE_QUEUE_BY_ID;
    String updateFuelByIdAPI = EndpointURL.UPDATE_FUEL_BY_ID;

    //Variables related to Endpoint
    QueueModel queueModelOuter = new QueueModel();
    FuelModel fuelModelOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_details);

        //Get the ID of necessary UI elements
        txtView_updVehCount = findViewById(R.id.txtview_updVehCount);
        txtView_remAmount = findViewById(R.id.label_remainfuel);
        txtView_fuelAmount = findViewById(R.id.txtView_fuelAmount);
        txtView_fuelCost = findViewById(R.id.txtview_fuelcost);

        btn_exit = findViewById(R.id.btn_exit);
        btn_fueled = findViewById(R.id.btn_fueled);

        imgBtn_refresh = findViewById(R.id.imgbtn_refresh);
        imgBtn_minus = findViewById(R.id.imgBtn_minus);
        imgBtn_plus = findViewById(R.id.imgBtn_plus);

        //Show the queue and fuel details
        Intent intent = getIntent();
        queueID = intent.getStringExtra("queueID");
        fuelType = intent.getStringExtra("fuelType");
        shedName = intent.getStringExtra("shedName");
        vehicleType = intent.getStringExtra("vehicleType");
        userID = intent.getStringExtra("userID");
//        queueID = "634f97552d6bab3a5d7ed2d5";
//        fuelType = "petrol";
//        shedName = "cde";

        //Set the initial fuel amount & cost
        txtView_fuelAmount.setText(String.valueOf(initializeFuelAmount(vehicleType)));
        txtView_fuelCost.setText(String.valueOf(calculateFuelCost(fuelType, maxFuelToPump)));

        getQueueDetails(queueID);
        getFuelDetails(fuelType, shedName);

        imgBtn_minus.setOnClickListener(this);
        imgBtn_plus.setOnClickListener(this);
        imgBtn_refresh.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_fueled.setOnClickListener(this);

    }

    /*************************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Handle on onclick for buttons (minus, plus, refresh, exit, fueled)
     *************************************************************************************/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_minus:
                reduceFuelAmount();
                break;
            case R.id.imgBtn_plus:
                increaseFuelAmount();
                break;
            case R.id.imgbtn_refresh:
                getFuelDetails(fuelType, shedName);
                Toast.makeText(this, "Fuel Amount Refreshed", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_exit:
                updateQueueDetails(queueID, String.valueOf(currVehicleCount - 1));
                Toast.makeText(this, "Exited from the Queue", Toast.LENGTH_LONG).show();
                Intent activityIntent = new Intent(QueueDetails.this, CustomerHomepage.class);
                activityIntent.putExtra("userid", userID);
                startActivity(activityIntent);
                break;
            case R.id.btn_fueled:
                if(Integer.valueOf(txtView_fuelAmount.getText().toString()) > 0){
                    if(Integer.valueOf(txtView_fuelAmount.getText().toString()) <= maxFuelToPump){
                        updateQueueDetails(queueID, String.valueOf(currVehicleCount - 1));
                        updateFuelDetails(fuelID);
                        Toast.makeText(this, "Fueled the vehicle successfully!", Toast.LENGTH_LONG).show();

                        //Navigate to Fueled Page
                        Intent intent = new Intent(QueueDetails.this, fueledpage.class);
                        intent.putExtra("shedName", shedName);
                        intent.putExtra("vehicleType", vehicleType);
                        intent.putExtra("fuelType", fuelType);
                        intent.putExtra("fueledAmount", txtView_fuelAmount.getText().toString());
                        intent.putExtra("fuelAmount", txtView_fuelCost.getText().toString());
                        intent.putExtra("userID", userID);
                        startActivity(intent);

                    }else{
                        Toast.makeText(this, "The fuel amount exceeds the allocated quota!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Please enter the fuel amount!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Initialize the fuel amount based on vehicle type
     **********************************************************************************/
    public int initializeFuelAmount(String vehType){
        int returnFuelAmount;
        if(vehicleType.equals("car") || vehicleType.equals("motor-car")){
            returnFuelAmount = 20;
        }else if(vehType.equals("van") || vehType.equals("passenger-van")){
            returnFuelAmount = 40;
        }else if(vehType.equals("bus") || vehType.equals("lorry")){
            returnFuelAmount = 60;
        }else if(vehType.equals("bike") || vehType.equals("motor-bike")){
            returnFuelAmount = 3;
        }else if(vehType.equals("three-wheeler") || vehType.equals("auto")){
            returnFuelAmount = 5;
        }else{
            returnFuelAmount = 0;
        }

        maxFuelToPump = returnFuelAmount;
        return returnFuelAmount;
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Calculate the fuel cost based on fuel amount
     **********************************************************************************/
    public double calculateFuelCost(String fuelType, int fuelAmount){
        double cost;

        if(fuelType.equals("petrol") || fuelType.equals("petrol92")){
            cost = 370.0 * fuelAmount;
        }else if(fuelType.equals("petrol95")){
            cost = 410.0 * fuelAmount;
        }else if(fuelType.equals("diesel92")){
            cost = 480.0 * fuelAmount;
        }else if(fuelType.equals("diesel95")){
            cost = 515.0 * fuelAmount;
        }else{
            cost = 0.0;
        }

        return cost;
    }

    /****************************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Handle Minus buttons (Reduce fuel amount + calculate new fuel cost)
     ***************************************************************************************/
    public void reduceFuelAmount() {
        String currFuelAmount = txtView_fuelAmount.getText().toString();
        if(Integer.valueOf(currFuelAmount) > 0){
            int newFuelAmount = Integer.valueOf(currFuelAmount) - 1;
            txtView_fuelAmount.setText(String.valueOf(newFuelAmount));
            double cost = calculateFuelCost(fuelType, Integer.valueOf(txtView_fuelAmount.getText().toString()));
            txtView_fuelCost.setText(String.valueOf(cost));
        }
    }

    /****************************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Handle Plus buttons (Increase fuel amount + calculate new fuel cost)
     ***************************************************************************************/
    public void increaseFuelAmount() {
        String currFuelAmount = txtView_fuelAmount.getText().toString();
        if(Integer.valueOf(currFuelAmount) < maxFuelToPump){
            int newFuelAmount = Integer.valueOf(currFuelAmount) + 1;
            txtView_fuelAmount.setText(String.valueOf(newFuelAmount));
            double cost = calculateFuelCost(fuelType, Integer.valueOf(txtView_fuelAmount.getText().toString()));
            txtView_fuelCost.setText(String.valueOf(cost));
        }else{
            Toast.makeText(this, "You can't exceed the allocated max quota!", Toast.LENGTH_SHORT).show();
        }
    }

    private static String TAG = "QUEUE_DETAILS_PAGE_LOG";

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the queue details based on queue ID
     * @MethodType  :   API CallOut
     **********************************************************************************/
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

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the details of fuel on shed (Remain Fuel Litres)
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void getFuelDetails(String fuelType, String shedName){
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

                                txtView_remAmount.setText(fuelModel.getRemainLitres());
                                fuelID = fuelModel.getId().trim();
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

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Update the fuel amount in shed once fueled successfully
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void updateFuelDetails(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        String updateAPI = updateFuelByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, updateAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(QueueDetails.this, "Fuel Amount Updated!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update fuel details: "+error.getLocalizedMessage());
            }
        }) {
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//
//                // on below line we are passing our key
//                // and value pair to our parameters.
//                params.put("id", fuelModelOuter.getId());
//                params.put("fuelType", fuelModelOuter.getFuelType());
//                params.put("shedName", fuelModelOuter.getShedName());
//                params.put("date", fuelModelOuter.getDate());
//                params.put("arrivalTime", fuelModelOuter.getArrivalTime());
//                params.put("arrivedLitres", fuelModelOuter.getArrivedLitres());
//
//                int updRemainAmount = updateFuelAmount(Integer.valueOf(fuelModelOuter.getRemainLitres()));
//                params.put("remainLitres", String.valueOf(updRemainAmount));
//
//                // at last we are
//                // returning our params.
//                return params;
//            }

            @Override
            public byte[] getBody() {
                int updRemainAmount = updateFuelAmount(Integer.valueOf(fuelModelOuter.getRemainLitres()));

                String body="{\"id\":" + "\"" + fuelModelOuter.getId() + "\"," +
                        "\"fuelType\":" + "\"" + fuelModelOuter.getFuelType() + "\"," +
                        "\"shedName\":" + "\"" + fuelModelOuter.getShedName() + "\"," +
                        "\"date\":" + "\"" + fuelModelOuter.getDate() + "\"," +
                        "\"arrivalTime\":" + "\"" + fuelModelOuter.getArrivalTime() + "\"," +
                        "\"arrivedLitres\":" + "\"" + fuelModelOuter.getArrivedLitres() + "\"," +
                        "\"remainLitres\":" + "\"" + updRemainAmount + "\"" +"}";
                Log.e(TAG, "getBody: "+body.getBytes());

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

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Calculate the new fuel amount after fueled in the shed
     **********************************************************************************/
    public int updateFuelAmount(int originalAmount){
        return originalAmount - Integer.valueOf(txtView_fuelAmount.getText().toString());
    }

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Update the queue details once entered and left (Exit/Fueled)
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void updateQueueDetails(String id, String vehicleCount){
        RequestQueue queue = Volley.newRequestQueue(this);

        String updateAPI = updateQueueByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, updateAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onUpdateResponse: "+response);
                        Toast.makeText(QueueDetails.this, "Vehicle Count Updated!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error to update vehicle count!: "+error.getMessage());
                Toast.makeText(QueueDetails.this, "Vehicle Count cannot be Updated!", Toast.LENGTH_LONG).show();
            }
        }) {
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//
//                // on below line we are passing our key
//                // and value pair to our parameters.
//                params.put("id", queueModelOuter.getId());
//                params.put("shedName", queueModelOuter.getShedName());
//                params.put("date", String.valueOf(LocalDate.now()));
//                params.put("fuelType", queueModelOuter.getFuelType());
//                params.put("vehicleCount", vehicleCount);
//
//                Log.e(TAG, "getParams: "+params.toString());
//
//                // at last we are
//                // returning our params.
//                return params;
//            }

            @Override
            public byte[] getBody() {
                String body="{\"id\":" + "\"" + queueModelOuter.getId() + "\"," +
                             "\"shedName\":" + "\"" + queueModelOuter.getShedName() + "\"," +
                             "\"date\":" + "\"" + String.valueOf(LocalDate.now()) + "\"," +
                             "\"fuelType\":" + "\"" + queueModelOuter.getFuelType() + "\"," +
                             "\"vehicleCount\":" + "\"" + vehicleCount + "\"" +"}";
                Log.e(TAG, "getBody: "+body.getBytes());
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

}