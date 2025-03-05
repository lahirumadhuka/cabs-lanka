package com.MyTransportApp.cabslanka.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogout, btnEditAccount;
    private BottomNavigationView bottomNavigationView;
    private ImageView imageViewProfileImg;
    private FloatingActionButton fabImgPicker;
    private User user;
    private TextView textViewName, textViewEmail, textViewPhoneNo, textViewAddress, textViewPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_account);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(user.getName());
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewEmail.setText(user.getEmail());
        textViewPhoneNo = (TextView) findViewById(R.id.textViewPhoneNo);
        textViewPhoneNo.setText(user.getPhoneNo());
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewAddress.setText(user.getAddress());
        textViewPW = (TextView) findViewById(R.id.textViewPW);
        textViewPW.setText(user.getPassword());

        //Image Picker for changing profile picture
        imageViewProfileImg = (ImageView) findViewById(R.id.imageViewProfileImg);
        fabImgPicker = (FloatingActionButton) findViewById(R.id.fabImgPicker);

        fabImgPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ImagePicker.with(AccountActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();
            }
        });

        //Bottom navigation bar (Home, Search, History, Help)
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_account);
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
                    Intent intentHelpPage = new Intent(this,HelpActivity.class);
                    intentHelpPage.putExtra("user",user);
                    startActivity(intentHelpPage);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_account:
                    return true;
            }
            return false;
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        btnEditAccount = (Button) findViewById(R.id.btnEditAccount);
        btnEditAccount.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        imageViewProfileImg.setImageURI(uri);
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogout: //Logout Button
                Intent intentMainPage = new Intent(this,MainActivity.class);
                startActivity(intentMainPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.btnEditAccount: //Edit Account Button
                Intent intentEditAccountPage = new Intent(this, EditAccountActivity.class);
                intentEditAccountPage.putExtra("user",user);
                startActivity(intentEditAccountPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
        }
    }
}
