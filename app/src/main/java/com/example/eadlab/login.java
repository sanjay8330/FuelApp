package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameInput, passwordInput;
    private Button loginBtn, registerBtn;

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

    }
}