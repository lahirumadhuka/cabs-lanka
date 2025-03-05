package com.MyTransportApp.cabslanka.UI;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.MyTransportApp.cabslanka.databinding.ActivityConfirmationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class ConfirmationActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener, SensorEventListener {

    private GoogleMap mMap;
    private ActivityConfirmationBinding binding;
    private Button btnCancelRide, btnDriverDetails;
    private TextView textViewTitle;
    private Polyline polyline;
    private List<Address> listGeoCoder,listGeoCoder2,listGeoCoder3;
    private double longitude,latitude,longitude2,latitude2;
    private LatLng pickLocation,dropLocation;
    private Marker driverMarker,pickMarker,dropMarker;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Dialog dialog,dialogEndRide;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Accelerometer Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_confirmation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        Button btnDialogConfirm = dialog.findViewById(R.id.btnDialogConfirm);
        btnDialogConfirm.setOnClickListener(this);
        Button btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        btnDialogCancel.setOnClickListener(this);

        //Create the Ride is complete Dialog here
        dialogEndRide = new Dialog(this);
        dialogEndRide.setContentView(R.layout.custom_dialog_ride_complete);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogEndRide.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialogEndRide.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogEndRide.setCancelable(false); //Optional
        dialogEndRide.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        Button btnDialogOk = dialogEndRide.findViewById(R.id.btnDialogOk);
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEndTheRidePage = new Intent(ConfirmationActivity.this, EndTheRideActivity.class);
                //Get location values from previous page
                String pickCity = getIntent().getStringExtra("keypickcity");
                String drop = getIntent().getStringExtra("keydrop");
                String price = getIntent().getStringExtra("price");
                String clickValuePaymentMethod = getIntent().getStringExtra("clickValuePaymentMethod");

                intentEndTheRidePage.putExtra("keypickcity",pickCity);
                intentEndTheRidePage.putExtra("keydrop",drop);
                intentEndTheRidePage.putExtra("price",price);
                intentEndTheRidePage.putExtra("clickValuePaymentMethod",clickValuePaymentMethod);
                intentEndTheRidePage.putExtra("user",user);
                startActivity(intentEndTheRidePage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                dialog.dismiss();
            }
        });

        btnCancelRide = (Button) findViewById(R.id.btnCancelRide);
        btnCancelRide.setOnClickListener(this);

        btnDriverDetails = (Button) findViewById(R.id.btnDriverDetails);
        btnDriverDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"⚠️Driver is not found",Toast.LENGTH_SHORT).show();
            }
        });

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);

        //Get location values from previous page
        String pick = getIntent().getStringExtra("keypick");
        String drop = getIntent().getStringExtra("keydrop");

        try{
            listGeoCoder = new Geocoder(this).getFromLocationName(pick,1);
            listGeoCoder2 = new Geocoder(this).getFromLocationName(drop,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Get Pick Location Longitude, Latitude
        longitude = listGeoCoder.get(0).getLongitude();
        latitude = listGeoCoder.get(0).getLatitude();

        longitude2 = listGeoCoder2.get(0).getLongitude();
        latitude2 = listGeoCoder2.get(0).getLatitude();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Get pick location marker
        pickLocation = new LatLng(latitude,longitude);
        pickMarker = mMap.addMarker(new MarkerOptions().position(pickLocation).title("Pick")
                .icon(setIcon(ConfirmationActivity.this,R.drawable.ic_baseline_man_24)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickLocation,15.0f));

        //Driver is arriving (delayed method)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Set Text
                textViewTitle.setText("Driver is arriving now");
                // Add a driver marker
                LatLng driver = new LatLng(6.828792963652233, 79.8728220294724);
                driverMarker = mMap.addMarker(new MarkerOptions().position(driver).title("Driver")
                        .icon(setIcon(ConfirmationActivity.this,R.drawable.car_marker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driver,15.0f));

                // Add a polyline between the driver and pick locations
                PolylineOptions options = new PolylineOptions()
                        .add(driver, pickLocation)
                        .width(8)
                        .color(Color.RED);
                polyline = mMap.addPolyline(options);

                // Move the camera to show the entire polyline
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(driver);
                builder.include(pickLocation);
                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                btnDriverDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        driverDetails();
                    }
                });
            }
        },10000);

        //Start Ride (delayed method)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Remove polyline and driver marker
                polyline.remove();
                driverMarker.remove();
                //Set Text
                textViewTitle.setText("Driver is going to the destination");
                // Set Driver marker
                pickMarker.setTitle("Driver");
                pickMarker.setIcon(setIcon(ConfirmationActivity.this,R.drawable.car_marker));
                // Get drop location marker
                dropLocation = new LatLng(latitude2, longitude2);
                dropMarker = mMap.addMarker(new MarkerOptions().position(dropLocation).title("Drop"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dropLocation,15.0f));

                // Add a polyline between the origin and destination locations
                PolylineOptions options = new PolylineOptions()
                        .add(pickLocation, dropLocation)
                        .width(8)
                        .color(Color.BLUE);
                polyline = mMap.addPolyline(options);

                // Move the camera to show the entire polyline
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(pickLocation);
                builder.include(dropLocation);
                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                btnDriverDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        driverDetails();
                    }
                });
            }
        },28000);

        //End Ride (delayed method)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Remove polyline and driver marker
                polyline.remove();
                pickMarker.remove();
                //Set Text
                textViewTitle.setText("Driver arrived to the destination");
                // Set Driver marker
                dropLocation = new LatLng(latitude2, longitude2);
                dropMarker = mMap.addMarker(new MarkerOptions().position(dropLocation).title("drop")
                        .icon(setIcon(ConfirmationActivity.this,R.drawable.car_marker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dropLocation,15.0f));

                btnDriverDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        driverDetails();
                    }
                });
            }
        },46000);

        //Finish Ride and move on to the next page (delayed method)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogEndRide.show(); // Showing the dialog here

                btnDriverDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        driverDetails();
                    }
                });
            }
        },52000);

        //Request runtime permissions
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
    }
    // create a custom bitmap
    public BitmapDescriptor setIcon(Activity context, int drawableID){
        Drawable drawable = ActivityCompat.getDrawable(context,drawableID);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Sensors
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // Use x, y, z values to determine the device's orientation and location
            Log.d("SENSOR", "X: " + x + ", Y: " + y + ", Z: " + z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    //Driver Profile Page
    public void driverDetails(){
        Intent intentDriverProfilePage = new Intent(this, DriverProfileActivity.class);
        //Get location values from previous page
        String pick = getIntent().getStringExtra("keypick");
        String drop = getIntent().getStringExtra("keydrop");
        String pickCity = getIntent().getStringExtra("keypickcity");
        String price = getIntent().getStringExtra("price");
        String clickValuePaymentMethod = getIntent().getStringExtra("clickValuePaymentMethod");

        intentDriverProfilePage.putExtra("keypick",pick);
        intentDriverProfilePage.putExtra("keydrop",drop);
        intentDriverProfilePage.putExtra("keypickcity",pickCity);
        intentDriverProfilePage.putExtra("price",price);
        intentDriverProfilePage.putExtra("clickValuePaymentMethod",clickValuePaymentMethod);
        intentDriverProfilePage.putExtra("user",user);
        startActivity(intentDriverProfilePage);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    //Use switch statement for button navigation
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnCancelRide:
                dialog.show(); // Showing the dialog here
                break;
            case R.id.btnDialogConfirm: //Dialog button
                Intent intentHomePage = new Intent(this, HomeActivity.class);
                intentHomePage.putExtra("user",user);
                startActivity(intentHomePage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                dialog.dismiss();
                break;
            case R.id.btnDialogCancel: //Dialog button
                dialog.dismiss();
                break;
        }
    }
}