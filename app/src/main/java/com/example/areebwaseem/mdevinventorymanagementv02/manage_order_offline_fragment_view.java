package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class manage_order_offline_fragment_view extends Fragment {
    private static final String TAG = "manage_order_offline_fr";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> order_customer_name_list;
    ArrayList<String> order_date_list;
    ArrayList<String> order_grand_total_list;
    ArrayList<String> order_id_list;
    ArrayList<String> product_qnty_list;
    ArrayList<String> all_orders;
    ProgressBar circular_progress;
    DataBaseHelper myHelper;
    String access_token="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_listView);
        circular_progress.setVisibility(View.INVISIBLE);
        order_customer_name_list = new ArrayList<String>();
        order_date_list = new ArrayList<String>();
        order_grand_total_list = new ArrayList<String>();
        order_id_list= new ArrayList<String>();
        all_orders= new ArrayList<String>();
        product_qnty_list = new ArrayList<String>();
        myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        myHelper = new DataBaseHelper(getActivity());

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
            ((offline_order_activity) getActivity()).setItemChecked("manage_order_offline");
            /*
            order_customer_name_list = new ArrayList<String>();
            order_date_list = new ArrayList<String>();
            order_grand_total_list = new ArrayList<String>();
            order_id_list= new ArrayList<String>();
            all_orders= new ArrayList<String>();
            */
            if (order_customer_name_list!=null)
            {
                order_customer_name_list.clear();
            }
            if (order_date_list!=null)
            {
                order_date_list.clear();
            }
            if (order_id_list!=null)
            {
                order_id_list.clear();
            }
            if (order_grand_total_list!=null)
            {
                order_grand_total_list.clear();
            }
            if (all_orders!=null)
            {
               all_orders.clear();
            }
            if (product_qnty_list!=null)
            {
                product_qnty_list.clear();
            }

            Cursor res = myHelper.getAllData_Order();
            if (res!=null) {
                StringBuffer buffer;
                while (res.moveToNext()) {
                    buffer = new StringBuffer();
                    // buffer.append("ID: " + res.getString(0) + "\n");
                    // buffer.append("Name: " + res.getString(1) + "\n");
                    buffer.append(res.getString(1));
                    all_orders.add(buffer.toString());
                }
            }
            if (all_orders.size() == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_off_orders), Toast.LENGTH_LONG).show();
            }
            else {
                ShowData();
            }
        }
        //INSERT CUSTOM CODE HERE
    }


    public void ShowData() {
        if (getActivity()!=null) {
           for (int x=0; x<all_orders.size();x++) {
               try {
                   JSONObject currentObject = new JSONObject(all_orders.get(x));
                     Log.d(TAG, all_orders.get(x));
                       String customer_name = currentObject.getString("current_customer_name");
                       String order_date = currentObject.getString("shipping_address");
                       String grand_total = currentObject.getString("grand_total");
                       order_id_list.add(currentObject.getString("customer_id"));
                       order_customer_name_list.add(customer_name);
                       order_date_list.add(order_date);
                       order_grand_total_list.add(grand_total);
                       int qnty_count=0;
                   JSONArray myArray = currentObject.getJSONArray("products");
                   for (int z=0;z<myArray.length();z++)
                   {
                       qnty_count = qnty_count + Integer.valueOf(myArray.getJSONObject(z).getString("quantity"));
                   }
               product_qnty_list.add(String.valueOf(qnty_count));

               } catch (JSONException e) {
                   Log.e(TAG, e.getMessage());
               }
           }
           // Log.d(TAG, data);
            // Log.d(TAG, String.valueOf(productStockCodeList.size()));
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 //   ((Order_activity_final) getActivity()).setupOrderDetail("orders_detail_fragment", order_id_list.get(i));
                    Log.d(TAG, "Item got Clicked");
                }
            });
        }
    }


    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return order_customer_name_list.size();
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
            TextView nameTextView = (TextView) view.findViewById(R.id.name_list);
            TextView StockCodeTextView = (TextView) view.findViewById(R.id.product_list);
            TextView costTextView = (TextView) view.findViewById(R.id.product_list);
            StockCodeTextView.setText(getResources().getString(R.string.address_string)+": " +order_date_list.get(i));
            nameTextView.setText(order_customer_name_list.get(i));
            costTextView.setText(getResources().getString(R.string.grand_total_string)+": Rs." + order_grand_total_list.get(i));
            imageView.setImageResource(R.drawable.invoice_stock_flat);
            return view;
            */
            view = getLayoutInflater().inflate(R.layout.manage_invoice_custom_list,null);
            //  ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView nameTextView = (TextView) view.findViewById(R.id.name_list_view_text);
            TextView StockCodeTextView = (TextView) view.findViewById(R.id.product_list_view_text);
            TextView numberTextView = (TextView) view.findViewById(R.id.number_list_view);
            numberTextView.setText(product_qnty_list.get(i) + "\n" + "Products");
            TextView costTextView = (TextView) view.findViewById(R.id.cost_list_view_text);
            //StockCodeTextView.setText(getResources().getString(R.string.name_string)+": " + customerNameList.get(i) + "\n\n" + "Date: " + dateList.get(i));
            StockCodeTextView.setText(order_date_list.get(i));
           // nameTextView.setText( getResources().getString(R.string.order_no)+": " + order_no_list.get(i) + ", " + order_customer_name_list.get(i));
            nameTextView.setText(order_customer_name_list.get(i));
            costTextView.setText(getResources().getString(R.string.grand_total_string)+" Rs." + order_grand_total_list.get(i));
            // imageView.setImageResource(R.drawable.invoice_stock_flat);
            return view;
        }
    }
}
