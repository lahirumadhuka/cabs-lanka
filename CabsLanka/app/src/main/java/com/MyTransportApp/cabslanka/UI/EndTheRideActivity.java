package com.MyTransportApp.cabslanka.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;

import java.util.Locale;

public class EndTheRideActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textViewRateCount, textViewYourRatings, textViewPickUp, textViewDrop, textViewPrice, textViewPaymentMethod;
    private Button btnSubmit;
    private RatingBar ratingBar;
    private float rateValue;
    private Dialog dialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_end_the_ride);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Get location values from previous page
        String pick = getIntent().getStringExtra("keypickcity");
        String drop = getIntent().getStringExtra("keydrop");
        String price = getIntent().getStringExtra("price");
        String clickValuePaymentMethod = getIntent().getStringExtra("clickValuePaymentMethod");

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_end_the_ride);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        Button btnDialogDone = dialog.findViewById(R.id.btnDialogDone);
        btnDialogDone.setOnClickListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        textViewRateCount = (TextView) findViewById(R.id.textViewRateCount);
        textViewPickUp = (TextView) findViewById(R.id.textViewPickUp);
        textViewPickUp.setText("From : "+pick.toUpperCase());
        textViewDrop = (TextView) findViewById(R.id.textViewDrop);
        textViewDrop.setText("To : "+drop.toUpperCase());
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewPrice.setText("Price : "+price);
        textViewPaymentMethod = (TextView) findViewById(R.id.textViewPaymentMethod);
        textViewPaymentMethod.setText("Payment Method : "+clickValuePaymentMethod);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

                if(rateValue<=1 && rateValue>0){
                    textViewRateCount.setText("Bad "+rateValue+"/5");
                }else if(rateValue<=2 && rateValue>1){
                    textViewRateCount.setText("OK "+rateValue+"/5");
                }else if(rateValue<=3 && rateValue>2){
                    textViewRateCount.setText("Good "+rateValue+"/5");
                }else if(rateValue<=4 && rateValue>3){
                    textViewRateCount.setText("Very Good "+rateValue+"/5");
                }else if(rateValue<=5 && rateValue>4){
                    textViewRateCount.setText("Best "+rateValue+"/5");
                }
            }
        });
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSubmit:
                String yourRatings = textViewRateCount.getText().toString();
                textViewYourRatings = dialog.findViewById(R.id.textViewYourRatings);
                textViewYourRatings.setText("Your Ratings : "+yourRatings);
                dialog.show(); // Showing the dialog here
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
