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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class customers_add_fragment_view extends Fragment implements  LoginPostRequest.OnPostComplete{
    private static final String TAG = "customers_add_fragment_";
    GetRawData getRawData;
    ListView myView;
    LoginPostRequest myRequest;
    SharedPreferences mySharedPreferences;
    ProgressBar circular_progress;
    String access_token="";
    String customer_id;
    Context context;
    Button add_button;
    EditText cust_name_add;
    EditText cust_phone_add;
    EditText cust_address_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_add_fragment,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_customer_add);
        circular_progress.setVisibility(View.INVISIBLE);
        context=this.getActivity();
        add_button = (Button) view.findViewById(R.id.customer_add_button);
        cust_address_add = (EditText) view.findViewById(R.id.customer_add_address);
        cust_phone_add = (EditText) view.findViewById(R.id.customer_phone_add);
        cust_name_add = (EditText) view.findViewById(R.id.customer_add_name);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cust_address_add.getText().toString().equals("") || cust_name_add.getText().toString().equals("") ||cust_phone_add.getText().toString().equals("") )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cant_string),Toast.LENGTH_SHORT).show();
                }
                else {
                    if (cust_phone_add.getText().toString().length()==10)
                    {
                        myRequest = new LoginPostRequest(customers_add_fragment_view.this, getActivity());
                        circular_progress.setVisibility(View.VISIBLE);
                        customer_id = ((NavBarSample) getActivity()).current_customer_id;
                        myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer/add", access_token, cust_name_add.getText().toString(), cust_phone_add.getText().toString(), cust_address_add.getText().toString());
                    }
                    else {
                        Toast.makeText(getActivity(), "Number must be 10 digits long!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


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
            ((NavBarSample) getActivity()).setItemChecked("addcustomers");
        }
    }



    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            if (s != null) {
                ((NavBarSample) getActivity()).makeToast("Customer Successfully Added!", true);
                if (getActivity()!=null)
                {
                    ((NavBarSample) getActivity()).setupCustomer_list_view_pager();
                }
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
