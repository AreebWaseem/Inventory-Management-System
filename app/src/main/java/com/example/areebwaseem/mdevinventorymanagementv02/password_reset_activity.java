package com.example.areebwaseem.mdevinventorymanagementv02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

public class password_reset_activity extends AppCompatActivity implements LoginPostRequest.OnPostComplete{
    private static final String TAG = "password_reset_activity";
    EditText newPass;
    EditText res_code_enter;
    EditText enter_email;
    ProgressBar myBar;
    LinearLayout sendEmail;
    LinearLayout resetCode;
    LinearLayout newPasswordLay;
    LoginPostRequest myRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_activity);
        newPass = (EditText) findViewById(R.id.new_password_edit_text);
        res_code_enter = (EditText) findViewById(R.id.reset_code_edit_text);
        enter_email = (EditText) findViewById(R.id.enter_email_edit_text);
        sendEmail = (LinearLayout)findViewById(R.id.send_email_layout);
        resetCode = (LinearLayout) findViewById(R.id.enter_reset_code_layout);
        newPasswordLay = (LinearLayout) findViewById(R.id.enter_new_password_layout);
        resetCode.setVisibility(View.INVISIBLE);
        newPasswordLay.setVisibility(View.INVISIBLE);
        myBar = (ProgressBar) findViewById(R.id.progressBar_reset_password_activity);
        myBar.setVisibility(View.INVISIBLE);

    }

    public void send_reset_email(View view) {

         if (enter_email.getText().toString().equals(""))
         {
             Toast.makeText(this,getResources().getString(R.string.fields_cant_string), Toast.LENGTH_SHORT).show();
         }
         else {
             myBar.setVisibility(View.VISIBLE);
             myRequest = new LoginPostRequest(this, this);
             myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/auth/forgot_password" ,enter_email.getText().toString());
         }
    }

    public void reset_clicked(View view) {
    }

    public void confirm_clicked(View view) {
    }

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
     if (s!=null)
     {
         myBar.setVisibility(View.INVISIBLE);
         Log.d(TAG, s);
     }
    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean coonection) {
        myBar.setVisibility(View.INVISIBLE);
        // if ( exception.equals("IOException")){

        //}
        //else{
        // Toast.makeText(MainActivity.this, "Error Loggin In!", Toast.LENGTH_LONG).show();
        //   Log.d(TAG, exception);
        //
        if (!coonection) {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.incor_user_or_pass_string), Toast.LENGTH_SHORT).show();
        }
    }
}
