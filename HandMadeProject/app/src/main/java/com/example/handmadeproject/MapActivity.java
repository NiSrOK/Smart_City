package com.example.handmadeproject;

import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.model.Marker;
import android.os.Bundle;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private double lat;
    private double lon;
    private static final float DEFAULT_ZOOM = 17f;
    private static final float START_ZOOM = 11f;
    ImageButton locationButton;

    private FusedLocationProviderClient fusedLocationClient;

    public void setCoordinates(Location location) {
        lat = location.getLongitude();
        lon = location.getLatitude();
    }

    public void defaultLocation(){
            lat = 56.281228;
            lon = 43.945257;
        LatLng latLng = new LatLng(lon, lat);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        moveCamera(new LatLng(lat,lon), START_ZOOM, "My Location");
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d("qwe", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

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
        setContentView(R.layout.activity_map);

        locationButton= findViewById(R.id.current_location);
        locationButton.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //перемещение на текущую локацию
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.current_location) {
            defaultLocation();
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Location currentLocation = location;

                        if (location != null) {
                            setCoordinates(location);
                            LatLng latLng = new LatLng(lon, lat);
                            //mMap.addMarker(new MarkerOptions().position(latLng).title("Вы здесь"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");}

                    }

                });
            }
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        defaultLocation();
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //установка маркера из чата
                final Intent intent = getIntent();
                final String LATITUDE = intent.getStringExtra("latitude");
                final String LONGITUDE = intent.getStringExtra("longitude");
                final String DISCRIPTION = intent.getStringExtra("discription");
                if ((LATITUDE != null)) {
                    MarkerStart(LATITUDE, LONGITUDE, DISCRIPTION);
                } else {
                    Location currentLocation = location;
                ///////

                //перемещение на локацию устройства при первом запуске
                if (location != null) {
                    setCoordinates(location);
                    LatLng latLng = new LatLng(lon, lat);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");}
            }
        }
            });

        //установка маркера
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //переход к активити описания
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MapActivity.this, ServiceSelect.class);
                        intent.putExtra("lati" ,Double.toString(marker.getPosition().latitude));
                        intent.putExtra("long" ,Double.toString(marker.getPosition().longitude));
                        startActivity(intent);
                        mMap.setOnInfoWindowClickListener(null);
                        finish();
                    }
                });
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Нажмите чтобы описать проблему");
                mMap.addMarker(markerOptions);

            }
        });

        //перемещение маркера ТОЛКОМ НЕ ТЕСТИЛОСЬ
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

    }

    //установка маркера из чата
    public void MarkerStart(String latitude, String longitude, String diskription){
        double lati = Double.parseDouble(latitude);
        double longi = Double.parseDouble(longitude);
        LatLng latLng = new LatLng(lati, longi);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(diskription);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        moveCamera(new LatLng(lati, longi), DEFAULT_ZOOM, diskription);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapActivity.this, RdyDisk.class);
                startActivity(intent);
            }
        });
    }
}