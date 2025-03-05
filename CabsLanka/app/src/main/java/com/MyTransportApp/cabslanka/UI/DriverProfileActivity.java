package com.MyTransportApp.cabslanka.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DriverProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnBack, btnCall, btnMessage, btnReport;
    private static int PERMISSION_CODE = 100;
    private TextView textViewPhoneNo;
    private User user;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_driver_profile);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_message_driver);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        Button btnDialogSend = dialog.findViewById(R.id.btnDialogSend);
        btnDialogSend.setOnClickListener(this);
        Button btnDialogClose = dialog.findViewById(R.id.btnDialogClose);
        btnDialogClose.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        textViewPhoneNo = (TextView) findViewById(R.id.textViewPhoneNo);

        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);

        btnReport = (Button) findViewById(R.id.btnReport);
        btnReport.setOnClickListener(this);

        btnMessage = (Button) findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(this);

        //Get Phone Call Permission
        if(ContextCompat.checkSelfPermission(DriverProfileActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DriverProfileActivity.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
        }
        //Get Message Permission
        if(ContextCompat.checkSelfPermission(DriverProfileActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DriverProfileActivity.this,new String[]{Manifest.permission.SEND_SMS},PERMISSION_CODE);
        }
    }

    //Use switch statement for button navigation
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnBack:
                Intent intentConfirmationPage = new Intent(this, ConfirmationActivity.class);
                //Get location values from previous page
                String pick = getIntent().getStringExtra("keypick");
                String drop = getIntent().getStringExtra("keydrop");
                String pickCity = getIntent().getStringExtra("keypickcity");
                String price = getIntent().getStringExtra("price");
                String clickValuePaymentMethod = getIntent().getStringExtra("clickValuePaymentMethod");

                intentConfirmationPage.putExtra("keypick",pick);
                intentConfirmationPage.putExtra("keydrop",drop);
                intentConfirmationPage.putExtra("keypickcity",pickCity);
                intentConfirmationPage.putExtra("price",price);
                intentConfirmationPage.putExtra("clickValuePaymentMethod",clickValuePaymentMethod);
                intentConfirmationPage.putExtra("user",user);
                startActivity(intentConfirmationPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.btnCall:
                String phoneNo = textViewPhoneNo.getText().toString();
                Intent intentPhoneCallAppPage = new Intent(Intent.ACTION_CALL);
                intentPhoneCallAppPage.setData(Uri.parse("tel:"+phoneNo));
                startActivity(intentPhoneCallAppPage);
                break;
            case R.id.btnReport:
                Intent intentReportDriverPage = new Intent(this, ReportDriverActivity.class);
                //Get location values from previous page
                pick = getIntent().getStringExtra("keypick");
                drop = getIntent().getStringExtra("keydrop");
                pickCity = getIntent().getStringExtra("keypickcity");
                price = getIntent().getStringExtra("price");
                clickValuePaymentMethod = getIntent().getStringExtra("clickValuePaymentMethod");

                intentReportDriverPage.putExtra("keypick",pick);
                intentReportDriverPage.putExtra("keydrop",drop);
                intentReportDriverPage.putExtra("keypickcity",pickCity);
                intentReportDriverPage.putExtra("price",price);
                intentReportDriverPage.putExtra("user",user);
                intentReportDriverPage.putExtra("clickValuePaymentMethod",clickValuePaymentMethod);
                startActivity(intentReportDriverPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.btnMessage:
                dialog.show(); // Showing the dialog here
                break;
            case R.id.btnDialogSend: //Dialog button
                EditText editTextSendMessage = (EditText) dialog.findViewById(R.id.editTextSendMessage);
                String driverPhoneNo = "0763471452";
                String message = editTextSendMessage.getText().toString();

                //Check condition if string is empty or not
                if(!driverPhoneNo.isEmpty() && !message.isEmpty()){
                    //Initialize SMS Manager
                    SmsManager smsManager = SmsManager.getDefault();
                    //Send message
                    smsManager.sendTextMessage(driverPhoneNo,null,message,null,null);
                    //Display Toast message
                    Toast.makeText(getApplicationContext(),"SMS sent successfully ✅",Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }else {
                    //When string is empty then display Toast message
                    Toast.makeText(getApplicationContext(),"⚠️Please enter the message",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDialogClose: //Dialog button
                dialog.dismiss();
                break;
        }
    }
}