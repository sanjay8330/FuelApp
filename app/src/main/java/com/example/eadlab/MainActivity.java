package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTest;
    private Button btntest2;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.btn_hello:
                //txtHello.setText("Hello Welcome!");
                //Toast.makeText(this, "Hello button clicked", Toast.LENGTH_SHORT).show();
                //break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.btn_test);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomerHomepage.class);
                startActivity(intent);
            }
        });
    }


}