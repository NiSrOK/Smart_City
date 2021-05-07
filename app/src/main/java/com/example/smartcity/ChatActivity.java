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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    Button sendMessage;

    public void writeNewMessage(String name, String text) {
        ChatMessage message = new ChatMessage(name, text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        sendMessage = findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_message) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            EditText textMessage = findViewById(R.id.text_message);
            String stringMessage = textMessage.getText().toString();
            ChatMessage message = new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getEmail(), stringMessage);
            String uniqueID = UUID.randomUUID().toString();
            mDatabase.child("Messages").child("message "+uniqueID).setValue(message);

            mDatabase.child("Messages").child("message 305f77eb-1e6e-4e8f-8f23-107d4cd00233").child("messageUser").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(ChatActivity.this, "Фэйл", Toast.LENGTH_SHORT).show();
                }
                else {
                    String out = String.valueOf(task.getResult().getValue());
                    Toast.makeText(ChatActivity.this, "Успех " + out, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
