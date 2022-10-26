package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Fuel_details_Update extends AppCompatActivity implements View.OnClickListener{

    EditText date, Fuel_type, arrival_time, litres_arriving;
    Button update_btn;

    String shed_name, fuel_type;

    String addFuelAPI = EndpointURL.ADD_FUEL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_details_update);

        //Get the Id of elements
        date = findViewById(R.id.dateid);
        Fuel_type = findViewById(R.id.fuelid);
        arrival_time = findViewById(R.id.arrivalid);
        litres_arriving = findViewById(R.id.litres_id);

        update_btn = findViewById(R.id.btn_id);


        Intent intent = getIntent();
        shed_name = intent.getStringExtra("shedname");
        fuel_type = intent.getStringExtra("fueltype");

        date.setText(this.getCurrentDate());
        Fuel_type.setText(fuel_type);
        update_btn.setOnClickListener(this);


    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

    private static final String TAG = "FUEL_DETAILS_UPDATE";

    public void addfueldetails(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String addAPI = addFuelAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(Fuel_details_Update.this, "Fuel details added!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update user fuel quota details: "+error.getLocalizedMessage());
            }
        }) {

            @Override
            public byte[] getBody() {

                String body="{\"fuelType\":" + "\"" + fuel_type + "\"," +
                        "\"shedName\":" + "\"" + shed_name + "\"," +
                        "\"date\":" + "\"" + date.getText().toString() + "\"," +
                        "\"arrivalTime\":" + "\"" + arrival_time.getText().toString() + "\"," +
                        "\"arrivedLitres\":" + "\"" + litres_arriving.getText().toString() + "\"," +
                        "\"remainLitres\":" + "\"" + litres_arriving.getText().toString() + "\"" +"}";
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_id:
                if (!arrival_time.getText().toString().isEmpty() && !litres_arriving.getText().toString().isEmpty()){
                    addfueldetails();
                }
                else {
                    Toast.makeText(this,"Please Fill the fields", Toast.LENGTH_LONG).show();
                }
        }
    }
}
