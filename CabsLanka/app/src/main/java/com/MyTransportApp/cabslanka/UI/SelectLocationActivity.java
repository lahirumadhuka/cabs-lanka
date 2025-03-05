package com.MyTransportApp.cabslanka.UI;

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
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.MyTransportApp.cabslanka.databinding.ActivitySelectLocationBinding;
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

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener, SensorEventListener {

    private GoogleMap mMap;
    private ActivitySelectLocationBinding binding;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private SearchView SearchViewDropLocation;
    private Button btnConfirm, btnBack, btnGetDirection, btnConfirmLocation;
    private Marker marker1, marker2;
    private LatLng origin, destination;
    private Polyline polyline;
    private String userLocationAddress, userLocationCity, clickValueVehicleType, clickValuePaymentMethod;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Dialog dialog;
    private int total;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Accelerometer Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        binding = ActivitySelectLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_select_location);
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
        //Radio buttons
        //Vehicle type
        RadioButton radioButtonCar = (RadioButton) dialog.findViewById(R.id.radioButtonCar);
        radioButtonCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValueVehicleType = "Car";
            }
        });
        RadioButton radioButtonThreeWheeler = (RadioButton) dialog.findViewById(R.id.radioButtonThreeWheeler);
        radioButtonThreeWheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValueVehicleType = "Three Wheeler";
            }
        });
        //Payment method
        RadioButton radioButtonCash = (RadioButton) dialog.findViewById(R.id.radioButtonCash);
        radioButtonCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValuePaymentMethod = "Cash";
            }
        });
        RadioButton radioButtonCard = (RadioButton) dialog.findViewById(R.id.radioButtonCard);
        radioButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValuePaymentMethod = "Card";
            }
        });

        SearchViewDropLocation = (SearchView) findViewById(R.id.SearchViewDropLocation);

        btnGetDirection = (Button) findViewById(R.id.btnGetDirection);
        btnGetDirection.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"⚠️Search bar is empty",Toast.LENGTH_SHORT).show();
            }
        });

        //Get the location from search bar
        SearchViewDropLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = SearchViewDropLocation.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    try {
                        // on below line we are creating and initializing a geo coder.
                        Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
                        try {
                            // on below line we are getting location from the
                            // location name and adding that location to address list.
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // on below line we are getting the location
                        // from our list a first position.
                        Address address = addressList.get(0);

                        // on below line we are creating a variable for our location
                        // where we will add our locations latitude and longitude.
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        // Add or update marker
                        if (marker1 == null) {
                            marker1 = mMap.addMarker(new MarkerOptions().position(latLng).title("Drop"));
                        } else {
                            if(polyline==null){
                                marker1.setPosition(latLng);
                            }else{
                                //Remove polyline
                                polyline.remove();
                                marker1.setPosition(latLng);
                            }
                        }
                        // below line is to animate camera to that position.
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        //Confirm button after the drop location is selected
                        btnConfirmLocation = (Button) findViewById(R.id.btnConfirm);
                        btnConfirmLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String drop = SearchViewDropLocation.getQuery().toString();
                                if(drop.isEmpty() || drop == null){
                                    Toast.makeText(getApplicationContext(),"⚠️Search bar is empty",Toast.LENGTH_SHORT).show();
                                }else{
                                    String pick = userLocationCity;
                                    TextView from = dialog.findViewById(R.id.textViewFrom);
                                    TextView to = dialog.findViewById(R.id.textViewTo);
                                    TextView price = dialog.findViewById(R.id.textViewPrice);

                                    from.setText("From : "+pick.toUpperCase());
                                    to.setText("To : "+drop.toUpperCase());
                                    //Price calculation
                                    int startFee = 80;
                                    int kmFee = 50;
                                    int distance = 1;
                                    total = startFee+(kmFee*distance);
                                    price.setText("Price : Rs. "+total);

                                    dialog.show(); // Showing the dialog here
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(marker1!=null){
                            Toast.makeText(getApplicationContext(),"⚠️Result is not found",Toast.LENGTH_SHORT).show();
                            btnConfirmLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(),"⚠️Search the valid location",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(getApplicationContext(),"⚠️Result is not found",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (isLocationPermissionGranted()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        } else {
            //Request location permission
            requestLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Request runtime permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getUserLocation();
        }
    }

    //Check the location PERMISSION_GRANTED or not
    private boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //Request location permission
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_CODE);
    }
    //User Location Method
    private void getUserLocation(){
        //Get User Location
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                origin = new LatLng(location.getLatitude(), location.getLongitude());
                //Get String value of user current location
                try {
                    Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    userLocationAddress = addressList.get(0).getAddressLine(0);
                    userLocationCity = addressList.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(marker2==null){
                    marker2 = mMap.addMarker(new MarkerOptions().position(origin).title("Pick")
                            .icon(setIcon(SelectLocationActivity.this, R.drawable.ic_baseline_man_24)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15.0f));
                }else{
                    marker2.setPosition(origin);
                }
            }
        });
    }

    //Polyline method origin and destination
    private void setPolyline(){
        // Get the origin and destination locations from somewhere
        origin = marker2.getPosition();
        destination = marker1.getPosition();

        // Add a polyline between the origin and destination locations
        PolylineOptions options = new PolylineOptions()
                .add(origin, destination)
                .width(8)
                .color(Color.BLUE);
        polyline = mMap.addPolyline(options);

        // Move the camera to show the entire polyline
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    // create a custom bitmap
    public BitmapDescriptor setIcon(Activity context,int drawableID){
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
    //Use switch statement for button navigation
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnGetDirection:
                // Add or update polyline
                if (polyline == null) {
                    if(marker1==null){
                        return;
                    }else{
                        //setPolyline Method
                        setPolyline();
                    }
                } else {
                    //setPolyline Method
                    setPolyline();
                }
                break;
            case R.id.btnBack:
                Intent intentHomePage = new Intent(this, HomeActivity.class);
                intentHomePage.putExtra("user",user);
                startActivity(intentHomePage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;

            case R.id.btnDialogConfirm: //Dialog button
                if((clickValueVehicleType=="Car"||clickValueVehicleType=="Three Wheeler")
                        && (clickValuePaymentMethod=="Cash"||clickValuePaymentMethod=="Card")){
                    Intent intentConfirmationPage = new Intent(this, ConfirmationActivity.class);
                    //Pass next page pick & drop location value
                    String pick = userLocationAddress;
                    intentConfirmationPage.putExtra("keypick",pick);
                    String drop = SearchViewDropLocation.getQuery().toString();
                    intentConfirmationPage.putExtra("keydrop",drop);
                    String pickCity = userLocationCity;
                    intentConfirmationPage.putExtra("keypickcity",pickCity);
                    String price = String.valueOf(total);
                    intentConfirmationPage.putExtra("price",price);
                    intentConfirmationPage.putExtra("clickValuePaymentMethod",clickValuePaymentMethod);
                    intentConfirmationPage.putExtra("user",user);

                    startActivity(intentConfirmationPage);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnDialogCancel: //Dialog button
                dialog.dismiss();
                break;
        }
    }
}