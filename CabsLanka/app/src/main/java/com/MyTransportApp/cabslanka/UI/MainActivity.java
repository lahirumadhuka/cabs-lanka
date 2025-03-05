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
import android.widget.TextView;
import android.widget.Toast;

import com.MyTransportApp.cabslanka.Database.DatabaseHelper;
import com.MyTransportApp.cabslanka.Model.User;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.MyTransportApp.cabslanka.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignup,btnLogin;
    private EditText editTextEmail, editTextPW;
    private AwesomeValidation awesomeValidation;
    private TextView textViewForgotPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        awesomeValidation = new AwesomeValidation(BASIC);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPW = (EditText) findViewById(R.id.editTextPassword);
        // to validate the email and password
        awesomeValidation.addValidation(MainActivity.this,R.id.editTextEmailAddress, Patterns.EMAIL_ADDRESS,R.string.err_email);
        awesomeValidation.addValidation(MainActivity.this,R.id.editTextPassword, RegexTemplate.NOT_EMPTY,R.string.err_loginPW);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        textViewForgotPW = (TextView) findViewById(R.id.textViewForgotPW);
        textViewForgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPasswordPage = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intentForgotPasswordPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }
    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignup:
                Intent intentSignUpPage = new Intent(this, SignUpActivity.class);
                startActivity(intentSignUpPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.btnLogin:
                if(awesomeValidation.validate()){
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    String email = editTextEmail.getText().toString();
                    String password = editTextPW.getText().toString();
                    User user = databaseHelper.checkLogin(email,password);

                    if(user==null){
                        Toast.makeText(getApplicationContext(),"⚠️Invalid login",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Login successful ✅",Toast.LENGTH_SHORT).show();
                        Intent intentHomePage = new Intent(this, HomeActivity.class);
                        intentHomePage.putExtra("user",user);
                        startActivity(intentHomePage);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
