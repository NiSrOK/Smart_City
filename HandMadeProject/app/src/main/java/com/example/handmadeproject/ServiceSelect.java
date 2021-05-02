package com.example.handmadeproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.provider.MediaStore.MediaColumns.RELATIVE_PATH;
import static android.provider.MediaStore.Video.VideoColumns.LATITUDE;
import static android.provider.MediaStore.Video.VideoColumns.LONGITUDE;

public class ServiceSelect extends AppCompatActivity implements View.OnClickListener{

    Button service_1;
    Button service_2;
    Button service_3;

    String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////
        if (SettingsActivity.mode=="dark"){
            setTheme(R.style.DarkThemeMedium);}
        else{
            setTheme(R.style.LightThemeMedium);
        }
        ////////
        setContentView(R.layout.activity_service_select);
        service_1 = findViewById(R.id.service_1);
        service_1.setOnClickListener(this);
        service_2 = findViewById(R.id.service_2);
        service_2.setOnClickListener(this);
        service_3 = findViewById(R.id.service_3);
        service_3.setOnClickListener(this);

        final Intent intent = getIntent();
        final String LATITUDE = intent.getStringExtra("lati");
        final String LONGITUDE = intent.getStringExtra("long");

        lat = LATITUDE;
        lon = LONGITUDE;

    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.service_1) {

            Intent intent = new Intent(this, Discription.class);
            intent.putExtra("serv", "#служба1");
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.service_2) {
            Intent intent = new Intent(this, Discription.class);
            intent.putExtra("serv", "#служба2");
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.service_3) {
            Intent intent = new Intent(this, Discription.class);
            intent.putExtra("serv", "#служба3");
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
            finish();
        }
    }
}
