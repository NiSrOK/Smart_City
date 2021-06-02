package com.example.smartcity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcity.pojo.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Description extends AppCompatActivity implements View.OnClickListener {

    Button send;
    private Button choose;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    private static final int CAMERA_REQUEST_CODE = 1;
    //String urlAds = "000000000";


    //private StorageReference storage;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private static final String TAG = "DescriptionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final Intent onCreateIntent = getIntent();
        setContentView(R.layout.activity_description);

        send = findViewById(R.id.send_disk);
        choose = findViewById(R.id.btnPhoto);

        imageView = findViewById(R.id.ivPhoto);
        //storage = FirebaseStorage.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        TextView coordinates = findViewById(R.id.coordinates);
        //вывод координат в description
        coordinates.setText("Широта: " + onCreateIntent.getStringExtra("lat") + "\n" + "Долгота: " + onCreateIntent.getStringExtra("lon"));




        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

                //Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                //startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.description);
                if (textField.getText().toString().length() > 0) {
                    sendMessageWithImage();
                }
                if((textField.getText().toString().length() > 0) && (filePath == null)){
                    sendMessageWithoutImage();
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

    public void sendMessageWithoutImage() {
        Log.e(TAG, "sendMessageWithoutImage()");

        final Intent intent = getIntent();
        final String latitude = intent.getStringExtra("lat");
        final String longitude = intent.getStringExtra("lon");
        final String service = intent.getStringExtra("serv");
        final String email = intent.getStringExtra("userEmail");
        //final TextView description = findViewById(R.id.description);

        EditText textField = findViewById(R.id.description);

        FirebaseDatabase.getInstance().getReference().child("Messages").child(MD5.hash(email)).push().setValue(
                                                /*new Message(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName(),
                                                        textField.getText().toString(), LATITUDE, LONGITUDE, serv, urlAds
                                                )*/

                new Message(textField.getText().toString(), email, null, latitude, longitude, service)
        );

        Toast.makeText(Description.this, "Ваше сообщение отправлено без изображения", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(Description.this, MainActivity.class);
        finish();
    }

    public void sendMessageWithImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

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
                                    final String latitude = intent.getStringExtra("lat");
                                    final String longitude = intent.getStringExtra("lon");
                                    final String service = intent.getStringExtra("serv");
                                    final String email = intent.getStringExtra("userEmail");
                                    //final TextView description = findViewById(R.id.description);

                                    EditText textField = findViewById(R.id.description);

                                    if (textField.getText().toString().length() > 0) {
                                        String urlAds = uri.toString();
                                        //Toast.makeText(Description.this, "URL адрес " + urlAds, Toast.LENGTH_SHORT).show();


                                        FirebaseDatabase.getInstance().getReference().child("Messages").child(MD5.hash(email)).push().setValue(
                                                /*new Message(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName(),
                                                        textField.getText().toString(), LATITUDE, LONGITUDE, serv, urlAds
                                                )*/
                                                new Message(textField.getText().toString(), email, urlAds, latitude, longitude, service)
                                        );
                                    }

                                }
                            });


                            Toast.makeText(Description.this, "Ваше сообщение отправлено с изображением", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Description.this, MainActivity.class);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
    }
}
