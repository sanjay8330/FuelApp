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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;
import com.example.eadlab.Model.FuelModel;
import com.example.eadlab.Model.ShedModel;
import com.example.eadlab.Model.UserModel;
import com.example.eadlab.Model.UserModel2;
import com.example.eadlab.Wrapper.SpinnerWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shed_Owner_Homepage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    EditText name, mobile_number, petrol_shed_name, location, litres_arrived, litres_remaining, vehicle_count;
    TextView label_litres_arr, label_litres_rem, label_veh_count;
    Button add_fuel_details;
    Spinner fuel_type_spinner;

    String user_id, shed_id, shed_name;
    SpinnerWrapper selectedFuelType;

    String Shed_owner_DetailAPI = EndpointURL.GET_USER_BY_ID;
    String Shed_DetailAPI = EndpointURL.GET_SHED_BY_ID;
    String getFuelAPI = EndpointURL.GET_FUEL_BY_SHEDNAME_FUELTYPE;

    List<SpinnerWrapper> fuelTypes;


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

        fuelTypes = new ArrayList<>();
        fuelTypes.add(new SpinnerWrapper("01", "Select a Fuel Type"));
        fuelTypes.add(new SpinnerWrapper("petrol92", "Petrol 92 - Normal"));
        fuelTypes.add(new SpinnerWrapper("petrol95", "Petrol 95 - Super"));
        fuelTypes.add(new SpinnerWrapper("diesel92", "Diesel 92 - Normal"));
        fuelTypes.add(new SpinnerWrapper("diesel95", "Diesel 95 - Super"));
        ArrayAdapter<SpinnerWrapper> spinnerArrayAdapter = new ArrayAdapter<SpinnerWrapper>(
                Shed_Owner_Homepage.this, android.R.layout.simple_spinner_dropdown_item, fuelTypes);
        fuel_type_spinner.setAdapter(spinnerArrayAdapter);

        user_id = "634ebb895e2da36177ba8644";
        shed_id = "634ebbe45e2da36177ba8646";

        getsShed_Owner_Details(user_id);

        fuel_type_spinner.setOnItemSelectedListener(this);
        add_fuel_details.setOnClickListener(this);

    }
    public  void hideUiElements(){
        label_litres_arr.setVisibility(TextView.INVISIBLE);
        label_litres_rem.setVisibility(TextView.INVISIBLE);
        label_veh_count.setVisibility(TextView.INVISIBLE);

        litres_arrived.setVisibility(EditText.INVISIBLE);
        litres_remaining.setVisibility(EditText.INVISIBLE);
        vehicle_count.setVisibility(EditText.INVISIBLE);


    }

    public  void showUiElements(){
        label_litres_arr.setVisibility(TextView.VISIBLE);
        label_litres_rem.setVisibility(TextView.VISIBLE);

        litres_arrived.setVisibility(TextView.VISIBLE);
        litres_remaining.setVisibility(TextView.VISIBLE);

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
                            UserModel2 userModel = new UserModel2(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("username"),
                                    jsonObject.getString("password"),
                                    jsonObject.getString("mobile"),
                                    jsonObject.getString("userrole")
                            );

                            name.setText(userModel.getUsername());
                            mobile_number.setText(userModel.getContact());
                            getsShed_Details(shed_id);

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

    public void getsShed_Details(String shed_id){
        RequestQueue queue = Volley.newRequestQueue(this);

        String Get_shed_api = Shed_DetailAPI + shed_id.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Get_shed_api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            //JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            ShedModel shedModel = new ShedModel(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("shedName"),
                                    jsonObject.getString("location")
                            );


                            petrol_shed_name.setText(shedModel.getShedName());
                            location.setText(shedModel.getLocation());
                            shed_name = shedModel.getShedName();

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

                                litres_arrived.setText(fuelModel.getArrivedLitres());
                                litres_remaining.setText(fuelModel.getRemainLitres());

                                showUiElements();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());

                hideUiElements();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_fuel:
                Intent intent = new Intent(Shed_Owner_Homepage.this, Fuel_details_Update.class);
                intent.putExtra("shedname", shed_name);
                intent.putExtra("fueltype", selectedFuelType.getId());
                startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinner:
                if(!adapterView.getItemAtPosition(i).toString().equals("Select a Fuel Type")){
                    selectedFuelType = (SpinnerWrapper) adapterView.getSelectedItem();
                    getFuelDetails(selectedFuelType.getId(), shed_name);
                }else{
                    selectedFuelType = new SpinnerWrapper();
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}