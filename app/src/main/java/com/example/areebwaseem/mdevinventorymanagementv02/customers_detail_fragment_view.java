package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class customers_detail_fragment_view extends Fragment implements GetRawData.OnDownloadComplete, LoginPostRequest.OnPostComplete{
    private static final String TAG = "customers_detail_fragme";
    GetRawData getRawData;
    ListView myView;
    LoginPostRequest myRequest;
    SharedPreferences mySharedPreferences;
   // TextView cust_id;
    TextView cust_name;
   // TextView cust_address;
    ProgressBar circular_progress;
    String access_token="";
    String customer_id;
    boolean isDownloadRunning=false;
    Button update_button;
    EditText cust_name_up;
    EditText cust_address_up;
    EditText cust_phone_up;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_detail_edit,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_customer_details);
       // cust_id = (TextView) view.findViewById(R.id.customer_id_text_view);
        cust_name = (TextView) view.findViewById(R.id.customer_detail_edit_custom_detail_text_view);
      //  cust_address = (TextView) view.findViewById(R.id.customer_address_text_view);
        cust_name_up = (EditText)view.findViewById(R.id.customer_name_update);
        cust_address_up = (EditText)view.findViewById(R.id.customer_address_update);
        cust_phone_up = (EditText) view.findViewById(R.id.customer_phone_number_update);
        context=this.getActivity();
        update_button = (Button) view.findViewById(R.id.update_customer_details);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cust_address_up.getText().toString().equals("") || cust_name_up.getText().toString().equals("") ||cust_phone_up.getText().toString().equals("") )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cant_string),Toast.LENGTH_SHORT).show();
                }
                else {
                    if (cust_phone_up.getText().toString().length()==10) {
                        myRequest = new LoginPostRequest(customers_detail_fragment_view.this, getActivity());
                        circular_progress.setVisibility(View.VISIBLE);
                        customer_id = ((NavBarSample) getActivity()).current_customer_id;
                        myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer/update/", access_token, customer_id, cust_name_up.getText().toString(), cust_address_up.getText().toString(), cust_phone_up.getText().toString());
                    }
                    else {
                        Toast.makeText(getActivity(), "Number must be 10 digits long!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        isDownloadRunning=true;
       // myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
       circular_progress.setVisibility(View.VISIBLE);
        customer_id =  ((NavBarSample)getActivity()).current_customer_id;
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer/get/" + customer_id ,access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("customers");
            if (isDownloadRunning == false) {
                circular_progress.setVisibility(View.VISIBLE);
                getRawData = new GetRawData(this, getActivity());
                customer_id = ((NavBarSample) getActivity()).current_customer_id;
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer/get/" + customer_id, access_token);
            }
        }
        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
            isDownloadRunning = false;
            circular_progress.setVisibility(View.INVISIBLE);
            try {
                JSONObject jsonData = new JSONObject(data);
                cust_name.setText( getResources().getString(R.string.name_string)+": " + jsonData.getJSONObject("data").getString("name") + "\n" + getResources().getString(R.string.phone_number_string)+": " + jsonData.getJSONObject("data").getString("phone_no") + "\n" + getResources().getString(R.string.address_string)+": " + jsonData.getJSONObject("data").getString("address"));
               // cust_address.setText(jsonData.getJSONObject("data").getString("address"));
               // cust_id.setText(jsonData.getJSONObject("data").getString("id"));


            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            // customAdapter CustomAdapter = new customAdapter();
            //  myView.setAdapter(CustomAdapter);
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
        /*
        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             //   ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",productStockCodeList.get(i));
                Log.d(TAG, "Item got Clicked");
            }
        });
        */
        }
    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getActivity(),  getResources().getString(R.string.error_com_server_string), Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getActivity(),  getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),  getResources().getString(R.string.something_went_string), Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            if (s != null) {
                ((NavBarSample) getActivity()).makeToast( getResources().getString(R.string.update_success_string), true);
                ((NavBarSample) getActivity()).setupCustomer_list_view_pager();
            }
        }
    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean connection) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getActivity(),  getResources().getString(R.string.error_com_server_string), Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getActivity(),  getResources().getString(R.string.no_internet_connection_string), Toast.LENGTH_SHORT).show();
            }
            else if (exception!=null && !exception.equals("No_internet_connection" )) {
                Log.d(TAG, exception);
                Toast.makeText(getActivity(),  getResources().getString(R.string.something_went_string), Toast.LENGTH_SHORT).show();
            }

        }
    }


/*
    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return customerNameList.size();
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
            TextView customerName = (TextView) view.findViewById(R.id.name_list);
            TextView customerID = (TextView) view.findViewById(R.id.product_list);
            TextView customer_address= (TextView) view.findViewById(R.id.cost_list);
            customerName.setText("Name: " + customerNameList.get(i));
            customerID.setText("ID: " + customerIDList.get(i));
            customer_address.setText("Address: " + customerAddressList.get(i));
            imageView.setImageResource(R.drawable.customer_stock_flat);
            return view;
        }
    }
    */
}
