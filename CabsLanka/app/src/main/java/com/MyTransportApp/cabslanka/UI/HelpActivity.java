package com.MyTransportApp.cabslanka.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnAboutUs;
    private BottomNavigationView bottomNavigationView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Bottom navigation bar (Home, Search, History, Help)
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_help);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    Intent intentHomePage = new Intent(this,HomeActivity.class);
                    intentHomePage.putExtra("user",user);
                    startActivity(intentHomePage);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_search:
                    Intent intentSelectLocationPage = new Intent(this,SelectLocationActivity.class);
                    intentSelectLocationPage.putExtra("user",user);
                    startActivity(intentSelectLocationPage);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_help:
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

        btnAboutUs = (Button) findViewById(R.id.btnAboutUs);
        btnAboutUs.setOnClickListener(this);
        //Video about Cabs Lanka
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.videohelp);
        videoView.start();
        //Media Controller
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }
    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAboutUs: //About Us button
                Intent intentAboutUsPage = new Intent(this, AboutUsActivity.class);
                intentAboutUsPage.putExtra("user",user);
                startActivity(intentAboutUsPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
        }
    }

}
