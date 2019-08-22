package com.example.areebwaseem.mdevinventorymanagementv02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class requested_products_detail_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "requested_products_deta";
    GetRawData getRawData;
    String request_order_id="";
    SharedPreferences mySharedPreferences;
    ArrayList<String> product_name_list;
    ArrayList<String> product_ordered_quantity_list;
    ArrayList<String> product_received_quantity_list;
    ArrayList<String> product_id_list;
    ArrayList<String> product_im_url_list;
    ListView product_detail_view;
    String current_stati="";
    String access_token="";
    //TextView current_id;
    TextView order_code;
    TextView deliver_date;
    TextView receive_date;
    TextView current_status;
    customAdapter adapter;
    TextView total_number_of_products;
    //TextView depID;
    String netResult="";
    boolean isDownloadRunning=false;
    ProgressBar detail_products_progress;
    Button confirm_Button;
    boolean isAcknowledging=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.requested_product_detail_acknowledge_combined_layout_fragment_viuew,container,false);
        detail_products_progress=(ProgressBar) view.findViewById(R.id.progressBar_requested_products_detail_updated);
       // current_id = (TextView) view.findViewById(R.id.custom_request_detail_id_text_view);
       order_code = (TextView) view.findViewById(R.id.request_product_detail_order_code);
        // depID = (TextView) view.findViewById(R.id.dept_id);
      //  deliver_date = (TextView) view.findViewById(R.id.custom_request_detail_delivered_text_view);
      //  receive_date = (TextView) view.findViewById(R.id.custom_request_detail_received_text_view);
      // total_number_of_products = (TextView) view.findViewById(R.id.custom_request_detail_total_number_text_view);
      current_status = (TextView) view.findViewById(R.id.request_product_detail_status);
        // product_array_details = (TextView) view.findViewById(R.id.products_array);
        product_ordered_quantity_list= new ArrayList<String>();
        product_received_quantity_list = new ArrayList<String>();
        product_id_list = new ArrayList<String>();
        product_name_list = new ArrayList<String>();
        product_im_url_list = new ArrayList<String>();
        adapter = new customAdapter();
       product_detail_view = (ListView) view.findViewById(R.id.request_detail_updated_list_view);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        request_order_id = ((NavBarSample)getActivity()).requested_products_detail_view_id;

        confirm_Button = (Button) view.findViewById(R.id.request_product_detail_proceed);
        confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!current_stati.equals("") && !current_stati.equals("in-verification") && !current_stati.equals("received")) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setTitle("Acknowledge")
                            .setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    isAcknowledging = true;
                                    getRawData = new GetRawData(requested_products_detail_fragment_view.this, getActivity());
                                    detail_products_progress.setVisibility(View.VISIBLE);
                                    String url = "http://cloud.mdevsolutions.com/inventory/develop/api/requested_products/received/" + request_order_id;
                                    getRawData.execute(url, access_token);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.checkbox_on_background)
                            .show();
                /*
                getRawData = new GetRawData(requested_products_detail_fragment_view.this, getActivity());
                detail_products_progress.setVisibility(View.VISIBLE);
                String url= "http://cloud.mdevsolutions.com/inventory/api/api/requested_products/received/" + request_order_id;
                getRawData.execute(url, access_token);
                */
                }

                else {
                    Toast.makeText(getActivity(), "Check Status!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        getRawData = new GetRawData(this,getActivity());

        detail_products_progress.setVisibility(View.VISIBLE);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products/view/" + request_order_id, access_token);
        isDownloadRunning=true;

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        Log.d(TAG, "First Resume");
        if (visible && isResumed())
        {

            Log.d(TAG, "First Resume with visible");
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume

            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Second Resume");

        /////////////////////////////////////////////////////////////////////////////////
        if (!getUserVisibleHint()) {
            Log.d(TAG, "Second Resume Inside");
            return;
        }
        if (getActivity()!=null) {
            ((NavBarSample) getActivity()).setItemChecked("requested_products_detail");

            if (isDownloadRunning == false) {
                Log.d(TAG, "Resume Downloaded Started");
                request_order_id = ((NavBarSample) getActivity()).requested_products_detail_view_id;
                Log.d(TAG, "Resume " + request_order_id);
                 adapter = new customAdapter();
                getRawData = new GetRawData(this, getActivity());
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products/view/" + request_order_id, access_token);
                detail_products_progress.setVisibility(View.VISIBLE);
                isDownloadRunning = true;
                isAcknowledging=false;

            }
        }

        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {

            if (isAcknowledging == true) {
                detail_products_progress.setVisibility(View.INVISIBLE);
                isDownloadRunning=false;
                isAcknowledging=false;
                try {
                    JSONObject myObj = new JSONObject(data);
                    Toast.makeText(getActivity(), myObj.getString("message"),Toast.LENGTH_SHORT).show();
                    ((NavBarSample)getActivity()).setrequested_viewpager();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            } else {
                boolean istrue = false;
                if (product_received_quantity_list != null) {
                    product_received_quantity_list.clear();
                }

                if (product_ordered_quantity_list != null) {
                    product_ordered_quantity_list.clear();
                }

                if (product_name_list != null) {
                    product_name_list.clear();
                }
                if (product_im_url_list!=null)
                {
                    product_im_url_list.clear();
                }
                if (product_id_list != null) {
                    product_id_list.clear();
                } else {
                    istrue = true;
                }
                if (istrue == false) {
                    product_id_list = new ArrayList<String>();
                    product_name_list = new ArrayList<String>();
                    product_ordered_quantity_list = new ArrayList<String>();
                    product_received_quantity_list = new ArrayList<String>();
                }
                detail_products_progress.setVisibility(View.INVISIBLE);
                isDownloadRunning = false;
                int currentIndex = 0;
                netResult = "";

                try {
                    JSONObject jsonData = new JSONObject(data);


                    JSONObject data_object = jsonData.getJSONObject("data");
                    // current_id.setText("ID: " + data_object.getString("id"));
                    order_code.setText(data_object.getString("order_code"));
                    //  deliver_date.setText("Deliver Date: " + data_object.getString("delivered_date"));
                    //  receive_date.setText("Received Date: " + data_object.getString("received_date") );
                    current_status.setText(getResources().getString(R.string.status_string)+": \n" + data_object.getString("status"));
                    current_stati= data_object.getString("status");

                    JSONArray myArray = data_object.getJSONArray("requested_products");
                    //  total_number_of_products.setText("Total Number: " + String.valueOf(myArray.length()));

                    for (int i = 0; i < myArray.length(); i++) {
                        JSONObject myObject = myArray.getJSONObject(i);
                        product_id_list.add(myObject.getString("product_id"));
                        product_name_list.add(myObject.getString("product_name"));
                        product_ordered_quantity_list.add(myObject.getString("ordered_quantity"));
                        product_received_quantity_list.add(myObject.getString("received_quantity"));
                        product_im_url_list.add(myObject.getString("image_url"));





                        if (myObject.has("image_url")) {

                            if (!myObject.getString("image_url").equals("")) {


                                final String my_url = myObject.getString("image_url");

                                if (my_url != null) {
                                    myDBhelper myHelper = new myDBhelper(getActivity());

                                    boolean im_found = false;
                                    Cursor res = myHelper.retreive_all_images();
                                    if (res != null) {
                                        StringBuffer buffer = new StringBuffer();
                                        while (res.moveToNext()) {
                                            buffer.append("ID: " + res.getString(0) + "\n");
                                            if (res.getString(0).equals(my_url)) {
                                                im_found = true;
                                            }
                                        }
                                        Log.d(TAG, buffer.toString());
                                    }
                                    myHelper.close();


                                    if (!im_found) {


                                        Runnable runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                             ImageDownloader downloader = new ImageDownloader();
                                                try {
                                                    //  Bitmap profImage;


                                                    if (getActivity() != null) {
                                                        final Bitmap profImage = downloader.execute(my_url).get();
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                // current_sele_prod_image_view.setImageBitmap(profImage);
                                                                myDBhelper myHelper = new myDBhelper(getActivity());

                                                                Bitmap bitmap = profImage;
                                                                if (bitmap != null) {
                                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                                    byte[] byteArray = stream.toByteArray();

                                                                    myHelper.addEntry(my_url, byteArray);

                                                                    if (adapter!=null) {
                                                                      adapter.notifyDataSetChanged();
                                                                    }
                                                                }

                                                                myHelper.close();
                                                            }


                                                        });
                                                    }

                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                } catch (ExecutionException e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        };
                                        new Thread(runnable).start();
                                    }

                                }


                            }
                        }

































                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }

                product_detail_view.setAdapter(adapter);
                //  ((NavBarSample)getActivity()).setViewPager("product_detail_view");
            }
        }
        Log.d(TAG, data);
    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
           detail_products_progress.setVisibility(View.INVISIBLE);
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_com_server_string), Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_SHORT).show();
            }
            else if (exception!=null && !exception.equals("No_internet_connection" )) {
                Log.d(TAG, exception);
                boolean check_data = false;
                String myData = exception;
                StringBuilder myString = new StringBuilder();
                for (int i = 0; i < myData.length(); i++) {
                    char c = myData.charAt(i);
                    if (c == '{') {
                        check_data = true;
                    }
                    if (check_data == true) {
                        myString.append(c);
                    }
                }
                try {
                    JSONObject myObject = new JSONObject(myString.toString());
                    String mess = myObject.getJSONObject("error").getString("message");
                    Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
                }catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getResources().getString(R.string.something_went_string), Toast.LENGTH_SHORT).show();
                }


            }

        }
    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return product_name_list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.request_product_detail_custom_list_item,null);

            TextView nameTextView = (TextView) view.findViewById(R.id.request_view_detail_updated_product_name_text_view);
           // TextView pro_id_view = (TextView) view.findViewById(R.id.custom_request_product_id_list);
            TextView ordered_qua_view = (TextView) view.findViewById(R.id.request_product_detail_custom_list_order_qnty_text_view);
            TextView received_qua_view= (TextView) view.findViewById(R.id.request_product_custom_updated_receive_qnty_text_view);
            nameTextView.setText(product_name_list.get(i));
            ImageView cur_im_view = (ImageView) view.findViewById(R.id.imageView13);
            cur_im_view.setImageDrawable(getResources().getDrawable(R.drawable.default_product));
           // pro_id_view.setText("ID: " + product_id_list.get(i));
            ordered_qua_view.setText(getResources().getString(R.string.ordered_quantity)+ ": " + product_ordered_quantity_list.get(i));
            received_qua_view.setText(getResources().getString(R.string.received_quanity)+ ": " + product_received_quantity_list.get(i));



            myDBhelper myHelper = new myDBhelper(getActivity());

            boolean im_found=false;
            Cursor res = myHelper.retreive_all_images();
            if (res != null) {
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("ID: " + res.getString(0) + "\n");
                    if (res.getString(0).equals(product_im_url_list.get(i)))
                    {
                        byte[] image = res.getBlob(1);



                       cur_im_view.setImageBitmap( BitmapFactory.decodeByteArray(image, 0, image.length));
                    }
                }
                Log.d(TAG, buffer.toString());
            }
            myHelper.close();






            return view;
        }
    }


    public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                return bitmap;
            }catch (MalformedURLException e)
            {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
























}
