package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by areebwaseem on 10/28/17.
 */
/*
public class requested_products_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "requested_products_frag";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> productNameList;
    ArrayList<String> product_order_code_list;
    ArrayList<String> order_delivered_date_list;
    ArrayList<String> request_order_id;
    ProgressBar circular_progress;
    String access_token="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_listView);
        productNameList = new ArrayList<String>();
        product_order_code_list = new ArrayList<String>();
        order_delivered_date_list = new ArrayList<String>();
        request_order_i = new ArrayList<String>();
        myView = (ListView) view.findViewById(R.id.product_view_array_list);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        circular_progress.setVisibility(View.VISIBLE);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/api/api/requested_products?limit=10000",access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("requested_products");
        }
        //INSERT CUSTOM CODE HERE
    }


    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("data");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);
                    request_order_id.add(currentObject.getString("id"));
                    product_order_code_list.add(currentObject.getString("order_code"));
                    order_delivered_date_list.add(currentObject.getString("status"));
                    productNameList.add(currentObject.getJSONArray("requested_products").getJSONObject(0).getString("product_name"));


                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
           // Log.d(TAG, data);
           // Log.d(TAG, String.valueOf(productStockCodeList.size()));
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ((NavBarSample) getActivity()).setupRequestProductsDetailView(request_order_id.get(i));

                    Log.d(TAG, "Item got Clicked");
                }
            });
        }
    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
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
            return productNameList.size();
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
            StockCodeTextView.setText("Order Code: " + product_order_code_list.get(i));
            nameTextView.setText( productNameList.get(i));
            costTextView.setText("Status: " + order_delivered_date_list.get(i));
            imageView.setImageResource(R.drawable.desktop_pic_flat);
            return view;
        }
    }
}
*/

