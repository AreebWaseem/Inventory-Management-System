package com.example.areebwaseem.mdevinventorymanagementv02;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NameList;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by areebwaseem on 11/13/17.
 */

public class BackgroundService extends Service implements GetRawData.OnDownloadComplete, LoginPostRequest.OnPostComplete{
    private static final String TAG = "BackgroundService";
     boolean isProcessing=false;
        ////////////////////////////////////////////////////////////////////////////////////////////////
        String customer_data="";
LoginPostRequest myRequest;
    LoginPostRequest order_initiate_post_reuqest;
    Notification MdevNotification;
    boolean cust_post_request=false;
    boolean order_post_request=false;
    boolean cust_down_request=false;
    boolean order_down_request=false;
    DataBaseHelper myHelper;
    ArrayList<String> order_id_list;
    ArrayList<String> order_data_list;
    int order_id_check=0;
    StringBuffer id_buffer;
    StringBuffer order_buffer;
    int offline_order_count=0;
    String id_index="";
GetRawData getRawData;

        private final IBinder iBinder = new LocalBinder();

        String access_token="";

    ArrayList<String> customerNameList;
    ArrayList<String> customerAddressList;
    ArrayList<String> customerIDList;
ArrayList<String> customer_phone_list;
int myCheck=0;
        Notification _foregroundNotification;

        SharedPreferences mySharedPreferences;

