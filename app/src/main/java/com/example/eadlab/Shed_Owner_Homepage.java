package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Shed_Owner_Homepage extends AppCompatActivity {

    EditText name, mobile_number, petrol_shed_name, location, litres_arrived, litres_remaining, vehicle_count;
    TextView label_litres_arr, label_litres_rem, label_veh_count;
    Button add_fuel_details;
    Spinner fuel_type_spinner;

    String user_id;

    String Shed_owner_DetailAPI = EndpointURL.GET_USER_BY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_owner_homepage);

        //Get the Id of elements
        name = findViewById(R.id.name);
        mobile_number = findViewById(R.id.mobile_number);
        petrol_shed_name = findViewById(R.id.petrol_shed_name);
        location = findViewById(R.id.location);
        litres_arrived = findViewById(R.id.litres_arr);
        litres_remaining = findViewById(R.id.litres_rem);
        vehicle_count = findViewById(R.id.vehicle_count);

        label_litres_arr = findViewById(R.id.label_litres_arr);
        label_litres_rem = findViewById(R.id.label_litres_rem);
        label_veh_count = findViewById(R.id.label_veh_count);

        add_fuel_details = findViewById(R.id.btn_add_fuel);
        fuel_type_spinner = findViewById(R.id.spinner);

        hideUiElements();

        user_id = "634ebb895e2da36177ba8644";
    }
    public  void hideUiElements(){
        label_litres_arr.setVisibility(TextView.INVISIBLE);
        label_litres_rem.setVisibility(TextView.INVISIBLE);
        label_veh_count.setVisibility(TextView.INVISIBLE);

        litres_arrived.setVisibility(TextView.INVISIBLE);
        litres_remaining.setVisibility(TextView.INVISIBLE);
        vehicle_count.setVisibility(TextView.INVISIBLE);
    }

    public  void showUiElements(){
        label_litres_arr.setVisibility(TextView.VISIBLE);
        label_litres_rem.setVisibility(TextView.VISIBLE);
        label_veh_count.setVisibility(TextView.VISIBLE);

        litres_arrived.setVisibility(TextView.VISIBLE);
        litres_remaining.setVisibility(TextView.VISIBLE);
        vehicle_count.setVisibility(TextView.VISIBLE);
    }

    private static final String TAG = "SHED_OWNER_HOMEPAGE_LOG";

    /**********************************************************************************
     * @Developer   :   Sanjay Sakthivel (IT19158228)
     * @Purpose     :   Get the details of logged in user
     * @MethodType  :   API CallOut
     **********************************************************************************/
    public void getsShed_Owner_Details(String userId){
        RequestQueue queue = Volley.newRequestQueue(this);

        String getUserAPI = Shed_owner_DetailAPI + userId.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUserAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            //JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
//                            UserModel userModel = new UserModel(
//                                    jsonObject.getString("id"),
//                                    jsonObject.getString("name"),
//                                    jsonObject.getString("vehicleType"),
//                            );
//
//                            name.setText(userModel.getVehicleNumber());
//                            mobile_number.setText(userModel.getVehicleType());

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