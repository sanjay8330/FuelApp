package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eadlab.Endpoints.EndpointURL;
import com.example.eadlab.Model.QueueModel;
import com.example.eadlab.Model.UserModel;
import com.example.eadlab.Model.UserModel2;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameInput, passwordInput;
    private Button loginBtn, registerBtn;

    String getUserAPI = EndpointURL.GET_USER_BY_UN_PW;
    String getCustomerAPI = EndpointURL.GET_CUSTOMER_BY_NAME;

    String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        usernameInput = findViewById(R.id.loginUserNameInput);
        passwordInput = findViewById(R.id.loginPasswordInput);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.regBtn);

        loginBtn.setOnClickListener(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginBtn:
                if(!usernameInput.getText().toString().isEmpty() && !passwordInput.getText().toString().isEmpty()){
                    handleLogin(usernameInput.getText().toString(), passwordInput.getText().toString());
                }
        }
    }

    private static final String TAG = "LOGIN_LOG";

    public void handleLogin(String username, String password){
        RequestQueue queue = Volley.newRequestQueue(this);

        String getAPI = getUserAPI + username.trim() + "/" + password.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            if(!response.isEmpty()){
                                JSONObject jsonObject = new JSONObject(response);
                                UserModel2 userModel2 = new UserModel2(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("password"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("userrole")
                                );
                                Toast.makeText(login.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                                //getCustDetails(userModel2.getUsername());

                                if(userModel2.getRole().equals("customer")){
                                    Intent intent = new Intent(login.this, CustomerHomepage.class);
                                    intent.putExtra("userid", customerID);
                                    startActivity(intent);

                                }else if (userModel2.getRole().equals("shedowner")){
                                    Intent intent = new Intent(login.this, Shed_Owner_Homepage.class);
                                    intent.putExtra("userid", userModel2.getId());
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(login.this, "Error in login", Toast.LENGTH_LONG).show();

                                }

                            }else{
                                Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
                Toast.makeText(login.this, "Login failed!", Toast.LENGTH_LONG).show();

            }
        });

        queue.add(stringRequest);
    }

    public void getCustDetails(String name){
        RequestQueue queue = Volley.newRequestQueue(this);

        String getAPI = getCustomerAPI + name.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response);
                        try {
                            if(!response.isEmpty()){
                                JSONObject jsonObject = new JSONObject(response);
                                UserModel userModel = new UserModel(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("vehicleType"),
                                        jsonObject.getString("vehicleNumber"),
                                        jsonObject.getString("fuelType"),
                                        jsonObject.getInt("remainFuelQuota")
                                );
                                customerID = userModel.getId();

                            }else{
                                Log.e(TAG, "onErrorResponse: ");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getLocalizedMessage());
                Toast.makeText(login.this, "Login failed!", Toast.LENGTH_LONG).show();

            }
        });

        queue.add(stringRequest);
    }
}