public class requested_products_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "requested_products_frag";
    GetRawData getRawData;
    ListView myView;
    Button proceed_Button;
    SharedPreferences mySharedPreferences;
    ArrayList<String> productNameList;
    ArrayList<String> product_order_code_list;
    ArrayList<String> order_delivered_date_list;
    ArrayList<String> request_order_id;
    ArrayList<String> status_list;
    ArrayList<String> requested_products_number;
    ArrayList<String> product_data;
    int prod_num_count=0;
    ProgressBar circular_progress;
    boolean isSearching=false;
    boolean no_prods_assigned=false;
   int page_number=1;
   int current_list_return_count=0;
    EditText req_search;
    String access_token="";
    boolean isDownloading=false;
     SearchableSpinner product_Spinner;
     ArrayList<String> request_products_selected;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.requested_product_updated_layout_fragment_view,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_requested_products_updated);
        productNameList = new ArrayList<String>();
        product_order_code_list = new ArrayList<String>();
        order_delivered_date_list = new ArrayList<String>();
        request_order_id = new ArrayList<String>();
        request_products_selected= new ArrayList<String>();
        status_list = new ArrayList<String>();
        product_data = new ArrayList<String>();

        req_search = (EditText) view.findViewById(R.id.editText2_homer_pager);
        requested_products_number = new ArrayList<String>();
        myView = (ListView) view.findViewById(R.id.requested_product_list_view_upated);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        circular_progress.setVisibility(View.VISIBLE);
        //getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products?limit=10000",access_token);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products?page=1&limit=10",access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("requested_products");

            if (isDownloading==false)
            {
                if (requested_products_number!=null)
                {
                    requested_products_number.clear();
                }
                if (request_order_id!=null)
                {
                    request_order_id.clear();
                }
                if (productNameList!=null)
                {
                    productNameList.clear();
                }
                if (order_delivered_date_list!=null)
                {
                    order_delivered_date_list.clear();
                }
                if (status_list!=null)
                {
                    status_list.clear();
                }
                if (product_order_code_list!=null)
                {
                    product_order_code_list.clear();
                }
                if (product_data!=null)
                {
                    product_data.clear();
                }
                if (request_products_selected!=null)
                {
                    request_products_selected.clear();
                }
                prod_num_count=0;
                page_number=1;
                current_list_return_count=0;
                no_prods_assigned=false;
                isDownloading=true;
                getRawData = new GetRawData(this,getActivity());
                circular_progress.setVisibility(View.VISIBLE);
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products?page=1&limit=10",access_token);
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
            Log.d(TAG, data);
            try {
                JSONObject jsonData = new JSONObject(myString.toString());
                JSONArray itemsArray = jsonData.getJSONArray("data");
                Log.d(TAG, "Size of req_products" + String.valueOf(itemsArray.length()));
                current_list_return_count= itemsArray.length();
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);
                    request_order_id.add(currentObject.getString("id"));
                    product_order_code_list.add(currentObject.getString("order_code"));
                    if (currentObject.getString("delivered_date")!=null) {
                        order_delivered_date_list.add(currentObject.getString("delivered_date"));
                    }
                    else {
                        order_delivered_date_list.add("null");
                    }
                  //  productNameList.add(currentObject.getJSONArray("requested_products").getJSONObject(0).getString("product_name"));
                    try {
                      String a= currentObject.getJSONArray("requested_products").getJSONObject(0).getString("product_id");
                      if (a!=null) {
                          requested_products_number.add(String.valueOf(currentObject.getJSONArray("requested_products").length()));
                      }
                      else {
                          requested_products_number.add("0");
                      }

                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "Exceptionnnnnnnnnnnnnnnnnnnnn");
                        requested_products_number.add("0");
                    }

                    status_list.add(currentObject.getString("status"));
                    JSONArray myArr = currentObject.getJSONArray("requested_products");
                    if (myArr!=null) {
                        String fin_val = "";

                        for (int z = 0; z < myArr.length(); z++) {
                            JSONObject currObj = myArr.getJSONObject(z);
                            fin_val = fin_val + currObj.getString("product_name");
                        }


                        //  product_data.add(myArr.getJSONObject(0).getString("product_name"));
                        product_data.add(fin_val);
                    }
                    else {
                        product_data.add("");
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, "Prod Data List" +String.valueOf(product_data.size()));
            Log.d(TAG, "Order id list" + String.valueOf(request_order_id.size()));
            Log.d(TAG, "order code list" +String.valueOf(product_order_code_list.size()));
            Log.d(TAG,"delivered date list" + String.valueOf(order_delivered_date_list.size()));
           // Log.d(TAG, String.valueOf(productNameList.size()));
            Log.d(TAG,"prods number list" +String.valueOf(requested_products_number.size()));
            Log.d(TAG, "Status list"+String.valueOf(status_list.size()));



      //      ArrayList<requested_product_search_custom_last> myplayers = new ArrayList<requested_product_search_custom_last>();

           // customAdapter CustomAdapter = new customAdapter();



