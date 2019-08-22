package com.example.areebwaseem.mdevinventorymanagementv02;

/**
 * Created by areebwaseem on 10/19/17.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK }

/**
 * Created by timbuchalka on 4/08/2016.
 */

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private final OnDownloadComplete mCallback;
    NetworkInfo networkInfo;
    ConnectivityManager connMgr;
    boolean internetConnected =false;
    private DownloadStatus mDownloadStatus;
    Context mContext;
    String downException="";
    interface OnDownloadComplete {
        void OnDownloadComplete(String data, DownloadStatus status);
        void downloadErrorPosting(String exception, DownloadStatus status, boolean connection);
    }

    public GetRawData(OnDownloadComplete Callback, Context context) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallback=Callback;
        mContext = context;
        connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "I cameeeee00000000000000000000000000000");
        if (s!=null && mDownloadStatus==DownloadStatus.OK){
            mCallback.OnDownloadComplete(s,mDownloadStatus );
            Log.d(TAG, "I cameeeee111111111111111111111111111111111");
        }
        else if (s==null && mDownloadStatus==DownloadStatus.IDLE){
            downException= "No_internet_connection";
            mCallback.downloadErrorPosting(downException, mDownloadStatus, internetConnected);
            Log.d(TAG, "I cameeeee2222222222222222222222222222");
        }
        else if (mDownloadStatus==DownloadStatus.PROCESSING && s!=null)
        {
            Log.d(TAG, "I came 3333333333333333333333333333333");
            downException=s;
            mCallback.downloadErrorPosting(downException, mDownloadStatus, internetConnected);
        }
        else {
            mCallback.downloadErrorPosting("comm_error",mDownloadStatus,internetConnected);
        }
        Log.d(TAG, "On Post Ends..");
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            internetConnected=true;

            try {
                mDownloadStatus = DownloadStatus.PROCESSING;
                URL url = new URL(strings[0]);
                String access_token = strings[1];
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("access-token", access_token);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d(TAG, "doInBackground: The response code was " + response);

                StringBuilder result = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    result.append(line).append("\n");
                }

                mDownloadStatus = DownloadStatus.OK;
                return result.toString();


            } catch (MalformedURLException e) {
                Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
            }catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "IOException" + e.getMessage());

                try {


                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                    //   BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    String second_result;

                    while ((line = reader1.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader1.close();
                    return sb.toString();
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }  catch (SecurityException e) {
                Log.e(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage());
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                    }
                }
            }

            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            return null;
        }
        else
        {
            internetConnected=false;
            mDownloadStatus = DownloadStatus.IDLE;
            return null;
        }
    }



}




