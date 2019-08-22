package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class all_products_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "products_detail_fragmen";
    GetRawData getRawData;
    String product_stock_code="";
    SharedPreferences mySharedPreferences;
    ArrayList<String> productNameList;
    ArrayList<String> productCostList;
    ArrayList<String> assigned_products_StockCodeList;
    ArrayList<String> returned_products_StockCodeList;
    ArrayList<String> batch_list;
    ArrayList<String> im_url_list;
    String access_token="";
    String currentType;
    TextView dept_name;
    TextView total_number;
    TextView stock_detail;
    TextView depID;
    TextView product_array_details;
    String netResult="";
    boolean isDownloadRunning=false;
    ConstraintLayout first_view;
    ConstraintLayout second_view;
    ConstraintLayout third_view;
    ConstraintLayout fourth_view;
    ConstraintLayout fifth_view;
    ConstraintLayout sixth_view;
    int textCount=3;
    boolean downloadStatus=false;
    TextView View3;
    TextView View4;
    TextView View5;
    TextView View6;
    TextView View7;
    TextView View8;
    TextView View9;
    TextView View10;
    TextView View11;
    TextView View12;
    TextView View13;
    TextView View14;
    TextView View15;
    TextView View16;
    TextView View17;
    TextView View18;
    TextView View19;
    TextView View20;
    TextView View21;
    TextView upperSee;
    TextView lowerSee;
    ProgressBar circular_bar;

    ImageView im_1;
    ImageView im_2;
    ImageView im_3;
    ImageView im_4;
    ImageView im_5;
    ImageView im_6;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_products,container,false);
        upperSee = (TextView) view.findViewById(R.id.upper_see_all);
        lowerSee = (TextView) view.findViewById(R.id.lower_see_all);
        first_view = (ConstraintLayout) view.findViewById(R.id.first_small_constraint);
        second_view = (ConstraintLayout) view.findViewById(R.id.second_small_constraint);
        third_view = (ConstraintLayout) view.findViewById(R.id.third_small_constraint);
        fourth_view= (ConstraintLayout) view.findViewById(R.id.fourth_small_constraint);
        fifth_view = (ConstraintLayout) view.findViewById(R.id.fifth_small_constraint);
        sixth_view = (ConstraintLayout) view.findViewById(R.id.sixth_small_constraint);
        im_url_list = new ArrayList<String>();
        for (int x=0;x<5;x++)
        {
            im_url_list.add("");
        }
        im_1 = (ImageView) view.findViewById(R.id.imageView_1);
        im_2 = (ImageView) view.findViewById(R.id.imageView_2);
        im_3 = (ImageView) view.findViewById(R.id.textView_9);
        im_4 = (ImageView) view.findViewById(R.id.imageView_4);
        im_5 = (ImageView) view.findViewById(R.id.imageView_5);
        im_6 = (ImageView) view.findViewById(R.id.imageView_6);

        View3 = (TextView) view.findViewById(R.id.textView_3);
        View4 = (TextView) view.findViewById(R.id.textView_4);
        View5 = (TextView) view.findViewById(R.id.textView_5);
        View6 = (TextView) view.findViewById(R.id.textView_6);
        View7 = (TextView) view.findViewById(R.id.textView_7);
        View8 = (TextView) view.findViewById(R.id.textView_8);
        View10 = (TextView) view.findViewById(R.id.textView_10);
        View11 = (TextView) view.findViewById(R.id.textView_11);
        View12 = (TextView) view.findViewById(R.id.textView_12);
        View13 = (TextView) view.findViewById(R.id.textView_13);
        View14 = (TextView) view.findViewById(R.id.textView_14);
        View15 = (TextView) view.findViewById(R.id.textView_15);
        View16 = (TextView) view.findViewById(R.id.textView_16);
        View17 = (TextView) view.findViewById(R.id.textView_17);
        View18 = (TextView) view.findViewById(R.id.textView_18);
        View19 = (TextView) view.findViewById(R.id.textView_19);
        View20 = (TextView) view.findViewById(R.id.textView_20);
        View21 = (TextView) view.findViewById(R.id.textView_21);

        first_view.setVisibility(View.INVISIBLE);
        second_view.setVisibility(View.INVISIBLE);
        third_view.setVisibility(View.INVISIBLE);
        fourth_view.setVisibility(View.INVISIBLE);
       fifth_view.setVisibility(View.INVISIBLE);
       sixth_view.setVisibility(View.INVISIBLE);
        assigned_products_StockCodeList = new ArrayList<String>();
        returned_products_StockCodeList = new ArrayList<String>();
        batch_list= new ArrayList<String>();
       first_view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",returned_products_StockCodeList.get(0));

           }
       });
        second_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",returned_products_StockCodeList.get(1));

            }
        });
        third_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",returned_products_StockCodeList.get(2));

            }
        });
        fourth_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_assigned",assigned_products_StockCodeList.get(0));

            }
        });
        fifth_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_assigned",assigned_products_StockCodeList.get(1));

            }
        });
        sixth_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_assigned",assigned_products_StockCodeList.get(2));

            }
        });
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        circular_bar = (ProgressBar) view.findViewById(R.id.allproducts_progressBar);
        /*
        dept_name = (TextView) view.findViewById(R.id.assignee_of_products);
        depID = (TextView) view.findViewById(R.id.dept_id);
        stock_detail = (TextView) view.findViewById(R.id.stock_code_details);
        total_number = (TextView) view.findViewById(R.id.total_number_of_products);
        product_array_details = (TextView) view.findViewById(R.id.products_array);
        productNameList = new ArrayList<String>();
        productCostList = new ArrayList<String>();
        productStockCodeList = new ArrayList<String>();
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        product_stock_code = ((NavBarSample)getActivity()).getStockCodeDetail();
        getRawData = new GetRawData(this);
        if (((NavBarSample)getActivity()).fragmentCurrentType().equals("assigned_products_fragment")) {
            currentType="assigned_products";
            getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/", access_token);
            isDownloadRunning=true;
        }
        else if (((NavBarSample)getActivity()).fragmentCurrentType().equals("returned_products_fragment"))
        {
            getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products", access_token);
            currentType="returned_products";
            isDownloadRunning=true;
        }
        */
        upperSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPager(((NavBarSample)getActivity()).viewPager,"returned_products_fragment");
                 Log.d(TAG, "I came here");
            }
        });
        lowerSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavBarSample)getActivity()).setupViewPager(((NavBarSample)getActivity()).viewPager,"assigned_products_fragment");
            }
        });
        circular_bar.setVisibility(View.VISIBLE);
        downloadStatus=false;
        getRawData = new GetRawData(this,getActivity());
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products", access_token);
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
        if (!getUserVisibleHint()) {
            Log.d(TAG, "Second Resume Inside");
            return;
        }
        if (getActivity()!=null) {
        ((NavBarSample)getActivity()).setItemChecked("products");
        if (isDownloadRunning==false) {
            downloadStatus = false;
            if (assigned_products_StockCodeList!=null)
            {
                assigned_products_StockCodeList.clear();
            }
            else if (returned_products_StockCodeList!=null)
            {
                returned_products_StockCodeList.clear();
            }
            getRawData = new GetRawData(this, getActivity());
            getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products", access_token);
            circular_bar.setVisibility(View.VISIBLE);
        }
/*
        if (isDownloadRunning==false) {
        Log.d(TAG, "Resume Downloaded Started");
        product_stock_code = ((NavBarSample) getActivity()).getStockCodeDetail();
        Log.d(TAG, "Resume " + product_stock_code);
        getRawData = new GetRawData(this);

        if (((NavBarSample) getActivity()).fragmentCurrentType().equals("assigned_products_fragment")) {
               currentType = "assigned_products";
               getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/", access_token);
        } else if (((NavBarSample) getActivity()).fragmentCurrentType().equals("returned_products_fragment")) {
            getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products", access_token);
            currentType = "returned_products";
        }
        }
        */
            first_view.setVisibility(View.INVISIBLE);
            second_view.setVisibility(View.INVISIBLE);
            third_view.setVisibility(View.INVISIBLE);
            fourth_view.setVisibility(View.INVISIBLE);
            fifth_view.setVisibility(View.INVISIBLE);
            sixth_view.setVisibility(View.INVISIBLE);

        }

        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
            circular_bar.setVisibility(View.INVISIBLE);
            isDownloadRunning = false;
            boolean check_data=false;
            StringBuilder myString = new StringBuilder();
            for (int i = 0; i < data.length(); i++) {
                char c = data.charAt(i);
                if (c == '{') {
                    check_data = true;
                }
                if (check_data == true) {
                    myString.append(c);
                }
            }
            if (downloadStatus == false) {
                try {
                    JSONObject jsonData = new JSONObject(myString.toString());
                    JSONArray itemsArray = jsonData.getJSONArray("data");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject currentObject = itemsArray.getJSONObject(i);
                      //  String stockCode = currentObject.getString("stock_code");
                      //  String productName = itemsArray.getJSONObject(i).getJSONArray("returned_products").getJSONObject(0).getString("product_name");
                      //  String productPrice = itemsArray.getJSONObject(i).getJSONArray("returned_products").getJSONObject(0).getString("buying_price");
                        String stockCode = currentObject.getString("id");
                        //  String productName = itemsArray.getJSONObject(i).getJSONArray("returned_products").getJSONObject(0).getString("product_name");
                        //  String productPrice = itemsArray.getJSONObject(i).getJSONArray("returned_products").getJSONObject(0).getString("buying_price");
                        String productName = currentObject.getString("product_name");
                        String productPrice = currentObject.getString("returned_quantity");
                        returned_products_StockCodeList.add(stockCode);
                        String batch_no = currentObject.getString("batch_no");
                        String image_url="";
                        if (currentObject.has("image_url"))
                        {
                            if (!currentObject.getString("image_url").equals(""))
                            {
                                image_url= currentObject.getString("image_url");
                            }
                        }
                        if (i == 0) {
                            View3.setText(productName);
                            View4.setText(getResources().getString(R.string.batch_string)+": " + batch_no);
                            View5.setText(getResources().getString(R.string.QNT_string)+ ": " + productPrice);
                            first_view.setVisibility(View.VISIBLE);
                            if (!image_url.equals(""))
                            {
                              //  im_url_list.set(0,image_url);
                                im_1.setImageBitmap(setsqlImage(image_url));
                              //  setsqlImage(image_url);
                               // check_sql_image(image_url);
                            }

                        } else if (i == 1) {
                            View6.setText(productName);
                            View7.setText(getResources().getString(R.string.batch_string)+": " + batch_no);
                            View8.setText(getResources().getString(R.string.QNT_string)+ ": " + productPrice);
                            second_view.setVisibility(View.VISIBLE);
                            if (!image_url.equals(""))
                            {
                             //   im_url_list.set(1,image_url);
                                im_2.setImageBitmap(setsqlImage(image_url));
                              //  setsqlImage(image_url);
                               // check_sql_image(image_url);
                            }

                        } else if (i == 2) {
                            View10.setText(productName);
                            View11.setText(getResources().getString(R.string.batch_string)+": " + batch_no);
                            View12.setText(getResources().getString(R.string.QNT_string)+ ": " + productPrice);
                            third_view.setVisibility(View.VISIBLE);
                            if (!image_url.equals(""))
                            {
                             //   im_url_list.set(2,image_url);
                                im_3.setImageBitmap(setsqlImage(image_url));
                               // setsqlImage(image_url);
                                //check_sql_image(image_url);
                            }

                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                if (getActivity() != null) {
                    getRawData = new GetRawData(this, getActivity());
                    circular_bar.setVisibility(View.VISIBLE);
                    getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/assigned_products/", access_token);
                    downloadStatus = true;
                }
            } else if (downloadStatus == true) {
                try {
                    JSONObject jsonData = new JSONObject(myString.toString());
                    JSONArray itemsArray = jsonData.getJSONArray("data");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject currentObject = itemsArray.getJSONObject(i);
                       // String stockCode = currentObject.getString("stock_code");
                       // String productName = itemsArray.getJSONObject(i).getJSONArray("assigned_products").getJSONObject(0).getString("product_name");
                       // String productPrice = itemsArray.getJSONObject(i).getJSONArray("assigned_products").getJSONObject(0).getString("buying_price");
                        String stockCode= currentObject.getString("id");
                        //String productName= itemsArray.getJSONObject(i).getJSONArray("assigned_products").getJSONObject(0).getString("product_name");
                        //String productPrice=itemsArray.getJSONObject(i).getJSONArray("assigned_products").getJSONObject(0).getString("buying_price");
                        String productName = currentObject.getString("product_name");
                        String productPrice = currentObject.getString("buying_price");
                        assigned_products_StockCodeList.add(stockCode);
                        String batch_no= currentObject.getString("batch_no");
                        String image_url="";
                        if (currentObject.has("image_url"))
                        {
                            if (!currentObject.getString("image_url").equals(""))
                            {
                                image_url= currentObject.getString("image_url");

                            }
                        }
                        if (i == 0) {
                            View13.setText(productName);
                            View14.setText(getResources().getString(R.string.batch_string)+": " + batch_no);
                            View15.setText(getResources().getString(R.string.price_string)+": " + productPrice);
                            fourth_view.setVisibility(View.VISIBLE);
                            if (!image_url.equals(""))
                            {
                             //   im_url_list.set(4,image_url);
                                im_4.setImageBitmap(setsqlImage(image_url));
                               // setsqlImage(image_url);
                              //  check_sql_image(image_url);
                            }

                        } else if (i == 1) {
                            View16.setText(productName);
                            View17.setText(getResources().getString(R.string.batch_string)+": " + batch_no);
                            View18.setText(getResources().getString(R.string.price_string)+": " + productPrice);
                            fifth_view.setVisibility(View.VISIBLE);
                            if (!image_url.equals(""))
                            {
//                                im_url_list.set(5,image_url);
                                im_5.setImageBitmap(setsqlImage(image_url));
                              //  setsqlImage(image_url);
                                //check_sql_image(image_url);
                            }
                        } else if (i == 2) {
                            View19.setText(productName);
                            View20.setText(getResources().getString(R.string.batch_string)+": " + batch_no);
                            View21.setText(getResources().getString(R.string.price_string)+": " + productPrice);
                            sixth_view.setVisibility(View.VISIBLE);
                            if (!image_url.equals(""))
                            {
                              //  im_url_list.set(6,image_url);
                                im_6.setImageBitmap(setsqlImage(image_url));
                               // setsqlImage(image_url);
                                //check_sql_image(image_url);
                            }
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            circular_bar.setVisibility(View.INVISIBLE);
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getActivity(), "Error Communicating with server!", Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getActivity(), "No internet Connection!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Something Went Wrong, Try Again!", Toast.LENGTH_SHORT).show();
                }


            }

        }
    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return assigned_products_StockCodeList.size();
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
            TextView costTextView = (TextView) view.findViewById(R.id.product_list);
            StockCodeTextView.setText(assigned_products_StockCodeList.get(i));
            nameTextView.setText(productNameList.get(i));
            costTextView.setText(productCostList.get(i));
            imageView.setImageResource(R.drawable.desktop_pic_flat);
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
                                            setsqlImage(my_url);
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


                setsqlImage(my_url);

            }


        }



    }

    public Bitmap setsqlImage(String fou_url)
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
                    im_found=true;
                    byte[] image = res.getBlob(1);

                   return BitmapFactory.decodeByteArray(image, 0, image.length);

                }
            }
            Log.d(TAG, buffer.toString());
        }
        myHelper.close();
        return BitmapFactory.decodeResource(this.getResources(), R.drawable.default_product);






    }







}
