package com.example.smartcity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "mySettings";
    private SharedPreferences mSettings;

    FloatingActionButton chatButton;
    FloatingActionButton mapButton;
    FloatingActionButton settingsButton;
    WebView web;

    private String EMAIL = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatButton = findViewById(R.id.open_chat);
        chatButton.setOnClickListener(this);


        mapButton = findViewById(R.id.open_map);
        mapButton.setOnClickListener(this);

        settingsButton = findViewById(R.id.open_settings);
        settingsButton.setOnClickListener(this);

        settingsButton.setOnLongClickListener(v -> {
            Toast.makeText(settingsButton.getContext(), getString(R.string.sign_out), Toast.LENGTH_LONG).show();

            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.clear();
            editor.apply();

            finish();
            return false;
        });

        web = findViewById(R.id.web);
        web.loadUrl("https://www.vgoroden.ru/");
        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        };

        web.setWebViewClient(webViewClient);
    }

    public void checkUserAuthentication(){
        Intent intent = getIntent();
        if(intent.getStringExtra("userEmail") != null){
            EMAIL = intent.getStringExtra("userEmail");
        }else {
            startAuthentication();
        }
    }

    public void startAuthentication(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
            return;
        }
        this.finishAffinity();
        super.onBackPressed();
    }

    //??????????????
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.open_chat) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("userEmail", EMAIL);
            startActivity(intent);
        }

        if (v.getId() == R.id.open_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("userEmail", EMAIL);
            startActivity(intent);
        }

        if (v.getId() == R.id.open_settings) {
            Toast.makeText(settingsButton.getContext(), getString(R.string.sign_out_warning), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //????????????????????????????
        checkUserAuthentication();
    }
}