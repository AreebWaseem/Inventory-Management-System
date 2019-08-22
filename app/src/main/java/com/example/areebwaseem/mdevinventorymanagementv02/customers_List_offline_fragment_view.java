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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class customers_List_offline_fragment_view extends Fragment {
    private static final String TAG = "customers_List_offline_";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> customerNameList;
    ArrayList<String> customerAddressList;
    ArrayList<String> customerIDList;
    ArrayList<String> phone_no_list;
    ProgressBar circular_progress;
    String access_token="";
    public String customer_id="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_listView);
        customerAddressList = new ArrayList<String>();
        customerNameList = new ArrayList<String>();
        customerIDList = new ArrayList<String>();
        phone_no_list = new ArrayList<String>();
        myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
       String customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");


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
            ((offline_order_activity) getActivity()).setItemChecked("customer_all_offline");
            mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
            String customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");

            if (!customer_data.equals("abc"))
            {
                if (customerNameList!=null)
                {
                    customerNameList.clear();
                }
                if (customerIDList!=null)
                {
                    customerIDList.clear();
                }
                if (customerAddressList!=null)
                {
                    customerAddressList.clear();
                }
                if (phone_no_list!=null)
                {
                    phone_no_list.clear();
                }
                ShowData(customer_data);
            }
            else {
                Toast.makeText(getActivity(),  getResources().getString(R.string.no_data_string), Toast.LENGTH_SHORT).show();
            }
        }

        //INSERT CUSTOM CODE HERE
    }

    public void ShowData(String data) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
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
                    phone_no_list.add(currentObject.getString("phone_no"));

                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",productStockCodeList.get(i));
                  // ((offline_order_activity) getActivity()).current_customer_id = customerIDList.get(i);
                  //  ((offline_order_activity) getActivity()).current_customer_address= customerAddressList.get(i);
                   // ((offline_order_activity) getActivity()).setUpPaymentViewPager();
                    Log.d(TAG, "Item got Clicked");
                }
            });
        }
    }


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
            /*
            view = getLayoutInflater().inflate(R.layout.custom_product_list_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView customerName = (TextView) view.findViewById(R.id.name_list);
            TextView customerID = (TextView) view.findViewById(R.id.product_list);
            TextView customer_address= (TextView) view.findViewById(R.id.product_list);
            customerName.setText( getResources().getString(R.string.name_string)+": " + customerNameList.get(i));
            customerID.setText("ID: " + customerIDList.get(i));
            customer_address.setText( getResources().getString(R.string.address_string)+": " + customerAddressList.get(i));
            imageView.setImageResource(R.drawable.customer_stock_flat);
            */
            view = getLayoutInflater().inflate(R.layout.customer_list_new_item,null);
            //  ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView customerName = (TextView) view.findViewById(R.id.name_list);
            TextView customerID = (TextView) view.findViewById(R.id.product_list);
            TextView customer_address= (TextView) view.findViewById(R.id.cost_list);
            customerName.setText( customerNameList.get(i));
            customerID.setText( customerAddressList.get(i));
            customer_address.setText(phone_no_list.get(i));
            //imageView.setImageResource(R.drawable.customer_stock_flat);
            return view;
        }
    }
}
