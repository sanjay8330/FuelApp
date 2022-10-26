package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class shed_registration extends AppCompatActivity implements View.OnClickListener{

    private EditText usernameTxt, passwordTxt, shedNameTxt, contactTxt, confirmPasswordTxt, locationTxt;
    private Button nextBtn;

    String addUserAPI = EndpointURL.ADD_USER;
    String addShedAPI = EndpointURL.ADD_SHED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shed_registration2);

        usernameTxt = findViewById(R.id.shedUsernameInput);
        passwordTxt = findViewById(R.id.shedPasswordInput);
        shedNameTxt = findViewById(R.id.shedNameInput);
        confirmPasswordTxt = findViewById(R.id.shedPasswordConfirmInput);
        contactTxt = findViewById(R.id.shedContactInput);
        locationTxt = findViewById(R.id.locationInput);

        nextBtn = findViewById(R.id.nxtBtn);
    }

    private static final String TAG = "SHED_REGISTRATION_LOG";

    public void addShed(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String addAPI = addShedAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(shed_registration.this, "Shed details added!", Toast.LENGTH_LONG).show();
                        addUser();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update user fuel quota details: "+error.getLocalizedMessage());
            }
        }) {

            @Override
            public byte[] getBody() {

                String body="{\"shedName\":" + "\"" + shedNameTxt.getText().toString() + "\"," +
                        "\"location\":" + "\"" + locationTxt.getText().toString() + "\"" +"}";
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


    public void addUser(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String addAPI = addUserAPI;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        Toast.makeText(shed_registration.this, "User details added!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cannot update user fuel quota details: "+error.getLocalizedMessage());
            }
        }) {

            @Override
            public byte[] getBody() {

                String body="{\"username\":" + "\"" + usernameTxt.getText().toString() + "\"," +
                        "\"password\":" + "\"" + passwordTxt.getText().toString() + "\"," +
                        "\"contact\":" + "\"" + contactTxt.getText().toString() + "\"," +
                        "\"role\":" + "\"" + "shedowner" + "\"" +"}";
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
            case R.id.nxtBtn:
                Log.e(TAG, "button clicked");

                if(!passwordTxt.getText().toString().equals(confirmPasswordTxt.getText().toString())){
                    Toast.makeText(shed_registration.this, "Passwords are mismatched", Toast.LENGTH_LONG).show();
                } else {
                    if(!usernameTxt.getText().toString().isEmpty() && !contactTxt.getText().toString().isEmpty() && !passwordTxt.getText().toString().isEmpty()
                            && !locationTxt.getText().toString().isEmpty() && !shedNameTxt.getText().toString().isEmpty()){
                        addShed();
                    }
                }

        }
    }
}