package com.MyTransportApp.cabslanka.UI;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReportDriverActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnConfirm, btnBack;
    private User user;
    private Dialog dialog;
    private static int PERMISSION_CODE = 100;
    private int clickValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_report_driver);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_report_driver);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        Button btnDialogDone = dialog.findViewById(R.id.btnDialogDone);
        btnDialogDone.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        RadioButton radioButtonSOS = (RadioButton) findViewById(R.id.radioButtonSOS);
        radioButtonSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValue = 1;
            }
        });

        //Get Message Permission
        if(ContextCompat.checkSelfPermission(ReportDriverActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ReportDriverActivity.this,new String[]{Manifest.permission.SEND_SMS},PERMISSION_CODE);
        }
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
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
                intentDriverProfilePage.putExtra("user",user);
                intentDriverProfilePage.putExtra("clickValuePaymentMethod",clickValuePaymentMethod);
                startActivity(intentDriverProfilePage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.btnConfirm:
                if(clickValue==1){
                    String nearestPolicePhoneNo = "0763471452";
                    String message = "SOS";

                    //Initialize SMS Manager
                    SmsManager smsManager = SmsManager.getDefault();
                    //Send message
                    smsManager.sendTextMessage(nearestPolicePhoneNo,null,message,null,null);
                    //Display Toast message
                    Toast.makeText(getApplicationContext(),"SOS SMS sent successfully ✅",Toast.LENGTH_SHORT).show();

                    dialog.show(); // Showing the dialog here
                }else {
                    Toast.makeText(getApplicationContext(),"⚠️Select report type",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDialogDone: //Dialog button
                Intent intentHomePage = new Intent(this, HomeActivity.class);
                intentHomePage.putExtra("user",user);
                startActivity(intentHomePage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                dialog.dismiss();
                break;
        }
    }
}
