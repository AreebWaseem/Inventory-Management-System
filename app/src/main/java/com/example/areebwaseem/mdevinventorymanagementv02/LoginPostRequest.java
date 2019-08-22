package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

/**
 * Created by areebwaseem on 10/21/17.
 */

enum PostStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK }

public class LoginPostRequest extends AsyncTask<String, Void, String> {
    Handler myhandler;
    HttpURLConnection httpcon;
    private static final String TAG = "LoginPostRequest";
    BufferedWriter writer;
    boolean error=false;
    private final OnPostComplete mCallBack;
    private String usernameFromActivity;
    private String passWordFromActivity;
    boolean internetConnected =false;
    Context mcontext;
    NetworkInfo networkInfo;
    ConnectivityManager connMgr;
    private PostStatus mPostStatus;
    String postException;
    interface OnPostComplete{
        void onPostComplete(String s, PostStatus postStatus);
        void showErrorPosting(String exception, PostStatus status, boolean coonection);
    }


    public LoginPostRequest(OnPostComplete CallBack, Context context){
        this.mPostStatus=PostStatus.IDLE;
       connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        mCallBack= CallBack;
        mcontext=context;
    }


    @Override
    protected void onPostExecute(String s) {
Log.d(TAG, "I cameeeee00000000000000000000000000000");
        if (s!=null && mPostStatus==PostStatus.OK){
           mCallBack.onPostComplete(s,mPostStatus );
            Log.d(TAG, "I cameeeee111111111111111111111111111111111");
        }
        else if (s==null && mPostStatus==PostStatus.IDLE){
            postException= "No_internet_connection";
            mCallBack.showErrorPosting(postException, mPostStatus, internetConnected);
            Log.d(TAG, "I cameeeee2222222222222222222222222222");
        }
         else if (mPostStatus==PostStatus.PROCESSING && s!=null)
        {
            Log.d(TAG, "I came 3333333333333333333333333333333");
            postException=s;
            mCallBack.showErrorPosting(postException, mPostStatus, internetConnected);
        }
        else {
            mCallBack.showErrorPosting("comm_error",mPostStatus,internetConnected);
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        if(strings == null) {
            mPostStatus = PostStatus.NOT_INITIALISED;
            return null;
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            internetConnected=true;
            String data = null;
            String result = null;
            String errorFound = "error";
            //Log.d(TAG, strings[0]+strings[1]+strings[2]);
            if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/auth/login")) {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    //httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    JSONObject json = new JSONObject();
                    json.put("user_name", usernameFromActivity);
                    json.put("password", passWordFromActivity);
                    writer.write(String.valueOf(json));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSONException" + e.getMessage());
                    postException = "JSONException";
                }
            } else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/customer/update/")) {
                Log.d(TAG, "*****************************************************************************");
                // getRawData.execute("http://cloud.mdevsolutions.com/inventory/api/api/customer/get/", access_token, customer_id,cust_name_up.getText().toString(),cust_address_up.getText().toString(),cust_phone_up.getText().toString() );

                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0] + strings[2]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("access-token", strings[1]);
                    //httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    JSONObject json = new JSONObject();
                    json.put("customer_name", strings[3]);
                    json.put("phone", strings[5]);
                    json.put("address", strings[4]);
                    writer.write(String.valueOf(json));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                }catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSONException" + e.getMessage());
                    postException = "JSONException";
                }

            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/customer/add"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("access-token", strings[1]);
                    //httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    JSONObject json = new JSONObject();
                    json.put("customer_name", strings[2]);
                   json.put("phone", strings[3]);
                    json.put("address", strings[4]);

                    writer.write(String.valueOf(json));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSONException" + e.getMessage());
                    postException = "JSONException";
                }

            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/save"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("access-token", strings[1]);
                    //httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));



                    writer.write(strings[2]);
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                }catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }

            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products/save"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("access-token", strings[1]);
                    //httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    writer.write(strings[2]);
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/auth/forgot_password"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    //httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    JSONObject json = new JSONObject();
                    json.put("mobile_no", strings[1]);

                    Log.d(TAG,strings[1]);
                    writer.write(String.valueOf(String.valueOf(json)));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
                 //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSONException" + e.getMessage());
                    postException = "JSONException";
                } catch (Exception e)
                {
                   // Log.d(TAG, "Exception sijdfdsiljfisgisdljdishgjishgusdhgdushguishgsdiuhgs " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/order/save"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("access-token", strings[1]);
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


                    writer.write(strings[2]);
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
                    //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (Exception e)
                {
                    // Log.d(TAG, "Exception sijdfdsiljfisgisdljdishgjishgusdhgdushguishgsdiuhgs " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/employeee/update"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("access-token", strings[1]);
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();
                    JSONObject update_object = new JSONObject();
                    update_object.put("name", strings[2]);
                    update_object.put("old_password", strings[3]);
                    update_object.put("password", strings[4]);

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(String.valueOf(update_object));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
                    //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (Exception e)
                {
                    // Log.d(TAG, "Exception sijdfdsiljfisgisdljdishgjishgusdhgdushguishgsdiuhgs " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/auth/enter_reset_code"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();
                    JSONObject update_object = new JSONObject();
                    update_object.put("email", strings[1]);
                    update_object.put("reset_code", strings[2]);

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(String.valueOf(update_object));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
                    //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (Exception e)
                {
                    // Log.d(TAG, "Exception sijdfdsiljfisgisdljdishgjishgusdhgdushguishgsdiuhgs " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else if (strings[0].equals("http://cloud.mdevsolutions.com/inventory/develop/api/auth/reset_password"))
            {
                try {
                    //Connect
                    mPostStatus = PostStatus.PROCESSING;
                    URL url = new URL(strings[0]);
                    httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();
                    JSONObject update_object = new JSONObject();
                        update_object.put("user_id", strings[1]);
                   update_object.put("password", strings[2]);


                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(String.valueOf(update_object));
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
                    //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    result = sb.toString();
                    mPostStatus = PostStatus.OK;
                    return result;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "UnsupportedEncodingException" + e.getMessage());
                    postException = "UnsupportedEncodingException";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException" + e.getMessage());
                    postException = "IOException";
                    try {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        String second_result;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }

                        reader.close();
                        second_result = sb.toString();
                        return sb.toString();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (Exception e)
                {
                    // Log.d(TAG, "Exception sijdfdsiljfisgisdljdishgjishgusdhgdushguishgsdiuhgs " + e.getMessage());
                    e.printStackTrace();
                }
            }


            mPostStatus = PostStatus.FAILED_OR_EMPTY;
            return null;
        }

else {
            internetConnected=false;
            mPostStatus = PostStatus.IDLE;
            return null;
        }
    }
    public void setUpLogin(String a, String b){
        usernameFromActivity = a;
        passWordFromActivity = b;
    }
}