/*

             final Adapter myNewSearchAdapter= new Adapter(getActivity(),myplayers);
         //   myView.setAdapter(CustomAdapter);
            myView.setAdapter(myNewSearchAdapter);


            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView id_tracker = (TextView) view.findViewById(R.id.textView7_request_product_search_id);
                    String current_i= id_tracker.getText().toString();
                  //  ((NavBarSample) getActivity()).setupRequestProductsDetailView(request_order_id.get(i));
                   ((NavBarSample) getActivity()).setupRequestProductsDetailView(current_i);
                }
            });
            req_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    myNewSearchAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
           */


            if (page_number==1) {
                //  Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
               // myView.setAdapter(CustomAdapter);
                //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
                ArrayList<requested_product_search_custom_last> myplayers;
                myplayers= new ArrayList<requested_product_search_custom_last>();
                requested_product_search_custom_last p;

                for (int x=0;x<request_order_id.size();x++)
                {
                    p = new requested_product_search_custom_last(requested_products_number.get(x),order_delivered_date_list.get(x),product_order_code_list.get(x),status_list.get(x),product_data.get(x),request_order_id.get(x));
                    myplayers.add(p);
                }
                final Adapter myNewSearchAdapter= new Adapter(getActivity(),myplayers);
                //  myView.setAdapter(CustomAdapter);
                myView.setAdapter(myNewSearchAdapter);
                myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //   ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_returned",productStockCodeList.get(i));
                        //  ((NavBarSample) getActivity()).setupViewPagerInvoiceDetail(invoiceNumberList.get(i));
                        // ((Order_activity_final) getActivity()).setupOrderDetail("orders_detail_fragment", order_id_list.get(i));
                        // ((Order_activity_final) getActivity()).setCustomerData(cust_email.get(i),cust_address.get(i),order_customer_name_list.get(i));
                        //  ((NavBarSample)getActivity()).setupViewPagerDetail("productViewDetail_assigned",productStockCodeList.get(i));
                       // ((NavBarSample) getActivity()).setupViewPagerDetail("productViewDetail_returned", productStockCodeList.get(i));
                        TextView id_tracker = (TextView) view.findViewById(R.id.textView7_request_product_search_id);
                        String current_i= id_tracker.getText().toString();
                        //  ((NavBarSample) getActivity()).setupRequestProductsDetailView(request_order_id.get(i));
                        ((NavBarSample) getActivity()).setupRequestProductsDetailView(current_i);
                        Log.d(TAG, "Item got Clicked");
                    }
                });
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
                                if (isDownloading==false && current_list_return_count==10 && myView.getCount()>=10 && no_prods_assigned==false &&isSearching==false)
                                {
                                    if (getActivity()!=null) {
                                        isDownloading = true;
                                        //    Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                                        page_number=page_number+1;
                                        getRawData = new GetRawData(requested_products_fragment_view.this, getActivity());
                                        circular_progress.setVisibility(View.VISIBLE);
                                        //   getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
                                        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products?page=" +String.valueOf(page_number)+"&limit=10", access_token);
                                    }

                                }
                                return;
                            }

                        }
                    }




                });
                req_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length()!=0)
                    {
                        isSearching=true;
                    }
                    else {
                        isSearching=false;
                    }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        myNewSearchAdapter.getFilter().filter(charSequence);
                        if (charSequence.toString().length()!=0)
                        {
                            isSearching=true;
                        }
                        else {
                            isSearching=false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().length()!=0)
                        {
                            isSearching=true;
                        }
                        else {
                            isSearching=false;
                        }
                    }
                });
            }
            else {
                if (myView!=null) {


                    ArrayList<requested_product_search_custom_last> myplayers;
                    myplayers= new ArrayList<requested_product_search_custom_last>();
                    requested_product_search_custom_last p;

                    for (int x=0;x<request_order_id.size();x++)
                    {
                        p = new requested_product_search_custom_last(requested_products_number.get(x),order_delivered_date_list.get(x),product_order_code_list.get(x),status_list.get(x),product_data.get(x),request_order_id.get(x));
                        myplayers.add(p);
                    }
                    final Adapter myNewSearchAdapter= new Adapter(getActivity(),myplayers);

                 //   CustomAdapter.notifyDataSetChanged();
                   // myNewSearchAdapter.notifyDataSetChanged();
                    myView.setAdapter(myNewSearchAdapter);

                    myView.setSelection(((page_number-1)*10)-1);
                    req_search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().length()!=0)
                            {
                                isSearching=true;
                            }
                            else {
                                isSearching=false;
                            }
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            myNewSearchAdapter.getFilter().filter(charSequence);
                            if (charSequence.toString().length()!=0)
                            {
                                isSearching=true;
                            }
                            else {
                                isSearching=false;
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (editable.toString().length()!=0)
                            {
                                isSearching=true;
                            }
                            else {
                                isSearching=false;
                            }
                        }
                    });
                }

            }










           // Log.d(TAG, data);
           // Log.d(TAG, String.valueOf(productStockCodeList.size()));

            /*
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ((NavBarSample) getActivity()).setupRequestProductsDetailView(request_order_id.get(i));

                    Log.d(TAG, "Item got Clicked");
                }
            });
            */
        }
    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            no_prods_assigned=true;
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
    /*

    class customAdapter extends BaseAdapter{
        private ArrayList<String>originalData = null;
        private ArrayList<String>filteredData = null;
        private LayoutInflater mInflater;
        private ItemFilter mFilter = new ItemFilter();

        public customAdapter(Context context, ArrayList<String> data) {
            this.filteredData = data ;
            this.originalData = data ;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return filteredData.size();
        }

        @Override
        public Object getItem(int i) {

            return filteredData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.requested_product_custom_list_item,null);


            TextView tot_num = (TextView) view.findViewById(R.id.custom_requested_product_total_number);
            TextView ord_date = (TextView) view.findViewById(R.id.custom_request_product_order_date);
            TextView ord_code = (TextView) view.findViewById(R.id.custom_requested_product_order_number);
            TextView ord_status = (TextView) view.findViewById(R.id.custom_request_product_status);
             tot_num.setText(requested_products_number.get(i) + "\n" + "Products");
             ord_date.setText(order_delivered_date_list.get(i));
             ord_code.setText(product_order_code_list.get(i));
             ord_status.setText(status_list.get(i));

            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (view == null) {
              // view = mInflater.inflate(R.layout.list_item, null);
                view = getLayoutInflater().inflate(R.layout.requested_product_custom_list_item,null);
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                TextView tot_num = (TextView) view.findViewById(R.id.custom_requested_product_total_number);
                TextView ord_date = (TextView) view.findViewById(R.id.custom_request_product_order_date);
                TextView ord_code = (TextView) view.findViewById(R.id.custom_requested_product_order_number);
                TextView ord_status = (TextView) view.findViewById(R.id.custom_request_product_status);
                TextView id_tracker = (TextView) view.findViewById(R.id.textView7_request_product_search_id);
                id_tracker.setText(request_order_id.get(i));
                tot_num.setText(requested_products_number.get(i) + "\n" + "Products");
                ord_date.setText(order_delivered_date_list.get(i));
                ord_code.setText(product_order_code_list.get(i));
                ord_status.setText(status_list.get(i));
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.textView2_custom_searchable_adapter);

                // Bind the data efficiently with the holder.

                view.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) view.getTag();
            }

            // If weren't re-ordering this you could rely on what you set last time
            holder.text.setText(filteredData.get(i));

            return view;

           // ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);

            TextView nameTextView = (TextView) view.findViewById(R.id.name_list);
            TextView StockCodeTextView = (TextView) view.findViewById(R.id.product_list);
            TextView costTextView = (TextView) view.findViewById(R.id.cost_list);
            StockCodeTextView.setText("Order Code: " + product_order_code_list.get(i));
            nameTextView.setText( productNameList.get(i));
            costTextView.setText("Status: " + order_delivered_date_list.get(i));

           // imageView.setImageResource(R.drawable.desktop_pic_flat);

            final TextView id_view = (TextView) view.findViewById(R.id.request_product_list_id_tracker_text_view);
            ImageButton myButton = (ImageButton) view.findViewById(R.id.request_product_custom_list_item_image_button);
            EditText qntyText = (EditText) view.findViewById(R.id.request_product_custom_list_item_edit_text);
            TextView product_name_view = (TextView) view.findViewById(R.id.request_product_custom_list_item_text_view);

            if (i<request_products_selected.size()) {
                product_name_view.setText(request_products_selected.get(i));
            }
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(id_view.getText().toString())<request_products_selected.size())
                    {
                        request_products_selected.remove(request_products_selected.get(Integer.valueOf(id_view.getText().toString())));
                        Log.d(TAG, "Removed");
                        Log.d(TAG, String.valueOf(request_products_selected.size()));
                        Log.d(TAG, "Index Number: " + id_view.getText().toString());
                        notifyDataSetChanged();
                        int totalHeight = 0;
                        int desiredWidth = View.MeasureSpec.makeMeasureSpec(myView.getWidth(), View.MeasureSpec.AT_MOST);
                        for (int z = 0; z < getCount(); z++) {
                            View listItem = getView(z, null, myView);
                            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        ViewGroup.LayoutParams params = myView.getLayoutParams();
                        params.height = totalHeight + (myView.getDividerHeight() * (getCount() - 1));
                        myView.setLayoutParams(params);
                        myView.requestLayout();
                    }
                }
            });



        }
         class ViewHolder {
            TextView text;
        }

        public Filter getFilter() {
            return mFilter;
        }

        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final ArrayList<String> list = originalData;

                int count = list.size();
                final ArrayList<String> nlist = new ArrayList<String>(count);

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i);
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(filterableString);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

        }


    }
    */

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return (request_order_id.size());
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
            view = getLayoutInflater().inflate(R.layout.requested_product_custom_list_item,null);
            // ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);


            TextView tot_num = (TextView) view.findViewById(R.id.custom_requested_product_total_number);
            TextView ord_date = (TextView) view.findViewById(R.id.custom_request_product_order_date);
            TextView ord_code = (TextView) view.findViewById(R.id.custom_requested_product_order_number);
            TextView ord_status = (TextView) view.findViewById(R.id.custom_request_product_status);
            tot_num.setText(requested_products_number.get(i) + "\n" + getResources().getString(R.string.products_string));
            ord_date.setText(order_delivered_date_list.get(i));
            ord_code.setText(product_order_code_list.get(i));
            ord_status.setText(status_list.get(i));

            return view;
        }
    }

    class Adapter extends BaseAdapter implements Filterable {
        Context c;
         ArrayList<requested_product_search_custom_last> players;
         ArrayList<requested_product_search_custom_last> filterList;
        CustomFilter myFilter;
         /*

        ArrayList<String> filterMylist;
*/
        public Adapter(Context ctx,ArrayList<requested_product_search_custom_last> players)
        {
            this.players=players;
            this.c = ctx;
            this.filterList=players;

        }



        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int i) {
            return players.get(i);
        }

        @Override
        public long getItemId(int i) {
            return players.indexOf(getItem(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            //LayoutInflater inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view==null) {
                view = getLayoutInflater().inflate(R.layout.requested_product_custom_list_item, null);
            }

            TextView tot_num = (TextView) view.findViewById(R.id.custom_requested_product_total_number);
            TextView ord_date = (TextView) view.findViewById(R.id.custom_request_product_order_date);
            TextView ord_code = (TextView) view.findViewById(R.id.custom_requested_product_order_number);
            TextView ord_status = (TextView) view.findViewById(R.id.custom_request_product_status);
            TextView id_tracker = (TextView) view.findViewById(R.id.textView7_request_product_search_id);
            /*
            tot_num.setText(requested_products_number.get(i) + "\n" + "Products");
            ord_date.setText(order_delivered_date_list.get(i));
            ord_code.setText(product_order_code_list.get(i));
            ord_status.setText(status_list.get(i));
*/
            tot_num.setText(players.get(i).getTotal_number() + "\n" +  getResources().getString(R.string.products_small_string));
            ord_date.setText(players.get(i).getOrd_date());
            ord_code.setText(players.get(i).getOrd_code());
            ord_status.setText(players.get(i).getOrd_status());
            id_tracker.setText(players.get(i).getProduct_id());
            return view;
        }

        @Override
        public Filter getFilter() {
            if (myFilter==null)
            {
                myFilter = new CustomFilter();
            }
            return myFilter;
        }

        class CustomFilter extends Filter
        {


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if (charSequence!=null && charSequence.length()>0)
                {
                    charSequence = charSequence.toString().toUpperCase();
                    ArrayList<requested_product_search_custom_last> filters = new ArrayList<requested_product_search_custom_last>();
                    for (int i=0;i<filterList.size();i++)
                    {
                        if (filterList.get(i).getProduct_DATA().toUpperCase().contains(charSequence))
                        {
                            requested_product_search_custom_last p = new requested_product_search_custom_last(filterList.get(i).getTotal_number(),filterList.get(i).getOrd_date(),filterList.get(i).getOrd_code(),filterList.get(i).getOrd_status(),filterList.get(i).getProduct_DATA(),filterList.get(i).getProduct_id());
                            filters.add(p);
                        }
                    }
                    results.count=filters.size();
                    results.values=filters;
                }
                else {
                    results.count=filterList.size();
                    results.values=filterList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

               players= (ArrayList<requested_product_search_custom_last>) filterResults.values;
               notifyDataSetChanged();

            }
        }




    }
}

