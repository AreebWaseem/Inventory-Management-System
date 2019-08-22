package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class profile_view_fragment extends Fragment implements GetRawData.OnDownloadComplete, LoginPostRequest.OnPostComplete{
    private static final String TAG = "profile_view_fragment";
    GetRawData getRawData;
    String product_stock_code="";
    LoginPostRequest myUpdateRequest;
    SharedPreferences mySharedPreferences;
    ArrayList<String> productNameList;
    ArrayList<String> productCostList;
    ArrayList<String> productStockCodeList;
    String access_token="";
    String currentType;
    TextView userName;
    EditText name_change;
    EditText old_pass;
    EditText new_pass;
    NetworkInfo networkInfo;
    ConnectivityManager connMgr;
    TextView profile_Details;
    boolean isDownloadRunning=false;
    ImageView profile_image;
    Bitmap profImage;
    Button update_button;
ProgressBar myBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main3,container,false);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        userName = (TextView) view.findViewById(R.id.profile_view_name);
        profile_Details = (TextView) view.findViewById(R.id.profile_detail_text_view);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        myBar= (ProgressBar) view.findViewById(R.id.progressBar_profile_view);
        update_button = (Button) view.findViewById(R.id.editProfile_Button);
        name_change = (EditText) view.findViewById(R.id.name_to_change_edit_text);
        old_pass = (EditText) view.findViewById(R.id.old_password_edit_text);
        new_pass = (EditText) view.findViewById(R.id.new_password_edit_text);
         myBar.setVisibility(View.VISIBLE);

        connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageDownloader downloader = new ImageDownloader();
                    try {
                        profImage = downloader.execute(mySharedPreferences.getString("PROFILE_IMAGE", "abc")).get();
                        if (getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profile_image.setImageBitmap(profImage);
                                    myBar.setVisibility(View.INVISIBLE);
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
            Toast.makeText(getActivity(),getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_LONG).show();
        }

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
        userName.setText(mySharedPreferences.getString("NAME", "abc"));
        String profileDetails="";
        profileDetails =  getResources().getString(R.string.name_string) + ": " +   mySharedPreferences.getString("USER_NAME", "abc") + "\n" + getResources().getString(R.string.email_string)+ ": " +   mySharedPreferences.getString("USER_EMAIL", "abc") ;
        profile_Details.setText(profileDetails);
      //  + "\n" + "Department: "  +  mySharedPreferences.getString("USER_DEPARTMENT", "abc")



        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name_change.getText().toString().equals("")|| old_pass.getText().toString().equals("") || new_pass.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cant_string), Toast.LENGTH_SHORT).show();
                }
                else {
                    myBar.setVisibility(View.VISIBLE);
                 myUpdateRequest = new LoginPostRequest(profile_view_fragment.this,getActivity());
                 myUpdateRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/employeee/update", access_token, name_change.getText().toString(),old_pass.getText().toString(),new_pass.getText().toString());
                }
            }
        });




        return view;
    }

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
           myBar.setVisibility(View.INVISIBLE);
            if (s != null) {
                ((NavBarSample) getActivity()).makeToast(getResources().getString(R.string.prof_success_updated_string), true);
            }
        }
    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean connection) {
        if (getActivity()!=null) {
           myBar.setVisibility(View.INVISIBLE);
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
            ((NavBarSample) getActivity()).setItemChecked("profile");
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
        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
            isDownloadRunning = false;
            int currentIndex = 0;
            if (currentType.equals("returned_products")) {
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray itemsArray = jsonData.getJSONArray("data");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        if (itemsArray.getJSONObject(i).getString("stock_code").equals(product_stock_code)) {
                            currentIndex = i;
                        }
                    }
                    JSONObject currentObject = itemsArray.getJSONObject(currentIndex);
                /*
                stock_detail.setText("Stock Code: " + currentObject.getString("stock_code"));
                dept_name.setText("Assigned by: " + currentObject.getString("assigned_by"));
                depID.setText("Department ID:  " + currentObject.getString("department_id"));
                total_number.setText("Total Number: " + String.valueOf(itemsArray.getJSONObject(currentIndex).getJSONArray("returned_products").length()));
                */
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else if (currentType.equals("assigned_products")) {
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray itemsArray = jsonData.getJSONArray("data");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        if (itemsArray.getJSONObject(i).getString("stock_code").equals(product_stock_code)) {
                            currentIndex = i;
                        }
                    }
                    JSONObject currentObject = itemsArray.getJSONObject(currentIndex);
                /*
                stock_detail.setText("Stock Code: " + currentObject.getString("stock_code"));
                dept_name.setText("Assigned by: " + currentObject.getString("assigned_by"));
                depID.setText("Department ID:  " + currentObject.getString("department_id"));
                total_number.setText("Total Number: " + String.valueOf(itemsArray.getJSONObject(currentIndex).getJSONArray("assigned_products").length()));
                */
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
            //  ((NavBarSample)getActivity()).setViewPager("product_detail_view");
            Log.d(TAG, data);

    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            myBar.setVisibility(View.INVISIBLE);
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_com_server_string), Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_SHORT).show();
            }
            else if (exception!=null && !exception.equals("No_internet_connection" )) {
                Log.d(TAG, exception);
                Toast.makeText(getActivity(), getResources().getString(R.string.something_went_string), Toast.LENGTH_SHORT).show();
            }

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
            TextView costTextView = (TextView) view.findViewById(R.id.product_list);
            StockCodeTextView.setText(productStockCodeList.get(i));
            nameTextView.setText(productNameList.get(i));
            costTextView.setText(productCostList.get(i));
            imageView.setImageResource(R.drawable.desktop_pic_flat);
            return view;
        }
    }
}
