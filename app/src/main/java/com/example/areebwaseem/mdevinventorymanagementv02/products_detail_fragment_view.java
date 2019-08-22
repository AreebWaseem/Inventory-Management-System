package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class products_detail_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "products_detail_fragmen";
    GetRawData getRawData;
    String product_stock_code="";
    SharedPreferences mySharedPreferences;
    ArrayList<String> product_batch_no_list;
    ArrayList<String> product_name_list;
    ArrayList<String> product_id_list;
    ArrayList<String> product_quantity_list;
    ArrayList<String> product_buying_price_list;
    ArrayList<String> product_selling_price_list;
    ArrayList<String> product_returned_quantity_list;
    ImageView current_sele_prod_image_view;
    ArrayList<String> product_remarks_list;
    ArrayList<String> product_code;
    ListView product_detail_view;
    String access_token="";
    String currentType;
    TextView dept_name;
    TextView total_number;
    TextView stock_detail;
    //TextView depID;
    TextView product_array_details;
    String netResult="";
    boolean isDownloadRunning=false;
    ProgressBar detail_products_progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_product_detail_view,container,false);
        detail_products_progress=(ProgressBar) view.findViewById(R.id.progressBar_product_detail_view);
        dept_name = (TextView) view.findViewById(R.id.assignee_of_products);
        product_detail_view = (ListView) view.findViewById(R.id.product_details_list_view);
       // depID = (TextView) view.findViewById(R.id.dept_id);
        stock_detail = (TextView) view.findViewById(R.id.stock_code_details);
        total_number = (TextView) view.findViewById(R.id.total_number_of_products);
       // product_array_details = (TextView) view.findViewById(R.id.products_array);

        product_batch_no_list = new ArrayList<String>();
        product_buying_price_list = new ArrayList<String>();
        product_id_list = new ArrayList<String>();
        product_quantity_list= new ArrayList<String>();
        product_remarks_list = new ArrayList<String>();
        product_returned_quantity_list= new ArrayList<String>();
        product_selling_price_list = new ArrayList<String>();
        product_code = new ArrayList<String>();
        product_name_list = new ArrayList<String>();
        product_quantity_list = new ArrayList<String>();
        current_sele_prod_image_view = (ImageView) view.findViewById(R.id.imageView3_mine);
        current_sele_prod_image_view.setImageDrawable(getResources().getDrawable(R.drawable.default_product));
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        product_stock_code = ((NavBarSample)getActivity()).getStockCodeDetail();
        getRawData = new GetRawData(this,getActivity());
        if (((NavBarSample)getActivity()).fragmentCurrentType().equals("assigned_products_fragment")) {
            currentType="assigned_products";
            detail_products_progress.setVisibility(View.VISIBLE);
            getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/view/" + product_stock_code, access_token);
            isDownloadRunning=true;
        }
        else if (((NavBarSample)getActivity()).fragmentCurrentType().equals("returned_products_fragment"))
        {
            detail_products_progress.setVisibility(View.VISIBLE);
            getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/view/"+ product_stock_code, access_token);
            currentType="returned_products";
            isDownloadRunning=true;
        }


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
        if (!getUserVisibleHint()) {
            Log.d(TAG, "Second Resume Inside");
            return;
        }
        if (getActivity()!=null)
        {

        ((NavBarSample)getActivity()).setItemChecked("products");
        if (((NavBarSample) getActivity()).fragmentCurrentType().equals("assigned_products_fragment"))
        {
            ((NavBarSample)getActivity()).setItemChecked("assigned");

        }
        if (((NavBarSample) getActivity()).fragmentCurrentType().equals("returned_products_fragment"))
        {
            ((NavBarSample)getActivity()).setItemChecked("returned");
        }
        if (isDownloadRunning==false) {
            current_sele_prod_image_view.setImageDrawable(getResources().getDrawable(R.drawable.default_product));
            Log.d(TAG, "Resume Downloaded Started");
            product_stock_code = ((NavBarSample) getActivity()).getStockCodeDetail();
            Log.d(TAG, "Resume " + product_stock_code);
            getRawData = new GetRawData(this, getActivity());

            if (((NavBarSample) getActivity()).fragmentCurrentType().equals("assigned_products_fragment")) {
                currentType = "assigned_products";
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/view/" + product_stock_code, access_token);
                detail_products_progress.setVisibility(View.VISIBLE);
                isDownloadRunning = true;
            } else if (((NavBarSample) getActivity()).fragmentCurrentType().equals("returned_products_fragment")) {
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/view/" + product_stock_code, access_token);
                detail_products_progress.setVisibility(View.VISIBLE);
                currentType = "returned_products";
                isDownloadRunning = true;
            }
        }
    }

        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {



            boolean istrue = false;
            if (product_batch_no_list != null) {
                product_batch_no_list.clear();
            }

            if (product_buying_price_list != null) {
                product_buying_price_list.clear();
            }

            if (product_quantity_list != null) {
                product_quantity_list.clear();
            }
            if (product_remarks_list != null) {
                product_remarks_list.clear();
            }
            if (product_returned_quantity_list != null) {
                product_batch_no_list.clear();
            }
            if (product_selling_price_list != null) {
                product_selling_price_list.clear();
            }
            if (product_code != null) {
                product_code.clear();
            }
            if (product_name_list != null) {
                product_name_list.clear();
            }
            if (product_selling_price_list != null) {
                product_selling_price_list.clear();
            }
            if (product_quantity_list != null) {
                product_quantity_list.clear();
            } else {
                istrue = true;
            }
            if (istrue == false) {
                product_batch_no_list = new ArrayList<String>();
                product_buying_price_list = new ArrayList<String>();
                product_id_list = new ArrayList<String>();
                product_quantity_list = new ArrayList<String>();
                product_remarks_list = new ArrayList<String>();
                product_returned_quantity_list = new ArrayList<String>();
                product_selling_price_list = new ArrayList<String>();
                product_code = new ArrayList<String>();
                product_name_list = new ArrayList<String>();
                product_quantity_list = new ArrayList<String>();
            }
            detail_products_progress.setVisibility(View.INVISIBLE);
            isDownloadRunning = false;
            int currentIndex = 0;
            netResult = "";
            String myData = data;
            boolean check_data=false;
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
            if (currentType.equals("returned_products")) {



                try {
                    JSONObject jsonData = new JSONObject(myString.toString());
                    //    JSONArray itemsArray = jsonData.getJSONArray("data");
                    final JSONObject dataObject = jsonData.getJSONObject("data");
                    // depID = (TextView) view.findViewById(R.id.dept_id);
                    stock_detail.setText(getResources().getString(R.string.batch_string)+": " + dataObject.getString("batch_no"));
                   // JSONArray returned_products = dataObject.getJSONArray("returned_products");
                    dept_name.setText(getResources().getString(R.string.product_string)+": " + dataObject.getString("product_name"));
                    total_number.setText(getResources().getString(R.string.qnty_string)+": " + dataObject.getString("returned_quantity") + "\n" + getResources().getString(R.string.date_string)+": " + dataObject.getString("returned_date"));
                     if (dataObject.has("image_url"))
                     {
                         if (!dataObject.getString("image_url").equals(""))
                         {
                             check_sql_image(dataObject.getString("image_url"));
                         }



                     }
                    /*
                    stock_detail.setText("Stock Code: " + dataObject.getString("stock_code"));
                    JSONArray returned_products = dataObject.getJSONArray("returned_products");
                    dept_name.setText("Assigned by: " + dataObject.getString("assigned_by"));
                    total_number.setText("Total Number: " + String.valueOf(returned_products.length()));

                    for (int i = 0; i < returned_products.length(); i++) {
                        product_batch_no_list.add(returned_products.getJSONObject(i).getString("batch_no"));
                        product_buying_price_list.add(returned_products.getJSONObject(i).getString("buying_price"));
                        product_id_list.add(returned_products.getJSONObject(i).getString("product_id"));
                        product_quantity_list.add(returned_products.getJSONObject(i).getString("product_quantity"));
                        product_remarks_list.add(returned_products.getJSONObject(i).getString("remarks"));
                        product_returned_quantity_list.add(returned_products.getJSONObject(i).getString("returned_quantity"));
                        product_selling_price_list.add(returned_products.getJSONObject(i).getString("selling_price"));
                        product_code.add(returned_products.getJSONObject(i).getString("product_code"));
                        product_name_list.add(returned_products.getJSONObject(i).getString("product_name"));
                    }
                    */
                /*
                JSONObject currentObject = itemsArray.getJSONObject(currentIndex);
                stock_detail.setText("Stock Code: " + currentObject.getString("stock_code"));
                dept_name.setText("Assigned by: " + currentObject.getString("assigned_by"));
              //  depID.setText("Department ID:  " + currentObject.getString("department_id"));
                total_number.setText("Total Number: " + String.valueOf(itemsArray.getJSONObject(currentIndex).getJSONArray("returned_products").length()));
                      // netResult= netResult + "\n";
                        JSONArray myArray = currentObject.getJSONArray("returned_products");
                        for (int i = 0; i < myArray.length(); i++) {
                            JSONObject myObject = myArray.getJSONObject(i);
                            Iterator iterator1 = myObject.keys();
                            while (iterator1.hasNext()) {
                                String myKey = (String) iterator1.next();
                                String myToString = myObject.getString(myKey);
                                for (int x=0; x<myKey.length();x++)
                                {
                                    if (myKey.charAt(x)=='_')
                                    {
                                        StringBuilder myName = new StringBuilder(myKey);
                                        myName.setCharAt(x, ' ');
                                        myKey = myName.toString();
                                        Log.d(TAG, "I came to Replaceeeeeeeeeeeeeee");
                                    }
                                }
                                StringBuilder key_to_uppercase = new StringBuilder(myKey);
                                key_to_uppercase.setCharAt(0, Character.toUpperCase(myKey.charAt(0)));
                                myKey=key_to_uppercase.toString();
                                netResult = netResult + (myKey + " : " + myToString + "\n");
                                Log.d(TAG, myToString);
                            }
                            netResult = netResult + "\n";
                        }
                      //  product_array_details.setText(netResult);

*/
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else if (currentType.equals("assigned_products")) {
                try {

                    JSONObject jsonData = new JSONObject(myString.toString());
                    //    JSONArray itemsArray = jsonData.getJSONArray("data");
                    JSONObject dataObject = jsonData.getJSONObject("data");
                    stock_detail.setText(getResources().getString(R.string.batch_string)+ ": " + dataObject.getString("batch_no"));
                    // JSONArray returned_products = dataObject.getJSONArray("returned_products");
                    dept_name.setText(getResources().getString(R.string.product_string)+ ": " + dataObject.getString("product_name"));
                    total_number.setText(getResources().getString(R.string.qnty_string)+ ": " + dataObject.getString("product_quantity")  + "\n" + getResources().getString(R.string.selling_string)+ ": " + dataObject.getString("selling_price"));
                     //       + "\n" + "Remarks: " + dataObject.getString("remarks") + "\n" );


                    ///////////////////// Get Image ////////////////////////////////////


                    if (dataObject.has("image_url"))
                    {
                        if (!dataObject.getString("image_url").equals(""))
                        {
                            check_sql_image(dataObject.getString("image_url"));
                        }



                    }

/*

                    final String my_url = dataObject.getString("image_url");
                    if (my_url!=null) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                ImageDownloader downloader = new ImageDownloader();
                                try {
                                    //  Bitmap profImage;

                                    final Bitmap  profImage = downloader.execute(my_url).get();
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                current_sele_prod_image_view.setImageBitmap(profImage);


                                                myDBhelper myHelper = new myDBhelper(getActivity());

                                                boolean im_found=false;
                                                Cursor res = myHelper.retreive_all_images();
                                                if (res != null) {
                                                    StringBuffer buffer = new StringBuffer();
                                                    while (res.moveToNext()) {
                                                        buffer.append("ID: " + res.getString(0) + "\n");
                                                        if (res.getString(0).equals(my_url))
                                                        {
                                                            im_found=true;
                                                        }
                                                    }
                                                   Log.d(TAG, buffer.toString());
                                                }
                                                if (!im_found) {
                                                    Bitmap bitmap = profImage;
                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                    byte[] byteArray = stream.toByteArray();

                                                    myHelper.addEntry(my_url, byteArray);
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
                    else {
                        Toast.makeText(getActivity(), "No url", Toast.LENGTH_SHORT).show();
                    }

                    */
                    /*
                    JSONArray returned_products = dataObject.getJSONArray("assigned_products");
                    stock_detail.setText("Stock Code: " + dataObject.getString("stock_code"));
                    dept_name.setText("Assigned by: " + dataObject.getString("assigned_by"));
                    total_number.setText("Total Number: " + String.valueOf(returned_products.length()));
                    for (int i = 0; i < returned_products.length(); i++) {
                        product_batch_no_list.add(returned_products.getJSONObject(i).getString("batch_no"));
                        product_buying_price_list.add(returned_products.getJSONObject(i).getString("buying_price"));
                        product_id_list.add(returned_products.getJSONObject(i).getString("product_id"));
                        product_quantity_list.add(returned_products.getJSONObject(i).getString("product_quantity"));
                        product_remarks_list.add(returned_products.getJSONObject(i).getString("remarks"));
                        product_returned_quantity_list.add("none");
                        product_selling_price_list.add(returned_products.getJSONObject(i).getString("selling_price"));
                        product_code.add(returned_products.getJSONObject(i).getString("product_code"));
                        product_quantity_list.add(returned_products.getJSONObject(i).getString("product_quantity"));
                        product_name_list.add(returned_products.getJSONObject(i).getString("product_name"));
                    }
                    */
                /*
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("data");
                for (int i=0; i<itemsArray.length();i++)
                {
                    if (itemsArray.getJSONObject(i).getString("stock_code").equals(product_stock_code)){
                        currentIndex=i;
                    }
                }
                JSONObject currentObject = itemsArray.getJSONObject(currentIndex);
                stock_detail.setText("Stock Code: " + currentObject.getString("stock_code"));
                dept_name.setText("Assigned by: " + currentObject.getString("assigned_by"));
               // depID.setText("Department ID:  " + currentObject.getString("department_id"));
                total_number.setText("Total Number: " + String.valueOf(itemsArray.getJSONObject(currentIndex).getJSONArray("assigned_products").length()));
                // netResult= netResult + "\n";
                JSONArray myArray = currentObject.getJSONArray("assigned_products");
                for (int i = 0; i < myArray.length(); i++) {
                    JSONObject myObject = myArray.getJSONObject(i);
                    Iterator iterator1 = myObject.keys();
                    while (iterator1.hasNext()) {
                        String myKey = (String) iterator1.next();
                        String myToString = myObject.getString(myKey);
                        for (int x=0; x<myKey.length();x++)
                        {
                            if (myKey.charAt(x)=='_')
                            {
                                StringBuilder myName = new StringBuilder(myKey);
                                myName.setCharAt(x, ' ');
                                myKey = myName.toString();
                                Log.d(TAG, "I came to Replaceeeeeeeeeeeeeee");
                            }
                        }
                        StringBuilder key_to_uppercase = new StringBuilder(myKey);
                        key_to_uppercase.setCharAt(0, Character.toUpperCase(myKey.charAt(0)));
                        myKey=key_to_uppercase.toString();
                        netResult = netResult + (myKey + " : " + myToString + "\n");
                        Log.d(TAG, myToString);
                    }
                    netResult = netResult + "\n";

                }
              //  product_array_details.setText(netResult);

*/
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }


            }
            customAdapter adapter = new customAdapter();
            product_detail_view.setAdapter(adapter);
            //  ((NavBarSample)getActivity()).setViewPager("product_detail_view");
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
            view = getLayoutInflater().inflate(R.layout.custom_product_detail_list_item,null);

            TextView nameTextView = (TextView) view.findViewById(R.id.product_detail_name_text_view);
            TextView product_detail_code = (TextView) view.findViewById(R.id.product_detail_code_text_view);
            TextView batch_no_view = (TextView) view.findViewById(R.id.product_batch_no_text_view);
            //TextView pro_id_view = (TextView) view.findViewById(R.id.product_id_text_view);
            TextView buying_view = (TextView) view.findViewById(R.id.product_detail_buying_view);
            TextView selling_view= (TextView) view.findViewById(R.id.product_selling_text_view);
            TextView returned_assigned_view = (TextView) view.findViewById(R.id.product_detail_returned_view);
            TextView remarks_view = (TextView) view.findViewById(R.id.product_detail_remarks_text_view);
            nameTextView.setText("Product Name: " + product_name_list.get(i));
            product_detail_code.setText("Product Code: " + product_code.get(i));
            batch_no_view.setText("Batch No: " + product_batch_no_list.get(i));
          //  pro_id_view.setText("ID: " + product_id_list.get(i));
            buying_view.setText("Buying Price: " + product_buying_price_list.get(i));
            selling_view.setText("Selling Price: " + product_selling_price_list.get(i));
            if (product_returned_quantity_list.get(0)!="none")
            {
                returned_assigned_view.setText("Product Quantity: " + product_returned_quantity_list.get(i) + "\n" + "Returned Quantity: " + product_returned_quantity_list.get(i) );
            }
            else {
                returned_assigned_view.setText("Product Quantity: " +  product_quantity_list.get(i));
            }
            remarks_view.setText("Remarks: " + product_remarks_list.get(i));

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

    public void check_sql_image(String im_url)
    {


        final String my_url = im_url;

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
                                        }

                                        myHelper.close();


                                        setsqlImage(my_url);
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
            else {


            setsqlImage(my_url);

            }


        }



    }

    public void setsqlImage(String fou_url)
    {

        myDBhelper myHelper = new myDBhelper(getActivity());

        boolean im_found=false;
        Cursor res = myHelper.retreive_all_images();
        if (res != null) {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("ID: " + res.getString(0) + "\n");
                if (res.getString(0).equals(fou_url))
                {
                    byte[] image = res.getBlob(1);
                    current_sele_prod_image_view.setImageBitmap( BitmapFactory.decodeByteArray(image, 0, image.length));
                }
            }
            Log.d(TAG, buffer.toString());
        }
        myHelper.close();



    }



}
