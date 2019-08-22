package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class products_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "products_fragment_view";
    GetRawData getRawData;
    ListView myView;
    String stockCode="";
    ArrayList<String> productNameList;
    ArrayList<String> productCostList;
    ArrayList<String> productStockCodeList;
    ArrayList<String> batch_list;
    SharedPreferences mySharedPreferences;
    String access_token="";
    ArrayList<String> product_im_url_list;
    ProgressBar circular_progress;
    boolean no_prods_assigned=false;
    boolean isDownloading=false;
    int page_number=1;
    customAdapter CustomAdapter;
    int current_list_return_count=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_listView);
        productNameList = new ArrayList<String>();
        productCostList = new ArrayList<String>();
        productStockCodeList = new ArrayList<String>();
        CustomAdapter = new customAdapter();
        batch_list = new ArrayList<String>();
        product_im_url_list = new ArrayList<String>();
        myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        circular_progress.setVisibility(View.VISIBLE);
        getRawData = new GetRawData(this, getActivity());
        isDownloading=true;
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/?page=1&limit=10",access_token);
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
        if (getActivity()!=null) {
            ((NavBarSample) getActivity()).setItemChecked("assigned");
        }

        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        circular_progress.setVisibility(View.INVISIBLE);
        isDownloading = false;
        Log.d(TAG, data);
        String myData = data;
        boolean check_data = false;
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
            JSONObject jsonData = new JSONObject(myString.toString());
            JSONArray itemsArray = jsonData.getJSONArray("data");
            current_list_return_count = itemsArray.length();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject currentObject = itemsArray.getJSONObject(i);
                String stockCode = currentObject.getString("id");
                //String productName= itemsArray.getJSONObject(i).getJSONArray("assigned_products").getJSONObject(0).getString("product_name");
                //String productPrice=itemsArray.getJSONObject(i).getJSONArray("assigned_products").getJSONObject(0).getString("buying_price");
                String productName = currentObject.getString("product_name");
                String productPrice = currentObject.getString("buying_price");
                productStockCodeList.add(stockCode);
                productNameList.add(productName);
                productCostList.add(productPrice);
                batch_list.add(currentObject.getString("batch_no"));
                product_im_url_list.add(currentObject.getString("image_url"));


                if (currentObject.has("image_url")) {

                    if (!currentObject.getString("image_url").equals("")) {


                        final String my_url = currentObject.getString("image_url");

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
                                                            if (CustomAdapter!=null) {
                                                                CustomAdapter.notifyDataSetChanged();
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
                //Toast.makeText(getActivity(), "No url", Toast.LENGTH_SHORT).show();


            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, data);
        /*
        Log.d(TAG, String.valueOf(productStockCodeList.size()));
        customAdapter CustomAdapter = new customAdapter();
        myView.setAdapter(CustomAdapter);
     ///   ((NavBarSample)getActivity()).setViewPager("assigned_products_fragment");
        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_assigned",productStockCodeList.get(i));
            }
        });
        */
        if (page_number == 1) {
            //  Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
            myView.setAdapter(CustomAdapter);
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //   ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",productStockCodeList.get(i));
                    //  ((NavBarSample) getActivity()).setupViewPagerInvoiceDetail(invoiceNumberList.get(i));
                    // ((Order_activity_final) getActivity()).setupOrderDetail("orders_detail_fragment", order_id_list.get(i));
                    // ((Order_activity_final) getActivity()).setCustomerData(cust_email.get(i),cust_address.get(i),order_customer_name_list.get(i));
                    ((NavBarSample) getActivity()).setupViewPagerDetail("productViewDetail_assigned", productStockCodeList.get(i));
                    Log.d(TAG, "Item got Clicked");
                }
            });
        } else {
            if (myView != null) {
                myView.setAdapter(CustomAdapter);
                CustomAdapter.notifyDataSetChanged();
                myView.setSelection(((page_number - 1) * 10) - 1);
            }

        }

        myView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount - visibleItemCount == firstVisibleItem){
                    View v =  myView.getChildAt(totalItemCount-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the bottom:
                        Log.d(TAG, "Reached Bottom!");
                        if (isDownloading==false && current_list_return_count==10 && myView.getCount()>=10 && no_prods_assigned==false)
                        {
                            if (getActivity()!=null) {
                                isDownloading = true;
                                //    Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                                page_number=page_number+1;
                                getRawData = new GetRawData(products_fragment_view.this, getActivity());
                                circular_progress.setVisibility(View.VISIBLE);
                                //   getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
                                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/?page=" +String.valueOf(page_number)+"&limit=10", access_token);
                            }

                        }
                        return;
                    }

                }
            }




        });


    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            no_prods_assigned=true;
            isDownloading=false;
            circular_progress.setVisibility(View.INVISIBLE);
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


    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productStockCodeList.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_product_list_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView nameTextView = (TextView) view.findViewById(R.id.name_list);
            TextView StockCodeTextView = (TextView) view.findViewById(R.id.product_list);
            TextView costTextView = (TextView) view.findViewById(R.id.cost_list);
            StockCodeTextView.setText(productStockCodeList.get(i));
            nameTextView.setText(productNameList.get(i));
            costTextView.setText(productCostList.get(i));
            StockCodeTextView.setText(getResources().getString(R.string.batch_string) +": " + batch_list.get(i));
            nameTextView.setText(getResources().getString(R.string.products_small_string) + ": " + productNameList.get(i));
            costTextView.setText(getResources().getString(R.string.price_string)+": Rs." + productCostList.get(i));
            imageView.setImageResource(R.drawable.default_product);


           // setsqlImage(product_im_url_list.get(i));


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



                        imageView.setImageBitmap( BitmapFactory.decodeByteArray(image, 0, image.length));
                    }
                }
                Log.d(TAG, buffer.toString());
            }
            myHelper.close();





            return view;
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
                      //  current_sele_prod_image_view.setImageBitmap( BitmapFactory.decodeByteArray(image, 0, image.length));
                    }
                }
                Log.d(TAG, buffer.toString());
            }
            myHelper.close();



        }
    }
}
