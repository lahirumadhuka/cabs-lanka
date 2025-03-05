package com.MyTransportApp.cabslanka.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnBack;
    private static int PERMISSION_CODE = 100;
    private TextView textViewPhoneNo, textViewWebsite;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_about_us);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        //Get Phone Call Permission
        if(ContextCompat.checkSelfPermission(AboutUsActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AboutUsActivity.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
        }

        textViewPhoneNo = (TextView) findViewById(R.id.textViewPhoneNo);
        textViewPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = textViewPhoneNo.getText().toString();
                Intent intentPhoneCallAppPage = new Intent(Intent.ACTION_CALL);
                intentPhoneCallAppPage.setData(Uri.parse("tel:"+phoneNo));
                startActivity(intentPhoneCallAppPage);
            }
        });
        //Cabs Lanka Website
        textViewWebsite = (TextView) findViewById(R.id.textViewWebsite);
        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String website = textViewWebsite.getText().toString();
                Uri uri = Uri.parse(website);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                Intent intentHelpPage = new Intent(this, HelpActivity.class);
                intentHelpPage.putExtra("user",user);
                startActivity(intentHelpPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }
}
