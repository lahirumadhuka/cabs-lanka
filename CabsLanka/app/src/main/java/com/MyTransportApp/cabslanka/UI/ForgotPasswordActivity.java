package com.MyTransportApp.cabslanka.UI;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.MyTransportApp.cabslanka.Database.DatabaseHelper;
import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.basgeekball.awesomevalidation.AwesomeValidation;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextEmailAddress;
    private Button btnResetPW,btnResetCancel;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_forgot_password);

        awesomeValidation = new AwesomeValidation(BASIC);

        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        // to validate the email address
        awesomeValidation.addValidation(ForgotPasswordActivity.this,R.id.editTextEmailAddress, Patterns.EMAIL_ADDRESS,R.string.err_email);

        btnResetPW = (Button) findViewById(R.id.btnResetPW);
        btnResetPW.setOnClickListener(this);
        btnResetCancel = (Button) findViewById(R.id.btnResetCancel);
        btnResetCancel.setOnClickListener(this);
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnResetPW:
                if(awesomeValidation.validate()){
                    DatabaseHelper databaseHelper = new DatabaseHelper(this);
                    String email = editTextEmailAddress.getText().toString();

                    User user = databaseHelper.checkEmail(email);
                    if(user!=null){
                        Intent intentResetPasswordPage = new Intent(this, ResetPasswordActivity.class);
                        intentResetPasswordPage.putExtra("email",email);
                        startActivity(intentResetPasswordPage);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else{
                        Toast.makeText(getApplicationContext(),"⚠️Invalid login email address",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnResetCancel:
                Intent intentMainPage = new Intent(this, MainActivity.class);
                startActivity(intentMainPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }
}