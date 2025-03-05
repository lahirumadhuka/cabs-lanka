package com.MyTransportApp.cabslanka.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnGetRide;
    private ImageSlider imageSlider;
    private BottomNavigationView bottomNavigationView;
    private TextView textViewGreeting;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        textViewGreeting = (TextView) findViewById(R.id.textViewGreeting);
        textViewGreeting.setText("Hello " + user.getName());

        //Bottom navigation bar (Home, Search, History, Help)
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_search:
                    Intent intentSelectLocationPage = new Intent(this,SelectLocationActivity.class);
                    intentSelectLocationPage.putExtra("user",user);
                    startActivity(intentSelectLocationPage);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_help:
                    Intent intentHelpPage = new Intent(this,HelpActivity.class);
                    intentHelpPage.putExtra("user",user);
                    startActivity(intentHelpPage);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_account:
                    Intent intentAccountPage = new Intent(this,AccountActivity.class);
                    intentAccountPage.putExtra("user",user);
                    startActivity(intentAccountPage);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        //Image slider for displaying advertisements
        imageSlider = (ImageSlider) findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.ad_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.ad_2, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        btnGetRide = (Button) findViewById(R.id.btnGetRide);
        btnGetRide.setOnClickListener(this);
    }
    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnGetRide: //Get a Ride button
                Intent intentSelectLocationPage = new Intent(this,SelectLocationActivity.class);
                intentSelectLocationPage.putExtra("user",user);
                startActivity(intentSelectLocationPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
        }
    }
}
