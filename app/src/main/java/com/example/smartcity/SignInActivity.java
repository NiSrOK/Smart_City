package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    //private FirebaseAuth mAuth;

    Button signInButton;
    Button registrationButton;

    private static final String TAG = "SignInActivity";

    public static final String APP_PREFERENCES = "mySettings";
    private SharedPreferences mSettings;
    private String APP_PREFERENCES_EMAIL = "email";
    private String APP_PREFERENCES_PASSWORD = "password";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signInButton = findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(this);

        registrationButton = findViewById(R.id.buttonRegistration);
        registrationButton.setOnClickListener(this);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        /*SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.apply();*/

        if((mSettings.contains(APP_PREFERENCES_EMAIL)) && (mSettings.contains(APP_PREFERENCES_PASSWORD))) {
            Log.e(TAG, "APP_PREFERENCES_EMAIL in PREFERENCES");
            APP_PREFERENCES_EMAIL = mSettings.getString(APP_PREFERENCES_EMAIL, "email");
            APP_PREFERENCES_PASSWORD = mSettings.getString(APP_PREFERENCES_PASSWORD, "password");
            signInWithSharedPreferences(APP_PREFERENCES_EMAIL, APP_PREFERENCES_PASSWORD);
        }
    }

    /*public void signIn(View view){
        EditText editEmail=findViewById(R.id.editTextTextEmailAddress);
        String email = editEmail.getText().toString();

        EditText editPassword = findViewById(R.id.editTextTextPassword);
        String password = editPassword.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignInActivity.this, "Успешный вход",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            // Close activity
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Ошибка входа",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    /*private String toMd5(String in){
        String codedIn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            BigInteger bigInt = new BigInteger(1, digest.digest());
            codedIn = bigInt.toString(16);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return codedIn;
    }*/

    private boolean checkValidRegistrationData(String email, String password){
        String sub = "@";
        if (email.contains(sub) && password.length() > 4){
            return true;
        } else{
            return false;
        }
    }

    private void registration(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText editEmail=findViewById(R.id.editTextTextEmailAddress);
        String email = editEmail.getText().toString();


        EditText editPassword = findViewById(R.id.editTextTextPassword);
        String password = editPassword.getText().toString();

        mDatabase.child("Users").child(MD5.hash(email)).child("Password").get().addOnCompleteListener(task -> {
            if (String.valueOf(task.getResult().getValue()).equals("null") && checkValidRegistrationData(email,password)) {
                Toast.makeText(SignInActivity.this, getString(R.string.successRegistration), Toast.LENGTH_LONG).show();

                mDatabase.child("Users").child(MD5.hash(email)).child("Password").setValue(MD5.hash(password));
                mDatabase.child("Users").child(MD5.hash(email)).child("Email").setValue(email);

            }
            else {
                //String out = String.valueOf(task.getResult().getValue());
                Toast.makeText(this, getString(R.string.failRegistration), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void signInWithSharedPreferences(String email, String password){
        Toast.makeText(this, getString(R.string.sharedPreferencesSignIn) + " " + email, Toast.LENGTH_LONG).show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(MD5.hash(email)).child("Password").get().addOnCompleteListener(task -> {
            if (String.valueOf(task.getResult().getValue()).equals(MD5.hash(password))) {
                Toast.makeText(this, getString(R.string.welcome) + " " + email, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userEmail", email);
                startActivity(intent);
                // Close activity
                finish();
            }
            else {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.clear();
                editor.apply();

                //String out = String.valueOf(task.getResult().getValue());
                Toast.makeText(this, getString(R.string.failAuthentication), Toast.LENGTH_LONG).show();
                recreate();
            }
        });
    }

    private void signIn() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        String email = editEmail.getText().toString();

        EditText editPassword = findViewById(R.id.editTextTextPassword);
        String password = editPassword.getText().toString();

        mDatabase.child("Users").child(MD5.hash(email)).child("Password").get().addOnCompleteListener(task -> {
            if (String.valueOf(task.getResult().getValue()).equals(MD5.hash(password))) {

                Toast.makeText(this, getString(R.string.welcome) + " " + email, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userEmail", email);
                startActivity(intent);

                //запись почты и пароля в SharedPreferences
                Log.e(TAG, "Save preferences");
                SharedPreferences.Editor editor = mSettings.edit();
                Log.e(TAG, "password = " + password);
                editor.putString(APP_PREFERENCES_PASSWORD, password);
                Log.e(TAG, "email = " + email);
                editor.putString(APP_PREFERENCES_EMAIL, email);
                editor.apply();
                Log.e(TAG, "Email " + mSettings.getString(APP_PREFERENCES_EMAIL, ""));
                Log.e(TAG, "Password " + mSettings.getString(APP_PREFERENCES_PASSWORD, ""));
                // Close activity
                finish();

            }
            else {
                //String out = String.valueOf(task.getResult().getValue());
                Toast.makeText(this, getString(R.string.failAuthentication), Toast.LENGTH_LONG).show();
            }
        });
    }
    /*public void registration(View view){
        EditText editEmail=findViewById(R.id.editTextTextEmailAddress);
        String email = editEmail.getText().toString();

        EditText editPassword = findViewById(R.id.editTextTextPassword);
        String password = editPassword.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignInActivity.this, "Успешная регистрация",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            // Close activity
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Ошибка регистрации",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.buttonSignIn) {
            signIn();
        }
        if (view.getId() == R.id.buttonRegistration) {
            registration();
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        super.onBackPressed();
    }
}
