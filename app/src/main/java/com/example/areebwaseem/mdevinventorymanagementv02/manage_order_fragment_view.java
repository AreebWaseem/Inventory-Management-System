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

import java.util.ArrayList;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class manage_order_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "manage_order_fragment_v";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> order_customer_name_list;
    ArrayList<String> order_date_list;
    ArrayList<String> order_grand_total_list;
    ArrayList<String> order_id_list;
    ArrayList<String> cust_email;
    ArrayList<String> cust_address;
    ArrayList<String> order_no_list;
    ArrayList<String> product_qnty_list;
    ProgressBar circular_progress;
    customAdapter CustomAdapter;
    boolean no_prods_assigned=false;
    String access_token="";
    int page_number=1;
    int current_list_return_count=0;
    boolean isDownloading=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_listView);
        order_customer_name_list = new ArrayList<String>();
        order_date_list = new ArrayList<String>();
        order_grand_total_list = new ArrayList<String>();
        order_id_list= new ArrayList<String>();
        cust_email= new ArrayList<String>();
        order_no_list = new ArrayList<String>();
        product_qnty_list= new ArrayList<String>();
        cust_address = new ArrayList<String>();
        CustomAdapter= new customAdapter();
        myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        circular_progress.setVisibility(View.VISIBLE);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/manage_order?page=1&limit=10",access_token);
        isDownloading=true;
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

            ((Order_activity_final) getActivity()).setItemChecked("orders");
            CustomAdapter= new customAdapter();
            if (isDownloading==false) {
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
                if (cust_address!=null)
                {
                    cust_address.clear();
                }
                if (cust_email!=null)
                {
                    cust_email.clear();
                }
                if (order_no_list!=null)
                {
                    order_no_list.clear();
                }
                if (order_grand_total_list!=null)
                {
                    order_grand_total_list.clear();
                }
                if (product_qnty_list!=null)
                {
                    product_qnty_list.clear();
                }
                no_prods_assigned=false;
                page_number=1;
                CustomAdapter = new customAdapter();
                getRawData = new GetRawData(this, getActivity());
                circular_progress.setVisibility(View.VISIBLE);
             //   getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/manage_order?limit=10000", access_token);
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/manage_order?page=1&limit=10",access_token);
                isDownloading=true;

            }
        }
        //INSERT CUSTOM CODE HERE
    }


    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
         isDownloading=false;



            circular_progress.setVisibility(View.INVISIBLE);
            boolean check_data = false;
            String myData = data;
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
                current_list_return_count= itemsArray.length();
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);
                    String customer_name = currentObject.getString("customer_name");
                    String order_date = currentObject.getString("order_date");
                    String grand_total = currentObject.getString("grand_total");
                    order_id_list.add(currentObject.getString("order_id"));
                    if (!customer_name.equals("null")) {
                        order_customer_name_list.add(customer_name);
                    }
                    else {
                        order_customer_name_list.add("Walking Client");
                    }
                    order_date_list.add(order_date);
                    order_grand_total_list.add(grand_total);
                    cust_email.add(currentObject.getString("email"));
                    cust_address.add(currentObject.getString("address"));
                    order_no_list.add(currentObject.getString("order_no"));
                    product_qnty_list.add(currentObject.getString("product_quantity"));
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            // Log.d(TAG, String.valueOf(productStockCodeList.size()));
            /*
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ((Order_activity_final) getActivity()).setupOrderDetail("orders_detail_fragment", order_id_list.get(i));
                    ((Order_activity_final) getActivity()).setCustomerData(cust_email.get(i),cust_address.get(i),order_customer_name_list.get(i));
                    Log.d(TAG, "Item got Clicked");
                }
            });
            */
            if (page_number==1) {
                //  Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                myView.setAdapter(CustomAdapter);
                //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
                myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //   ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",productStockCodeList.get(i));
                      //  ((NavBarSample) getActivity()).setupViewPagerInvoiceDetail(invoiceNumberList.get(i));
                        ((Order_activity_final) getActivity()).setupOrderDetail("orders_detail_fragment", order_id_list.get(i));
                        ((Order_activity_final) getActivity()).setCustomerData(cust_email.get(i),cust_address.get(i),order_customer_name_list.get(i));
                        Log.d(TAG, "Item got Clicked");
                    }
                });
            }
            else {
                if (myView!=null) {
                    myView.setAdapter(CustomAdapter);
                    CustomAdapter.notifyDataSetChanged();
                    myView.setSelection(((page_number-1)*10)-1);
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
                                    getRawData = new GetRawData(manage_order_fragment_view.this, getActivity());
                                    circular_progress.setVisibility(View.VISIBLE);
                                    //   getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
                                    getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/manage_order?page=" +String.valueOf(page_number)+"&limit=10", access_token);
                                }

                            }
                            return;
                        }

                    }
                }




            });
        }
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
            StockCodeTextView.setText(getResources().getString(R.string.date_string)+": " +order_date_list.get(i));
            nameTextView.setText(order_customer_name_list.get(i) + "\n\n" + getResources().getString(R.string.order_no)+": " + order_no_list.get(i));
            costTextView.setText(getResources().getString(R.string.grand_total_string)+": Rs." + order_grand_total_list.get(i));
            imageView.setImageResource(R.drawable.invoice_stock_flat);
            return view;
            */
            view = getLayoutInflater().inflate(R.layout.manage_invoice_custom_list,null);
            //  ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView nameTextView = (TextView) view.findViewById(R.id.name_list_view_text);
            TextView StockCodeTextView = (TextView) view.findViewById(R.id.product_list_view_text);
            TextView numberTextView = (TextView) view.findViewById(R.id.number_list_view);
            numberTextView.setText(product_qnty_list.get(i) + "\n" + getResources().getString(R.string.product_invoice_order_string));
            TextView costTextView = (TextView) view.findViewById(R.id.cost_list_view_text);
            //StockCodeTextView.setText(getResources().getString(R.string.name_string)+": " + customerNameList.get(i) + "\n\n" + "Date: " + dateList.get(i));
            StockCodeTextView.setText(order_date_list.get(i));
            nameTextView.setText( getResources().getString(R.string.order_no)+": " + order_no_list.get(i) + ", " + order_customer_name_list.get(i));
            costTextView.setText(getResources().getString(R.string.grand_total_string)+" Rs." + order_grand_total_list.get(i));
            // imageView.setImageResource(R.drawable.invoice_stock_flat);
            return view;
        }
    }
}
