package com.example.smartcity;

import android.content.Intent;
import android.os.Bundle;
import com.example.smartcity.pojo.Message;

import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
//import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity{

    private static String EMAIL;
    FirebaseListAdapter<Message> adapter;
    ArrayList<Message> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Intent intent = getIntent();
        EMAIL = intent.getStringExtra("userEmail");

        arrayList = new ArrayList<>();
        ListView list = findViewById(R.id.list_of_messages);
        displayAllMessages();
        //автоскролл
        list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

    }


    private void displayAllMessages() {
        final ListView listOfMessages = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.item_message, FirebaseDatabase.getInstance().getReference().child("Messages").child(MD5.hash(EMAIL))) {
            @Override
            protected void populateView(View v, Message model, int position) {
                    TextView mess_time, mess_text, mess_lat, mess_lon, mess_serv, mess_status;
                    ImageView mess_pict;

                    mess_time = v.findViewById(R.id.message_time);
                    mess_text = v.findViewById(R.id.message_text);
                    mess_lat = v.findViewById(R.id.message_lat);
                    mess_lon = v.findViewById(R.id.message_lon);
                    mess_serv = v.findViewById(R.id.message_serv);
                    mess_status = v.findViewById(R.id.message_status);
                    mess_pict = v.findViewById(R.id.message_photo);

                    mess_status.setText(getString(R.string.status,model.getStatus()));
                    mess_text.setText(getString(R.string.message,model.getText()));
                    mess_lat.setText(model.getLatitude());
                    mess_lon.setText(model.getLongitude());
                    mess_serv.setText(model.getService());
                    mess_time.setText(DateFormat.format("dd.MM HH:mm", model.getMessageTime()));

                    if(model.getImageUrl()!=null){
                        Picasso.get().load(model.getImageUrl()).resize(1000,1000).centerInside().into(mess_pict);
                    }
                }
        };
        listOfMessages.setAdapter(adapter);
    }
}
