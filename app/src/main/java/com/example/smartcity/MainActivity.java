package com.example.smartcity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
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

    FloatingActionButton chatButton;
    FloatingActionButton mapButton;
    FloatingActionButton settingsButton;
    WebView web;
    public static final int SIGN_IN_REQUEST_CODE = 1;

    private static final String TAG = "MyApp";

    private String EMAIL;

    private FirebaseAuth mAuth;

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

        /*if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
                    //AuthUI.getInstance().createSignInIntentBuilder().build(),
                    //SIGN_IN_REQUEST_CODE
        } else {
            Toast.makeText(this,
                    getString(R.string.welcome) + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail(),
                    Toast.LENGTH_LONG)
                    .show();
        }*/



    }

    public void checkUserAuthentication(){

        if(getIntent().getExtras() != null){
            Bundle arguments = getIntent().getExtras();
            EMAIL = arguments.get("userEmail").toString();
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
        super.onBackPressed();
    }

    //менюшка
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.open_chat) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.open_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.open_settings) {
            Toast.makeText(settingsButton.getContext(), getString(R.string.sign_out_warning), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        //Аутентификация
        Log.e(TAG, "Проверка аутентификации в onStart");
        checkUserAuthentication();
    }
}