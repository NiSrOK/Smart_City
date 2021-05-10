package com.example.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceSelect extends AppCompatActivity implements View.OnClickListener {

    Button service_1;
    Button service_2;
    Button service_3;

    private String LATITUDE, LONGITUDE,EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_select);
        service_1 = findViewById(R.id.service_1);
        service_1.setOnClickListener(this);
        service_2 = findViewById(R.id.service_2);
        service_2.setOnClickListener(this);
        service_3 = findViewById(R.id.service_3);
        service_3.setOnClickListener(this);

        final Intent intent = getIntent();
        LATITUDE = intent.getStringExtra("lati");
        LONGITUDE = intent.getStringExtra("long");
        EMAIL = intent.getStringExtra("userEmail");
    }

    private void startDescription(String serviceType){
        Intent intent = new Intent(this, Description.class);
        intent.putExtra("serv", serviceType);
        intent.putExtra("lat", LATITUDE);
        intent.putExtra("lon", LONGITUDE);
        intent.putExtra("userEmail", EMAIL);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.service_1) {
            startDescription(getString(R.string.service1));
        }

        if (v.getId() == R.id.service_2) {
            startDescription(getString(R.string.service2));
        }

        if (v.getId() == R.id.service_3) {
            startDescription(getString(R.string.service3));
        }
    }
}
