package com.MyTransportApp.cabslanka.UI;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MyTransportApp.cabslanka.Database.DatabaseHelper;
import com.MyTransportApp.cabslanka.Model.User;
import com.MyTransportApp.cabslanka.R;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class DeleteAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnBack, btnConfirm;
    private User user;
    private EditText editTextPassword, editTextConfirmPW;
    private TextView textViewEmailAddress;
    private AwesomeValidation awesomeValidation;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_delete_account);

        awesomeValidation = new AwesomeValidation(BASIC);

        //Get values from previous page (Email and Password)
        user = (User) getIntent().getSerializableExtra("user");

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_delete_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        Button btnDialogConfirm = dialog.findViewById(R.id.btnDialogConfirm);
        btnDialogConfirm.setOnClickListener(this);
        Button btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        btnDialogCancel.setOnClickListener(this);
        TextView textViewBye = dialog.findViewById(R.id.textViewBye);
        textViewBye.setText(user.getName()+", sorry to see you go");

        textViewEmailAddress = (TextView) findViewById(R.id.textViewEmailAddress);
        textViewEmailAddress.setText(user.getEmail());

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPW = (EditText) findViewById(R.id.editTextConfirmPW);

        // to validate the password
        awesomeValidation.addValidation(DeleteAccountActivity.this,R.id.editTextPassword, RegexTemplate.NOT_EMPTY,R.string.err_loginPW);
        // to validate the confirmation password
        awesomeValidation.addValidation(DeleteAccountActivity.this,R.id.editTextConfirmPW, R.id.editTextPassword,R.string.err_confirmPW);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }
    //Use switch statement for button navigation
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                Intent intentEditAccountPage = new Intent(this,EditAccountActivity.class);
                intentEditAccountPage.putExtra("user",user);
                startActivity(intentEditAccountPage);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.btnConfirm:
                if(awesomeValidation.validate()){
                    String email = textViewEmailAddress.getText().toString();
                    String password = editTextPassword.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(this);

                    User checkUser = databaseHelper.checkLogin(email,password);
                    if(checkUser!=null){
                        dialog.show(); // Showing the dialog here
                    }else{
                        Toast.makeText(getApplicationContext(),"⚠️Invalid password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"⚠️There are items that require your attention",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDialogConfirm: //Dialog button
                String email = textViewEmailAddress.getText().toString();
                String password = editTextPassword.getText().toString();

                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                Boolean checkAccountDelete = databaseHelper.deleteAccount(email,password);
                if(checkAccountDelete==true){
                    Toast.makeText(getApplicationContext(),"Account deleted successfully ✅",Toast.LENGTH_SHORT).show();
                    Intent intentMainPage = new Intent(this,MainActivity.class);
                    startActivity(intentMainPage);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),"⚠️Something went wrong",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDialogCancel: //Dialog button
                dialog.dismiss();
                break;
        }
    }
}