package com.example.eadlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueueDetails extends AppCompatActivity {

    private TextView txtView_updVehCount, txtView_remAmount, txtView_waitTime;
    private Button btn_exit, btn_fueled;

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

        //Add a new record in queue details

        //Show the remaining fuel details
    }
}