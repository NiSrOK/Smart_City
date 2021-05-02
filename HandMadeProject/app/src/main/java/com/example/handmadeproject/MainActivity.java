package com.example.handmadeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton chatButton;
    FloatingActionButton mapButton;
    FloatingActionButton settingsButton;
    WebView web;
    private static final int SIGN_IN_REQUEST_CODE = 1;
    public String mainTheme = SettingsActivity.mode;




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
        setContentView(R.layout.activity_main);

        chatButton= findViewById(R.id.open_chat);
        chatButton.setOnClickListener(this);


        mapButton= findViewById(R.id.open_map);
        mapButton.setOnClickListener(this);

        settingsButton= findViewById(R.id.open_settings);
        settingsButton.setOnClickListener(this);


        //отображение сайта
        web = findViewById(R.id.web);
        web.loadUrl("https://www.vgoroden.ru/");
        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        };

        web.setWebViewClient(webViewClient);
       // onBackPressed();


        //проверка идентификации
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
            //displayAllMessages();
        }

    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mainTheme!=SettingsActivity.mode){
            mainTheme = SettingsActivity.mode;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            //Toast.makeText(this, "Старт", Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //кнопка выхода с аккаунта
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();
                            startActivityForResult(
                                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                                    SIGN_IN_REQUEST_CODE
                            );
                        }
                    });
        }
        return true;
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    //окончание регистрации
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        }
    }
}
