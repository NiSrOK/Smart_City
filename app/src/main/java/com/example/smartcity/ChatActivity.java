package com.example.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    Button sendMessage;


    private static final String TAG = "ChatActivity";

    /*public void writeNewMessage(String name, String text) {
        ChatMessage message = new ChatMessage(name, text);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        sendMessage = findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

        //mDatabase.addChildEventListener(childEventListener);

        /*FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser().getEmail())
                        );

                // Clear the input
                input.setText("");
            }
        });

        //автоскролл
        //list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //list.setStackFromBottom(true);*/

    }


    /*ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e(TAG, "onChildAdded:" + dataSnapshot.getKey());

            Object object = dataSnapshot.getValue(Object.class);
            String json = new Gson().toJson(object);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.e(TAG, "onChildAdded:" + snapshot.getKey());
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Error");
        }
    };*/

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_message) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            EditText textMessage = findViewById(R.id.text_message);
            String stringMessage = textMessage.getText().toString();
            Intent intent = getIntent();
            //ChatMessage message = new ChatMessage(intent.getStringExtra("userEmail"), stringMessage);
            String uniqueID = UUID.randomUUID().toString();
            //mDatabase.child("Messages").child("message "+uniqueID).setValue(message);


            mDatabase.child("Messages").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //Gson gson = new Gson();
                    //JSONObject json = String.valueOf(task.getResult().getValue());
                    String out = String.valueOf(task.getResult().getValue());
                    String json = new Gson().toJson(out);
                    Log.e(TAG, "Список сообщений из бд " + json, task.getException());
                    //Toast.makeText(ChatActivity.this, "Успех " + out, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    }
