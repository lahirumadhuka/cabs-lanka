package com.MyTransportApp.cabslanka.UI;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MyTransportApp.cabslanka.Database.DatabaseHelper;
import com.MyTransportApp.cabslanka.R;
import com.basgeekball.awesomevalidation.AwesomeValidation;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textViewEmailAddress;
    private EditText editTextResetPassword ,editTextResetConfirmPW;
    private Button btnResetConfirm,btnResetCancel;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_reset_password);

        awesomeValidation = new AwesomeValidation(BASIC);

        textViewEmailAddress = (TextView) findViewById(R.id.textViewEmailAddress);
        textViewEmailAddress.setText(getIntent().getStringExtra("email"));

        editTextResetPassword = (EditText) findViewById(R.id.editTextResetPassword);
        editTextResetConfirmPW = (EditText) findViewById(R.id.editTextResetConfirmPW);

        // to validate the reset password
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(ResetPasswordActivity.this,R.id.editTextResetPassword, regexPassword,R.string.err_pw);
        // to validate the confirmation reset password
        awesomeValidation.addValidation(ResetPasswordActivity.this,R.id.editTextResetConfirmPW, R.id.editTextResetPassword,R.string.err_confirmPW);

        btnResetConfirm = (Button) findViewById(R.id.btnResetConfirm);
        btnResetConfirm.setOnClickListener(this);
        btnResetCancel = (Button) findViewById(R.id.btnResetCancel);
        btnResetCancel.setOnClickListener(this);
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnResetConfirm:
                if(awesomeValidation.validate()){
                    String email = textViewEmailAddress.getText().toString();
                    String password = editTextResetPassword.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(this);

                    Boolean checkPasswordUpdate = databaseHelper.updatePassword(email,password);
                    if(checkPasswordUpdate==true){
                        Intent intentMainPage = new Intent(this, MainActivity.class);
                        Toast.makeText(getApplicationContext(),"Password updated successfully ✅",Toast.LENGTH_SHORT).show();
                        intentMainPage.putExtra("email",email);
                        startActivity(intentMainPage);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else{
                        Toast.makeText(getApplicationContext(),"⚠️Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnResetCancel:
                Intent intentForgotPasswordPage = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentForgotPasswordPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }
}