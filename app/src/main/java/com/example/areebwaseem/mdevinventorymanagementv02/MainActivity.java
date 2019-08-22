package com.example.areebwaseem.mdevinventorymanagementv02;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoginPostRequest.OnPostComplete {
 //   TextView mdevTitle;
    ProgressBar progressBar;
    SharedPreferences mySharedPreferences;
    private static final String TAG = "MainActivity";
    //EditText myText = (EditText) findViewById(R.id.editText);
    EditText userNameText;
    EditText passwordText;
    LoginPostRequest myRequest;
    String user_id = "";
    String user_name = "";
    String user_email = "";
    String user_role = "";
    String access_token = "";
    String user_departement = "";
    String profile_image = "";
    String name="";
    AlertDialog.Builder builder;
    ConstraintLayout inventoryView;
    NetworkInfo networkInfo;
    ConnectivityManager connMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPreferences = getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
      //  mdevTitle = (TextView) findViewById(R.id.MdevTitleTextView);
        passwordText = (EditText) findViewById(R.id.passwordText);
        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        String boldText = "MDEV";
        String normalText = " INVENTORY";
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
       SpannableString str = new SpannableString(boldText + normalText);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  mdevTitle.setText(str);
        inventoryView = (ConstraintLayout) findViewById(R.id.loginConstraint);
        inventoryView.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);
        progressBar.setVisibility(View.INVISIBLE);
        checkInternetPermissions();
        if (mySharedPreferences.getString("Login_Status","abc").equals("true"))
        {
            if (networkInfo != null && networkInfo.isConnected()) {
                Intent i = new Intent(getApplicationContext(), BackgroundService.class);
                startService(i);

                Intent x = new Intent(getApplicationContext(), NavBarSample.class);
                startActivity(x);
            }

             else {
                    Log.d(TAG, "build alert dialog");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this);
                    }
                    builder.setTitle("No Internet")
                            .setMessage(getResources().getString(R.string.wish_enter_off))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent i = new Intent(MainActivity.this, offline_order_activity.class);
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mySharedPreferences.getString("Login_Status","abc").equals("false"))
        {
            userNameText = (EditText) findViewById(R.id.userNameText);
            passwordText = (EditText) findViewById(R.id.passwordText);
            userNameText.setText("");
            passwordText.setText("");
        }
    }

    private  void checkInternetPermissions(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.INTERNET");
            if(permissionCheck!=0){
                this.requestPermissions(new String[]{Manifest.permission.INTERNET},1001);
            }

            else
            {
                Log.i("Has", "Permissions");
            }
        }
    }

    public void logInClicked(View view) {
        userNameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
       // progressBar.setVisibility(View.VISIBLE);

        if (userNameText.getText().toString().equals("") || passwordText.getText().toString().equals(""))
        {
            Log.d(TAG, "I came to second");
            Toast.makeText(this, getResources().getString(R.string.fields_cant_string), Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            myRequest = new LoginPostRequest(this, this);
            myRequest.setUpLogin(userNameText.getText().toString(), passwordText.getText().toString());
            myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/auth/login");
        }

/*
        myRequest = new LoginPostRequest(this);
        myRequest.setUpLogin("jumlauser1","password");
        myRequest.execute("http://cloud.mdevsolutions.com/inventory/api/api/auth/login");
        */
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        if (isMyServiceRunning(BackgroundService.class)) {
            Intent i = new Intent(this,BackgroundService.class);
            stopService(i);
        }

        super.onDestroy();
    }
    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.loginConstraint){
            try {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        progressBar.setVisibility(View.INVISIBLE);
        if (s!=null)
        {
            Log.d(TAG, s);
        }
        try{
            JSONObject myObject = new JSONObject(s);
            JSONObject jsonData = myObject.getJSONObject("data");
            user_id = jsonData.getString("id");
            user_name = jsonData.getString("username");
            user_email = jsonData.getString("email");
            user_role = jsonData.getString("role");
            access_token = jsonData.getString("token");
            user_departement = jsonData.getString("department");
            profile_image = jsonData.getString("profile_image");
            name = jsonData.getString("name");

            Log.d(TAG, user_id  +  user_email + user_role + user_departement + user_name );

        }catch (JSONException e){
            Log.e(TAG, "Exception is: " + e.getMessage());
        }
        mySharedPreferences.edit().putString("User_ID", user_id).apply();
        mySharedPreferences.edit().putString("USER_NAME", user_name).apply();
        mySharedPreferences.edit().putString("USER_EMAIL", user_email).apply();
        mySharedPreferences.edit().putString("USER_ROLE", user_role).apply();
        mySharedPreferences.edit().putString("ACCESS_TOKEN", access_token).apply();
        mySharedPreferences.edit().putString("USER_DEPARTMENT", user_departement).apply();
        mySharedPreferences.edit().putString("PROFILE_IMAGE", profile_image).apply();
        mySharedPreferences.edit().putString("NAME", name).apply();
        mySharedPreferences.edit().putString("Login_Status", "true").apply();
        Intent i = new Intent(getApplicationContext(),NavBarSample.class);
        startActivity(i);
        Log.d(TAG, "OnPostEnds");

    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean internetconnected) {
        progressBar.setVisibility(View.INVISIBLE);
       if (exception.equals("comm_error"))
       {
           Toast.makeText(MainActivity.this, getResources().getString(R.string.error_com_server_string), Toast.LENGTH_SHORT).show();
       }
       else if (!internetconnected) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_SHORT).show();
        }
        else if (exception!=null && !exception.equals("No_internet_connection") ) {
            Log.d(TAG, exception);
           userNameText = (EditText) findViewById(R.id.userNameText);
           passwordText = (EditText) findViewById(R.id.passwordText);
           userNameText.setText("");
           passwordText.setText("");

            Toast.makeText(MainActivity.this, getResources().getString(R.string.incor_user_or_pass_string), Toast.LENGTH_SHORT).show();
        }

    }

    public void i_forgot_password(View view) {

        Intent i = new Intent(getApplicationContext(),Forgot_password_Activity_final.class);
        startActivity(i);

    }
}
