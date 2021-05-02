package com.example.handmadeproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class Discription extends AppCompatActivity implements View.OnClickListener  {

    Button send;
    private Button choose;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    private static final int CAMERA_REQUEST_CODE = 1;
    String urlAds = "000000000" ;



    //private StorageReference storage;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //////
        if (SettingsActivity.mode == "dark") {
            setTheme(R.style.DarkThemeMedium);
        } else {
            setTheme(R.style.LightThemeMedium);
        }
        //////
        setContentView(R.layout.activity_discription);

        send = findViewById(R.id.send_disk);
        choose = findViewById(R.id.btnPhoto);

        imageView = findViewById(R.id.ivPhoto);
        //storage = FirebaseStorage.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //получение данных с MapActivity
        final Intent intent = getIntent();
        final String LATITUDE = intent.getStringExtra("lat");
        final String LONGITUDE = intent.getStringExtra("lon");
        final String serv = intent.getStringExtra("serv");
        final TextView disk = findViewById(R.id.disk);



                choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

                //Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                //startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });



        //отправка сообщения в чат
        disk.setText("Широта: " + LATITUDE + "\n" + "Долгота: " + LONGITUDE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.discription);
                if (textField.getText().toString().length() > 0) {
                    uploadImage();

                }
            }
        });


    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ////////////////////////
                            ////////////////////////
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    /////////////// вот url
                                    final Intent intent = getIntent();
                                    final String LATITUDE = intent.getStringExtra("lat");
                                    final String LONGITUDE = intent.getStringExtra("lon");
                                    final String serv = intent.getStringExtra("serv");
                                    final TextView disk = findViewById(R.id.disk);

                                    EditText textField = findViewById(R.id.discription);
                                    if (textField.getText().toString().length() > 0) {
                                    urlAds = uri.toString();
                                    Toast.makeText(Discription.this, "URL адрес "+urlAds, Toast.LENGTH_SHORT).show();

                                        FirebaseDatabase.getInstance().getReference().push().setValue(
                                                new Message(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName(),
                                                        textField.getText().toString(), LATITUDE, LONGITUDE, serv, urlAds
                                                )
                                        );
                                    }
                                    ///////////////////////
                                }
                            });
                            /////////////////////////////
                            /////////////////////////////

                            Toast.makeText(Discription.this, "Ваше сообщение отправлено", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Discription.this, MainActivity.class);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Discription.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {

    }
}