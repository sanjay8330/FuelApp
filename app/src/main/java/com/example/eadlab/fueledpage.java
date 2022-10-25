/*
 * Developer     :   Sanjay Sakthivel (IT19158228)
 * Purpose       :   Handle Fueled page
 * Created Date  :   18th October 2022
 */
package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.eadlab.Model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fueledpage extends AppCompatActivity implements View.OnClickListener {

    //List of variables to hold UI elements
    TextView txtView_shedName, txtView_vehicleType, txtView_fuelType, txtView_fueledAmount, txtView_fueledCost;
    Button btn_logout, btn_returnHome;

    //Endpoints
    String userDetailAPI = EndpointURL.GET_CUSTOMER_BY_ID;
    String updateUserByIdAPI = EndpointURL.UPDATE_CUSTOMER_BY_ID;

    //Endpoint variables
    UserModel userModelGlobal = new UserModel();

    //Local variables
    String shedName, vehType, fuelType, fuelAmount, fuelCost, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fueledpage);

        //Get the Id of elements
        txtView_shedName = findViewById(R.id.txtview_fueledShed);
        txtView_vehicleType = findViewById(R.id.txtview_fueledVehType);
        txtView_fuelType = findViewById(R.id.txtview_fueledFuelType);
        txtView_fueledAmount = findViewById(R.id.txtview_fueledAmount);
        txtView_fueledCost = findViewById(R.id.txtview_fueledCost);

        btn_logout = findViewById(R.id.btn_logout);
        btn_returnHome = findViewById(R.id.btn_home);

        //Get the parameters from last activity using intent
        Intent intent = getIntent();
        shedName = intent.getStringExtra("shedName");
        vehType = intent.getStringExtra("vehicleType");
        fuelType = intent.getStringExtra("fuelType");
        fuelAmount = intent.getStringExtra("fueledAmount");
        fuelCost = intent.getStringExtra("fuelAmount");
        userID = intent.getStringExtra("userID");

        setFueledDetails();

        if(userID != null || !userID.isEmpty()){
            getUserDetails(userID);
        }

        btn_returnHome.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    /*************************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Set the details about fueled vehicle and amount
     *************************************************************************************/
    public void setFueledDetails(){
        txtView_shedName.setText(shedName);
        txtView_vehicleType.setText(vehType);
        txtView_fuelType.setText(fuelType);
        txtView_fueledAmount.setText(fuelAmount);
        txtView_fueledCost.setText(fuelCost);
    }

    /*************************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Handle on onclick for buttons (Home, Logout)
     *************************************************************************************/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_home:
                Intent intent = new Intent(fueledpage.this, CustomerHomepage.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private static final String TAG = "FUELED_PAGE_LOG";

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the details of user
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void getUserDetails(String userId){
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

                            userModelGlobal = userModel;
                            updateUserDetails(userModel.getId());

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
     * @Purpose     :   Update the users' fuel quota once fueled successfully
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void updateUserDetails(String id){
        RequestQueue queue = Volley.newRequestQueue(this);

        String updateAPI = updateUserByIdAPI + id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, updateAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(fueledpage.this, "Fuel Quota for User Updated!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update user fuel quota details: "+error.getLocalizedMessage());
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
                int updFuelQuota = updateFuelQuota(Integer.valueOf(userModelGlobal.getRemainFuelQuota()));

                String body="{\"id\":" + "\"" + userModelGlobal.getId() + "\"," +
                        "\"name\":" + "\"" + userModelGlobal.getName() + "\"," +
                        "\"vehicleType\":" + "\"" + userModelGlobal.getVehicleType() + "\"," +
                        "\"vehicleNumber\":" + "\"" + userModelGlobal.getVehicleNumber() + "\"," +
                        "\"fuelType\":" + "\"" + userModelGlobal.getFuelType() + "\"," +
                        //"\"remainFuelQuota\":" + "\"" + updFuelQuota + "\"" +"}";
                        "\"remainFuelQuota\":" + updFuelQuota +"}";
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

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Calculate the new fuel quota after fueled in the shed
     **********************************************************************************/
    public int updateFuelQuota(int originalAmount){
        return originalAmount - Integer.valueOf(fuelAmount);
    }
}