package com.example.handmadeproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button acceptButton;
    TextView textStatus;
    public static String mode;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_MODE = "stock";
    private SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ////////////
        if (mode=="dark"){
            setTheme(R.style.DarkThemeMedium);
            //Toast.makeText(this, "Устанавливаю темную", Toast.LENGTH_SHORT).show();
            }
        else{
            setTheme(R.style.LightThemeMedium);
            //textStatus.setText("@string/off");
        }
        ////////
        setContentView(R.layout.settings_activity);
        textStatus = findViewById(R.id.status);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        acceptButton= findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(this);

        ////////////
        if (mode=="dark"){
            textStatus.setText(R.string.on);
            acceptButton.setText(R.string.offDark);
            textStatus.setTextColor(getResources().getColor(R.color.Green));

        }
        else{
            textStatus.setText(R.string.off);
            acceptButton.setText(R.string.onDark);
            textStatus.setTextColor(getResources().getColor(R.color.Red));
        }
        ////////

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_MODE)) {
            // Получаем число из настроек
            mode = mSettings.getString(APP_PREFERENCES_MODE, "light");
            SharedPreferences.Editor editor = mSettings.edit();
            editor.clear();
            editor.apply();
            //Toast.makeText(this, "Прочитал "+ mode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_MODE, mode);
        editor.apply();
        //Toast.makeText(this, "Записал "+ mode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.acceptButton ){

            if (mode == "dark"){
                mode = "light";
                //Toast.makeText(this, "Включил светлую", Toast.LENGTH_SHORT).show();

            }
            else {
                mode = "dark";
                //Toast.makeText(this, "Включил темную", Toast.LENGTH_SHORT).show();

            }

            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }
    }

}