        @Override
        public void onCreate() {
            mySharedPreferences = getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
            myHelper = new DataBaseHelper(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            this.registerReceiver(mReceiver,filter);
            if (MdevNotification==null) {
                startInForeground();
            }

            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
            customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");

if (!isProcessing) {
    myHelper = new DataBaseHelper(this);
    isProcessing=true;
    customerNameList = new ArrayList<String>();
    customerAddressList = new ArrayList<String>();
    customer_phone_list = new ArrayList<String>();
    customerIDList = new ArrayList<String>();
    order_id_list = new ArrayList<String>();
    order_data_list = new ArrayList<String>();
    Cursor res = myHelper.getAllData_Order();
    if (res != null) {
        StringBuffer buffer = new StringBuffer();
        try {
            while (res.moveToNext()) {
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("Name: " + res.getString(1) + "\n");
                offline_order_count = offline_order_count + 1;
            }

            Log.d(TAG, buffer.toString());
            Log.d(TAG, "order buffer count   " + String.valueOf(offline_order_count));
        } finally {
            res.close();
        }
        try {
            JSONObject jsonData = new JSONObject(customer_data);
            JSONArray itemsArray = jsonData.getJSONArray("data");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject currentObject = itemsArray.getJSONObject(i);
                String nam = currentObject.getString("name");
                String id = currentObject.getString("id");
                String addres = currentObject.getString("address");
                if (id.equals("-1")) {
                    customer_phone_list.add(currentObject.getString("phone"));
                } else {
                    customer_phone_list.add("");
                }
                customerNameList.add(nam);
                customerIDList.add(id);
                customerAddressList.add(addres);

            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        boolean new_check = false;
        for (int x = 0; x < customerIDList.size(); x++) {
            if (customerIDList.get(x).equals("-1")) {
                new_check = true;
                myCheck = x;
                break;
            }
        }
        if (new_check == true) {
            Toast.makeText(getApplicationContext(), "Customer Sync Started!", Toast.LENGTH_SHORT).show();
            cust_post_request = true;
            myRequest = new LoginPostRequest(BackgroundService.this, getApplicationContext());
            myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer/add", access_token, customerNameList.get(myCheck), customer_phone_list.get(myCheck), customerAddressList.get(myCheck));
        } else {


            order_post_request = true;
            res = myHelper.getAllData_Order();
            int myCount = 0;
            if (res != null) {
                try {


                    while (res.moveToNext()) {
                        id_buffer = new StringBuffer();
                        order_buffer = new StringBuffer();
                        id_buffer.append(res.getString(0));
                        order_buffer.append(res.getString(1));
                        order_id_list.add(id_buffer.toString());
                        order_data_list.add(order_buffer.toString());
                    }
                } finally {
                    res.close();
                }
                //    buffer.append("Name: " + res.getString(1) + "\n");

            }
            Log.d(TAG, String.valueOf(order_data_list.size()));

            if (order_id_list.size() != 0) {
                Log.d(TAG, "Order Sync Startet............");
                String myOrder = "";

                for (int y = 0; y < order_id_list.size(); y++) {
                    try {
                        myOrder = order_data_list.get(y);
                        JSONObject myObject = new JSONObject(myOrder);
                        if (myObject.getString("customer_id").equals("-1")) {
                            Log.d(TAG, "del 1");
                            for (int z = 0; z < customerIDList.size(); z++) {
                                if (myObject.getString("current_customer_name").equals(customerNameList.get(z)) && myObject.getString("shipping_address").equals(customerAddressList.get(z))) {
                                    myObject.put("customer_id", customerIDList.get(z));
                                    myObject.remove("current_customer_name");
                                    // Log.d(TAG, String.valueOf(myObject));
                                    // Log.d(TAG, "-1 removed");

                                }
                            }
                        } else {
                            Log.d(TAG, "del 2");
                            myObject.remove("current_customer_name");

                            // Log.d(TAG, "other removed removed");
                        }
                        order_data_list.set(y, String.valueOf(myObject));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, String.valueOf(order_data_list.size()));
                Toast.makeText(getApplicationContext(), "Order Sync Started!", Toast.LENGTH_SHORT).show();
                order_id_check = 0;
                order_initiate_post_reuqest = new LoginPostRequest(this, getApplicationContext());
                order_initiate_post_reuqest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/save", access_token, String.valueOf(order_data_list.get(0)));


                //   Log.d(TAG, buffer.toString());
            } else {
                Log.d(TAG, "All order Synced!");
                Toast.makeText(getApplicationContext(), "All orders Synced!", Toast.LENGTH_LONG).show();
                isProcessing = false;
                stopForeground(true);
                stopSelf();
            }
        }
    }
        else{
            Log.d(TAG, "All order Synced");
            stopForeground(true);
            stopSelf();
        }
}
else {
    Toast.makeText(getApplicationContext(), "Already Syncing!", Toast.LENGTH_SHORT).show();

}

            return START_STICKY;
        }

        @Override
        public void onDestroy() {
            Log.d(TAG, "Service Stopped");
            if (mReceiver!=null) {
                unregisterReceiver(mReceiver);
            }
            ////////////////////////////////////////////////////////////////////////////////
            super.onDestroy();
        }

        private BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ni= connectivityManager.getActiveNetworkInfo();
                    if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                        Log.i("app", "Network " + ni.getTypeName() + " connected");
                    } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                        Log.d("app", "There's no network connectivity");
                    }
                }
            }
        };

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            return iBinder;
        }





        void startInForeground(){
        /*
        int notificationIcon = R.drawable.babyimage;
        String notificationTickerText = "Plural service starting";

        _foregroundNotification= new Notification(notificationIcon,notificationTickerText,notificationTimestamp);
        */
            long notificationTimestamp= System.currentTimeMillis();

            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this, "1");
            _foregroundNotification= builder.setSmallIcon(R.drawable.mdev_logo).setTicker("Inventory Sync Started").setWhen(notificationTimestamp)
                    .setContentTitle("MDEV")
                    .setContentText("Inventory Sync in Process").build();

            startForeground(1,_foregroundNotification);
        }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
            if (data!=null && cust_down_request==true)
            {
                Log.d(TAG, data);
                mySharedPreferences.edit().putString("order_create_customer_list", data).apply();
                cust_down_request=false;
                Toast.makeText(getApplicationContext(), "Customer Sync Completed!", Toast.LENGTH_SHORT).show();
                if (customerIDList!=null)
                {
                    customerIDList.clear();
                    customerIDList=new ArrayList<String>();
                }
                if (customerAddressList!=null)
                {
                    customerAddressList.clear();
                    customerAddressList= new ArrayList<String>();
                }
                if (customerNameList!=null)
                {
                    customerNameList.clear();
                    customerNameList= new ArrayList<String>();
                }
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray itemsArray = jsonData.getJSONArray("data");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject currentObject = itemsArray.getJSONObject(i);
                        String nam = currentObject.getString("name");
                        String id = currentObject.getString("id");
                        String addres = currentObject.getString("address");
                        customerNameList.add(nam);
                        customerIDList.add(id);
                        customerAddressList.add(addres);

                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                order_post_request = true;
               Cursor res = myHelper.getAllData_Order();
               if (order_data_list!=null)
               {
                   order_id_list.clear();
                   order_id_list = new ArrayList<String>();
               }
               if (order_id_list!=null)
               {
                   order_data_list.clear();
                   order_data_list=new ArrayList<String>();
               }
                int myCount = 0;
                if (res != null) {
                    try {


                        while (res.moveToNext()) {
                            id_buffer = new StringBuffer();
                            order_buffer = new StringBuffer();
                            id_buffer.append(res.getString(0));
                            order_buffer.append(res.getString(1));
                            order_id_list.add(id_buffer.toString());
                            order_data_list.add(order_buffer.toString());
                        }
                    } finally {
                        res.close();
                    }
                    //    buffer.append("Name: " + res.getString(1) + "\n");

                }
                Log.d(TAG, String.valueOf(order_data_list.size()));

                if (order_id_list.size() != 0) {
                    Log.d(TAG, "Order Sync Startet............");
                    String myOrder = "";

                    for (int y = 0; y < order_id_list.size(); y++) {
                        try {
                            myOrder = order_data_list.get(y);
                            JSONObject myObject = new JSONObject(myOrder);
                            if (myObject.getString("customer_id").equals("-1")) {
                                Log.d(TAG, "del 1");
                                for (int z = 0; z < customerIDList.size(); z++) {
                                    if (myObject.getString("current_customer_name").equals(customerNameList.get(z)) && myObject.getString("shipping_address").equals(customerAddressList.get(z))) {
                                        myObject.put("customer_id", customerIDList.get(z));
                                        myObject.remove("current_customer_name");
                                        // Log.d(TAG, String.valueOf(myObject));
                                        // Log.d(TAG, "-1 removed");

                                    }
                                }
                            }
                            else if (myObject.getString("customer_id").equals("-1000"))
                            {
                                myObject.remove("current_customer_name");
                                myObject.remove("customer_id");
                            }
                            else {
                                Log.d(TAG, "del 2");
                                myObject.remove("current_customer_name");

                                // Log.d(TAG, "other removed removed");
                            }
                            order_data_list.set(y, String.valueOf(myObject));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, String.valueOf(order_data_list.size()));
                    Toast.makeText(getApplicationContext(), "Order Sync Started!", Toast.LENGTH_SHORT).show();
                    order_id_check = 0;
                    order_initiate_post_reuqest = new LoginPostRequest(this, getApplicationContext());
                    order_initiate_post_reuqest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/save", access_token, String.valueOf(order_data_list.get(0)));


                    //   Log.d(TAG, buffer.toString());
                } else {
                    Log.d(TAG, "All order Synced!");
                    Toast.makeText(getApplicationContext(), "All orders Synced!", Toast.LENGTH_LONG).show();
                    isProcessing = false;
                    stopForeground(true);
                    stopSelf();
                }

            }


            /*
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray itemsArray = jsonData.getJSONArray("data");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject currentObject = itemsArray.getJSONObject(i);
                String nam = currentObject.getString("name");
                String id = currentObject.getString("id");
                String addres = currentObject.getString("address");
                customerNameList.add(nam);
                customerIDList.add(id);
                customerAddressList.add(addres);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
       */
    }


    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {

isProcessing=false;

            if (exception.equals("comm_error"))
            {
                Toast.makeText(getApplicationContext(), "Error Communicating with server!", Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getApplicationContext(), "No internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if (exception!=null && !exception.equals("No_internet_connection" )) {
                Log.d(TAG, exception);
                Toast.makeText(getApplicationContext(), "Something Went Wrong, Try Again!", Toast.LENGTH_SHORT).show();
            }
            stopForeground(true);
        stopSelf();

    }

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
            if (s != null) {
              //  ((NavBarSample) getActivity()).makeToast("Customer Successfully Added!", true);
                Log.d(TAG, "Successfully Addedddddddddd");
                if (cust_post_request==true) {

                    if (myCheck < customerNameList.size() - 1) {
                        myCheck++;
                        myRequest = new LoginPostRequest(BackgroundService.this, getApplicationContext());
                        myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer/add", access_token, customerNameList.get(myCheck), customer_phone_list.get(myCheck), customerAddressList.get(myCheck));
                        cust_post_request = true;
                    } else {
                        customerIDList.clear();
                        customerNameList.clear();
                        customerAddressList.clear();
                        customer_phone_list.clear();
                        getRawData = new GetRawData(this, getApplicationContext());
                        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000", access_token);
                        cust_post_request = false;
                        cust_down_request = true;
                    }
                }
                else if (order_post_request==true)
                {
                    if (order_id_check< order_id_list.size()-1)
                    {
                        myHelper.delete_all_orders(order_id_list.get(order_id_check));
                        order_id_check=order_id_check+1;
                        try {

                            JSONObject myObject = new JSONObject(order_data_list.get(order_id_check));
                            order_initiate_post_reuqest = new LoginPostRequest(this, getApplicationContext());
                            order_initiate_post_reuqest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/save", access_token, String.valueOf(myObject));
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                    else {
                        myHelper.delete_all_orders(order_id_list.get(order_id_check));
                        Toast.makeText(getApplicationContext(), "Order Sync Completed!", Toast.LENGTH_SHORT).show();
                        isProcessing=false;
                    Log.d(TAG, "Order Sync Complete");
                    order_post_request=false;
                    }



                }
            }

    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean connection) {
        isProcessing=false;
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getApplicationContext(), "Error Communicating with server!", Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getApplicationContext(), "No internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if (exception!=null && !exception.equals("No_internet_connection" )) {
                Log.d(TAG, exception);
                Toast.makeText(getApplicationContext(), "Something Went Wrong, Try Again!", Toast.LENGTH_SHORT).show();
            }
            stopForeground(true);
            stopSelf();
    }


    // public void setCallbacks(ServiceCallbacks callbacks) {
           // serviceCallbacks = callbacks;
       // }


        public class LocalBinder extends Binder {
            BackgroundService getService(){
                return BackgroundService.this;
            }
        }




}
