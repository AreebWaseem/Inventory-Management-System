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

public class customers_List_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "customers_List_fragment";
    GetRawData getRawData;
    ListView myView;
    boolean no_prods_assigned=false;
    customAdapter CustomAdapter;
    int page_number=1;
    int current_list_return_count=0;
    SharedPreferences mySharedPreferences;
    ArrayList<String> customerNameList;
    ArrayList<String> customerAddressList;
    ArrayList<String> customerIDList;
    ArrayList<String> phone_number_list;
    ProgressBar circular_progress;
    String access_token="";
    boolean isDownloading=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_listView);
        customerAddressList = new ArrayList<String>();
        customerNameList = new ArrayList<String>();
        customerIDList = new ArrayList<String>();
        phone_number_list = new ArrayList<String>();
        myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        circular_progress.setVisibility(View.VISIBLE);
        CustomAdapter = new customAdapter();
       // getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?page=1&limit=10",access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("customers");
            if (isDownloading==false)
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
                if (phone_number_list!=null)
                {
                    phone_number_list.clear();
                }
                page_number=1;
               no_prods_assigned=false;
                CustomAdapter = new customAdapter();
                getRawData = new GetRawData(this,getActivity());
                circular_progress.setVisibility(View.VISIBLE);
             //   getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?page=1&limit=10",access_token);
            }
        }

        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
            isDownloading=false;
            circular_progress.setVisibility(View.INVISIBLE);
            String myData = data;
            boolean didgetabrace = false;
            StringBuilder myString = new StringBuilder();
            for (int i = 0; i < myData.length(); i++) {
                char c = myData.charAt(i);
                if (c == '{') {
                    didgetabrace = true;
                }
                if (didgetabrace == true) {
                    myString.append(c);
                }
            }
            try {
                JSONObject jsonData = new JSONObject(myString.toString());
                JSONArray itemsArray = jsonData.getJSONArray("data");
                current_list_return_count=itemsArray.length();
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);
                    String nam = currentObject.getString("name");
                    String id = currentObject.getString("id");
                    String addres = currentObject.getString("address");
                    customerNameList.add(nam);
                    customerIDList.add(id);
                    customerAddressList.add(addres);
                    phone_number_list.add(currentObject.getString("phone_no"));
                }
                mySharedPreferences.edit().putString("order_create_customer_list", data).apply();
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            if (page_number==1) {
              //  Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                myView.setAdapter(CustomAdapter);
                //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
                myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //   ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",productStockCodeList.get(i));
                        ((NavBarSample) getActivity()).setupViewPagerCustomerDetail(customerIDList.get(i));
                        Log.d(TAG, "Item got Clicked");
                    }
                });
            }
            else {
                if (myView!=null) {
                    myView.setAdapter(CustomAdapter);
                    CustomAdapter.notifyDataSetChanged();
                    myView.setSelection((page_number-1)*10);
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
                                    getRawData = new GetRawData(customers_List_fragment_view.this, getActivity());
                                    circular_progress.setVisibility(View.VISIBLE);
                                    //   getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
                                    getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?page=" +String.valueOf(page_number)+"&limit=10", access_token);
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
            circular_progress.setVisibility(View.INVISIBLE);
            no_prods_assigned=true;
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
            view = getLayoutInflater().inflate(R.layout.customer_list_new_item,null);
          //  ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView customerName = (TextView) view.findViewById(R.id.name_list);
            TextView customerID = (TextView) view.findViewById(R.id.product_list);
            TextView customer_address= (TextView) view.findViewById(R.id.cost_list);
            customerName.setText( customerNameList.get(i));
            customerID.setText( customerAddressList.get(i));
            customer_address.setText(phone_number_list.get(i));
           //imageView.setImageResource(R.drawable.customer_stock_flat);
            return view;
        }
    }
}
