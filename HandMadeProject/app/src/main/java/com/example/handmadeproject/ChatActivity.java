package com.example.handmadeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.text.format.DateFormat;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class ChatActivity extends AppCompatActivity{

    private static final String TAG = "TEST";
    FirebaseListAdapter<Message> adapter;
    EditText inputSearch;
    ArrayList<Message> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////
        if (SettingsActivity.mode == "dark") {
            setTheme(R.style.DarkThemeMedium);
        } else {
            setTheme(R.style.LightThemeMedium);
        }
        ////////
        setContentView(R.layout.activity_chat);
        inputSearch = findViewById(R.id.inputSearch);
        arrayList = new ArrayList<>();
        ListView list = findViewById(R.id.list_of_messages);
        displayAllMessages();
        //автоскролл
        list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

    }


    //в названии метода и так понятно что он делает
    private void displayAllMessages() {
        final ListView listOfMessages = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.item_message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {

                TextView mess_user, mess_time, mess_text, mess_lat, mess_lon, mess_serv, mess_url;
                ImageView mess_pict;

                Message i = (Message) listOfMessages.getItemAtPosition(position);

                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);
                mess_lat = v.findViewById(R.id.message_lat);
                mess_lon = v.findViewById(R.id.message_lon);
                mess_serv = v.findViewById(R.id.message_serv);
                mess_url = v.findViewById(R.id.message_url);
                mess_pict = v.findViewById(R.id.message_photo);

                mess_user.setText(model.getUserName());
                mess_text.setText(model.getTextMessage());
                mess_lat.setText(model.getLatitude());
                mess_lon.setText(model.getLongitude());
                mess_serv.setText(model.getServ());
                mess_url.setText(model.getUrl());
                mess_time.setText(DateFormat.format("dd.MM HH:mm", model.getMessageTime()));
                Picasso.get().load(model.getUrl()).into(mess_pict);

            }
        };


        //установка маркера по координатам
        listOfMessages.setAdapter(adapter);
        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message i = (Message) listOfMessages.getItemAtPosition(position);
                Intent intent = new Intent(ChatActivity.this, MapActivity.class);
                intent.putExtra("latitude", i.latitude);
                intent.putExtra("longitude", i.longitude);
                intent.putExtra("discription", i.textMessage);
                startActivity(intent);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return  true;
    }
}
