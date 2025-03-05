package com.MyTransportApp.cabslanka.UI;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MyTransportApp.cabslanka.Database.DatabaseHelper;
import com.MyTransportApp.cabslanka.Model.User;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.MyTransportApp.cabslanka.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSignUp;
    private EditText editTextName,editTextEmail,editTextPhoneNo,editTextAddress,editTextPW,editTextConfirmPW;
    private AwesomeValidation awesomeValidation;
    private TextView textViewAlreadyHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_signup);

        awesomeValidation = new AwesomeValidation(BASIC);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPW = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPW = (EditText) findViewById(R.id.editTextConfirmPW);

        // to validate the name, email, phone no and address
        awesomeValidation.addValidation(SignUpActivity.this,R.id.editTextName,"[a-zA-Z\\s]+",R.string.err_name);
        awesomeValidation.addValidation(SignUpActivity.this,R.id.editTextEmailAddress, Patterns.EMAIL_ADDRESS,R.string.err_email);
        awesomeValidation.addValidation(SignUpActivity.this,R.id.editTextPhoneNo, "^[0-9]{10}$",R.string.err_tel);
        awesomeValidation.addValidation(SignUpActivity.this,R.id.editTextAddress, "[a-zA-Z\\s]+",R.string.err_address);
        // to validate the password
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(SignUpActivity.this,R.id.editTextPassword, regexPassword,R.string.err_pw);
        // to validate the confirmation password
        awesomeValidation.addValidation(SignUpActivity.this,R.id.editTextConfirmPW, R.id.editTextPassword,R.string.err_confirmPW);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener((View.OnClickListener) this);

        textViewAlreadyHaveAnAccount = (TextView) findViewById(R.id.textViewAlreadyHaveAnAccount);
        textViewAlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainPage = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intentMainPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }
    //Use switch statement for button navigation
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                if(awesomeValidation.validate()){
                    String name = editTextName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String phoneNo = editTextPhoneNo.getText().toString();
                    String address = editTextAddress.getText().toString();
                    String password = editTextPW.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPhoneNo(phoneNo);
                    user.setAddress(address);
                    user.setPassword(password);

                    User checkEmail = databaseHelper.checkEmail(email); //Check Email Address Exists
                    if(checkEmail==null){
                        //Insert data
                        if(databaseHelper.addUser(user)){
                            Toast.makeText(getApplicationContext(),"Submitted successfully ✅",Toast.LENGTH_SHORT).show();
                            Intent intentMainPage = new Intent(this, MainActivity.class);
                            startActivity(intentMainPage);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }else {
                            Toast.makeText(getApplicationContext(),"⚠️Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"⚠️Email address exists",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
