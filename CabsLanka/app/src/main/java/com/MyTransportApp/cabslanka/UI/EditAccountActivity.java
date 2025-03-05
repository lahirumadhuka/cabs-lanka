package com.MyTransportApp.cabslanka.UI;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MyTransportApp.cabslanka.Database.DatabaseHelper;
import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.basgeekball.awesomevalidation.AwesomeValidation;

public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private User user;
    private EditText editTextName, editTextPhoneNo, editTextAddress, editTextPassword, editTextConfirmPW;
    private TextView textViewEmailAddress, textViewDeleteAccount;
    private Button btnSave, btnCancel;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_edit_account);

        awesomeValidation = new AwesomeValidation(BASIC);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        textViewEmailAddress = (TextView) findViewById(R.id.textViewEmailAddress);
        textViewEmailAddress.setText(user.getEmail());

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setText(user.getName());
        editTextPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        editTextPhoneNo.setText(user.getPhoneNo());
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextAddress.setText(user.getAddress());
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setText(user.getPassword());
        editTextConfirmPW = (EditText) findViewById(R.id.editTextConfirmPW);

        // to validate the name, phone no and address
        awesomeValidation.addValidation(EditAccountActivity.this,R.id.editTextName,"[a-zA-Z\\s]+",R.string.err_name);
        awesomeValidation.addValidation(EditAccountActivity.this,R.id.editTextPhoneNo, "^[0-9]{10}$",R.string.err_tel);
        awesomeValidation.addValidation(EditAccountActivity.this,R.id.editTextAddress, "[a-zA-Z\\s]+",R.string.err_address);
        // to validate the password
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(EditAccountActivity.this,R.id.editTextPassword, regexPassword,R.string.err_pw);
        // to validate the confirmation password
        awesomeValidation.addValidation(EditAccountActivity.this,R.id.editTextConfirmPW, R.id.editTextPassword,R.string.err_confirmPW);

        textViewDeleteAccount = (TextView) findViewById(R.id.textViewDeleteAccount);
        textViewDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeleteAccountPage = new Intent(EditAccountActivity.this,DeleteAccountActivity.class);
                intentDeleteAccountPage.putExtra("user",user);
                startActivity(intentDeleteAccountPage);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSave:
                if(awesomeValidation.validate()){
                    String name = editTextName.getText().toString();
                    String email = textViewEmailAddress.getText().toString();
                    String phoneNo = editTextPhoneNo.getText().toString();
                    String address = editTextAddress.getText().toString();
                    String password = editTextPassword.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(this);

                    Boolean checkAccountUpdate = databaseHelper.updateAccount(name,email,phoneNo,address,password);
                    if(checkAccountUpdate==true){
                        Toast.makeText(getApplicationContext(),"Account updated successfully ✅",Toast.LENGTH_SHORT).show();
                        Intent intentAccountPage = new Intent(this, AccountActivity.class);
                        intentAccountPage.putExtra("user",user);
                        startActivity(intentAccountPage);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else {
                        Toast.makeText(getApplicationContext(),"⚠️Something went wrong",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel:
                Intent intentAccountPage = new Intent(this,AccountActivity.class);
                intentAccountPage.putExtra("user",user);
                startActivity(intentAccountPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }
}