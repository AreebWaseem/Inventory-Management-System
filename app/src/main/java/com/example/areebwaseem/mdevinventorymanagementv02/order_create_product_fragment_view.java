package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class order_create_product_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "order_create_product_fr";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> order_product_name_list;
    ArrayList<String> order_product_batch_list;
    ArrayList<String> order_code_list;
    ArrayList<String> order_buying_price_list;
    ArrayList<String> order_selling_price_list;
    ArrayList<String> order_isStock_qnty_list;
    ArrayList<String> order_category_main_list;
    ArrayList<String> order_category_sub_list;
    ArrayList<String> requested_product_selling_price_list;
    ArrayList<String> product_id_list;
    ArrayList<String> customer_name_list;
    ArrayList<String> customer_id_list;
    ArrayList<String> qntyCount;
    ArrayList<String> req_prod_in_qnty_list;
    ArrayList<String> request_products_selected;
    ArrayList<String> requested_products_selected_id_list;
    ArrayList<String> customer_address_list;
    ArrayList<String> name_and_qnty_list;
    ArrayList<String> requested_prod_batch_list;
    TextView total_view;
    String current_cust_address;
    boolean isDownloading=false;
  SearchableSpinner product_spinner, customer_spinner;
  //  ProgressBar circular_progress;
    Button proceed_button;
    String access_token="";
    String current_customer_id="";
    Boolean[] array_checked;
    boolean isCust_ruuning=false;
    boolean isProd_running=true;
    int[] qntyArray;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_order_updated_fragment_view,container,false);
       // circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_custom_order_product_select);
        order_product_name_list = new ArrayList<String>();
        order_code_list = new ArrayList<String>();
        order_buying_price_list = new ArrayList<String>();
        product_id_list= new ArrayList<String>();
        order_selling_price_list=new ArrayList<String>();
        order_isStock_qnty_list= new ArrayList<String>();
        order_category_main_list= new ArrayList<String>();
        order_category_sub_list= new ArrayList<String>();
        order_product_batch_list= new ArrayList<String>();
        customer_name_list= new ArrayList<String>();
        customer_id_list = new ArrayList<String>();
        name_and_qnty_list = new ArrayList<String>();
        customer_address_list = new ArrayList<String>();
        requested_prod_batch_list = new ArrayList<String>();
        req_prod_in_qnty_list = new ArrayList<String>();
        requested_product_selling_price_list = new ArrayList<String>();


        customer_id_list.add("nil");
        customer_name_list.add("Walking Client");
        customer_address_list.add("nil");




        if (getActivity()!=null)
        {
            ( (Order_activity_final)getActivity()).isNew=false;
        }

        name_and_qnty_list.add(getResources().getString(R.string.spin_select_products_str_resource));


        total_view = (TextView) view.findViewById(R.id.custom_order_updated_total_amount_text_view);






        request_products_selected = new ArrayList<String>();
        requested_products_selected_id_list= new ArrayList<String>();
        order_product_name_list.add(getResources().getString(R.string.spin_select_products_str_resource));
        qntyCount = new ArrayList<String>();

       product_spinner= (SearchableSpinner) view.findViewById(R.id.order_product_spinner_order);
       customer_spinner= (SearchableSpinner) view.findViewById(R.id.customer_spinner_order_spinner);
       customer_spinner.setTitle("Select Customer");
        proceed_button = (Button) view.findViewById(R.id.custom_order_update_order_proceed);
        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qntyCount.size()!=0) {
                    boolean qntyCheck = true;
                    //  Log.d(TAG, "Total Length of qntyCount:" + String.valueOf(qntyCount.size()));
                    //  Log.d(TAG, "Total length of Request Name list: " + String.valueOf(request_products_selected.size()));
                    //  Log.d(TAG, "Total length of items in ID list" + String.valueOf(requested_products_selected_id_list.size()));
                    for (int i = 0; i < qntyCount.size(); i++) {
                        if (qntyCount.get(i).equals("0")) {
                            qntyCheck = false;
                        }
                        Log.d(TAG, qntyCount.get(i));
                        Log.d(TAG, request_products_selected.get(i));
                        Log.d(TAG, requested_products_selected_id_list.get(i));
                    }
                    if (qntyCheck == true && !current_customer_id.equals("")) {
                        for (int i = 0; i < qntyArray.length; i++) {
                            array_checked[i] = false;
                            qntyArray[i] = 0;
                        }

                        for (int i = 0; i < requested_products_selected_id_list.size(); i++) {
                            for (int z = 0; z < product_id_list.size(); z++) {
                                if (requested_products_selected_id_list.get(i).equals(product_id_list.get(z))&&requested_prod_batch_list.get(i).equals(order_product_batch_list.get(z))) {
                                    qntyArray[z] = Integer.valueOf(qntyCount.get(i));
                                    break;
                                }
                            }
                        }
                        for (int x = 0; x < qntyArray.length; x++) {
                            Log.d(TAG, String.valueOf(qntyArray[x]));
                        }
                        //    Toast.makeText(getActivity(),"Whats up!", Toast.LENGTH_SHORT).show();
                        ((Order_activity_final) getActivity()).setProductData(qntyArray);
                        ((Order_activity_final) getActivity()).current_customer_id = current_customer_id;
                        ((Order_activity_final) getActivity()).current_customer_address = current_cust_address;
                         ((Order_activity_final) getActivity()).setUpPaymentViewPager();

                    }
                    else if (current_customer_id.equals(""))
                    {
                        Toast.makeText(getActivity(),  getResources().getString(R.string.invali_cust_string), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.invalid_qnty_string) , Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_prods_string), Toast.LENGTH_SHORT).show();
                }
            }
        });

        myView = (ListView) view.findViewById(R.id.custom_order_product_list_view_order);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        isDownloading=true;
     //   circular_progress.setVisibility(View.VISIBLE);
        isProd_running=true;
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/create",access_token);
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
            ((Order_activity_final) getActivity()).setItemChecked("create_order_product");
            if( ((Order_activity_final)getActivity()).isNew==true) {

                if (isDownloading == false) {
                    ( (Order_activity_final)getActivity()).isNew=false;
                    if (order_product_name_list != null) {
                        order_product_name_list.clear();
                        order_product_name_list.add("Select Products");
                    }
                    if (order_code_list != null) {
                        order_code_list.clear();
                    }
                    if (order_buying_price_list != null) {
                        order_buying_price_list.clear();
                    }
                    if (product_id_list != null) {
                        product_id_list.clear();
                    }
                    if (order_selling_price_list != null) {
                        order_selling_price_list.clear();
                    }
                    if (order_isStock_qnty_list != null) {
                        order_isStock_qnty_list.clear();
                    }
                    if (order_category_main_list != null) {
                        order_category_main_list.clear();
                    }

                    if (order_category_sub_list != null) {
                        order_category_sub_list.clear();
                    }
                    if (order_product_batch_list != null) {
                        order_product_batch_list.clear();
                    }
                    if (customer_name_list != null) {
                        customer_name_list.clear();
                        customer_name_list.add("Walking Client");
                    }
                    if (customer_id_list != null) {
                        customer_id_list.clear();
                        customer_id_list.add("nil");
                    }
                    if (name_and_qnty_list != null) {
                        name_and_qnty_list.clear();
                        name_and_qnty_list.add(getResources().getString(R.string.spin_select_products_str_resource));
                    }
                    if (customer_address_list != null) {
                        customer_address_list.clear();
                        customer_address_list.add("nil");
                    }
                    if (requested_prod_batch_list != null) {
                        requested_prod_batch_list.clear();
                    }
                    if (req_prod_in_qnty_list != null) {
                        req_prod_in_qnty_list.clear();
                    }
                    if (requested_product_selling_price_list != null) {
                        requested_product_selling_price_list.clear();
                    }
                    if (request_products_selected != null) {
                        request_products_selected.clear();
                    }
                    if (requested_products_selected_id_list != null) {
                        requested_products_selected_id_list.clear();
                    }
                    if (qntyCount != null) {
                        qntyCount.clear();
                    }
                    if (total_view!=null) {
                        total_view.setText("Total:");
                    }
                    if (myView!=null) {
                        ViewGroup.LayoutParams params = myView.getLayoutParams();
                        params.height = 0;
                        myView.setLayoutParams(params);
                        myView.requestLayout();
                    }


                    getRawData = new GetRawData(this, getActivity());
                    isDownloading = true;
                    //   circular_progress.setVisibility(View.VISIBLE);
                    isProd_running = true;
                    getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/order/create", access_token);


                }
            }

        }


        //INSERT CUSTOM CODE HERE
    }


    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
      //      circular_progress.setVisibility(View.INVISIBLE);
            Log.d(TAG, data);

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
            if (isProd_running==true) {
                isDownloading=false;
                isProd_running=false;
                try {
                    JSONObject jsonData = new JSONObject(myString.toString());
                    mySharedPreferences.edit().putString("create_order_data", myString.toString()).apply();
                    JSONObject dataObject = jsonData.getJSONObject("data");
                    JSONArray itemsArray = dataObject.getJSONArray("products");
                    array_checked = new Boolean[itemsArray.length()];
                    qntyArray = new int[itemsArray.length()];
                    for (int i = 0; i < array_checked.length; i++) {
                        array_checked[i] = false;
                        qntyArray[i] = 0;
                    }
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject currentObject = itemsArray.getJSONObject(i);

                        product_id_list.add(currentObject.getString("id"));
                        order_product_batch_list.add(currentObject.getString("batch_no"));
                        order_product_name_list.add(currentObject.getString("name"));
                        order_code_list.add(currentObject.getString("code"));
                        order_selling_price_list.add(currentObject.getString("selling_price"));
                        order_isStock_qnty_list.add(currentObject.getString("in_stock_quantity"));
                        order_category_main_list.add(currentObject.getJSONObject("category").getString("main"));
                        order_category_sub_list.add(currentObject.getJSONObject("category").getString("sub"));
                        name_and_qnty_list.add(order_product_name_list.get(i+1) + "-" + order_product_batch_list.get(i)+ " (" +order_isStock_qnty_list.get(i) + ")");


                    }


                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                Log.d(TAG, data);
                Log.d(TAG, "GOT DATAAAAAAAAAAAAAAAAAA");
                // Log.d(TAG, String.valueOf(productStockCodeList.size()));
                final ArrayAdapter<String> myadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, name_and_qnty_list);

                product_spinner.setAdapter(myadapter);
                final customAdapter CustomAdapter = new customAdapter();
                myView.setAdapter(CustomAdapter);


                product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i != 0) {
                            boolean id_check=false;
                            for (int x=0;x<requested_products_selected_id_list.size();x++)
                            {
                                if (requested_products_selected_id_list.get(x).equals(product_id_list.get(i-1)))
                                {
                                    if (requested_prod_batch_list.get(x).equals(order_product_batch_list.get(i-1))) {
                                        id_check = true;
                                    }
                                }
                            }
                            if (id_check==false) {
                                request_products_selected.add(order_product_name_list.get(i));
                                requested_products_selected_id_list.add(product_id_list.get(i - 1));
                                requested_product_selling_price_list.add(order_selling_price_list.get(i - 1));
                                requested_prod_batch_list.add(order_product_batch_list.get(i-1));
                                req_prod_in_qnty_list.add(order_isStock_qnty_list.get(i-1));
                                qntyCount.add("0");
                                CustomAdapter.notifyDataSetChanged();
                                Log.d(TAG, product_id_list.get(i - 1));
                                Log.d(TAG, String.valueOf(request_products_selected.size()));
                                int totalHeight = 0;
                                int desiredWidth = View.MeasureSpec.makeMeasureSpec(myView.getWidth(), View.MeasureSpec.AT_MOST);
                                for (int z = 0; z < CustomAdapter.getCount(); z++) {
                                    View listItem = CustomAdapter.getView(z, null, myView);
                                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                                    totalHeight += listItem.getMeasuredHeight();
                                }
                                ViewGroup.LayoutParams params = myView.getLayoutParams();
                                params.height = totalHeight + (myView.getDividerHeight() * (CustomAdapter.getCount() - 1));
                                myView.setLayoutParams(params);
                                myView.requestLayout();
                            }
                            else {
                                Toast.makeText(getActivity(),  getResources().getString(R.string.prod_alrea_added_string), Toast.LENGTH_SHORT).show();
                            }
                       /*

                       View listItem = CustomAdapter.getView(i, null, myView);

                       ViewGroup.LayoutParams params = myView.getLayoutParams();
                       params.height = params.height + listItem.getMeasuredHeight() + myView.getDividerHeight();
                       myView.setLayoutParams(params);
                       myView.requestLayout();
                       */
                        }
                        product_spinner.setSelection(0);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.d(TAG, "whats up");
                    }
                });
                getRawData = new GetRawData(order_create_product_fragment_view.this,getActivity());
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/customer?limit=10000",access_token);
                isCust_ruuning=true;
                isDownloading=true;

            }
            else if (isCust_ruuning==true)
            {
                isDownloading=false;

            isCust_ruuning=false;


                try {
                    JSONObject jsonData = new JSONObject(myString.toString());
                    mySharedPreferences.edit().putString("order_create_customer_list", myString.toString()).apply();
                    JSONArray customer_array = jsonData .getJSONArray("data");
                    /*
                    array_checked = new Boolean[itemsArray.length()];
                    qntyArray = new int[itemsArray.length()];
                    for (int i = 0; i < array_checked.length; i++) {
                        array_checked[i] = false;
                        qntyArray[i] = 0;
                    }
                    */
                    for (int i = 0; i < customer_array.length(); i++) {
                        JSONObject currentObject = customer_array.getJSONObject(i);
                        customer_id_list.add(currentObject.getString("id"));
                        customer_name_list.add(currentObject.getString("name"));
                        customer_address_list.add(currentObject.getString("address"));
                    }


                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }


                final ArrayAdapter<String> my2adpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, customer_name_list);
                customer_spinner.setAdapter(my2adpater);
                customer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        current_customer_id = customer_id_list.get(i);
                        current_cust_address = customer_address_list.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                customer_spinner.setSelection(0);
                customer_spinner.setSelected(true);

            }
/*
           customAdapter myAdapter = new customAdapter();
           myView.setAdapter(myAdapter);
           */
/*
            final ArrayAdapter<String> myadapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,order_product_name_list);
            product_Spinner.setAdapter(myadapter);
            */
/*
           myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   try {
                       InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                       inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

                   EditText seText = (EditText) view.findViewById(R.id.order_custom_qnty_edit_text);
                   if (!seText.getText().toString().equals("")) {
                       qntyArray[i] = Integer.valueOf(seText.getText().toString());
                   }
                   else {
                       qntyArray[i]=0;
                   }
                   Log.d(TAG, "Item Got Clicked");
                  CheckBox myBox = (CheckBox) view.findViewById(R.id.item_switch);
                  if (array_checked[i]==false) {
                      array_checked[i] = true;
                  }
                  else {
                      array_checked[i]=false;
                  }
                  myBox.setChecked(array_checked[i]);
               }
           });
           myView.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View view, MotionEvent motionEvent) {
                   Log.d(TAG, "Toucheddddddddd.........");
                   if (view instanceof EditText)
                   {
                       EditText myText = view.findViewById(R.id.order_custom_qnty_edit_text);
                       Log.d(TAG, "focusable to true");
                       myText.setFocusable(true);
                   }
                   else
                   {
                       EditText myText = view.findViewById(R.id.order_custom_qnty_edit_text);
                       myText.setFocusable(false);
                       Log.d(TAG,"On touch");
                   }
                   return false;
               }
           });
*/

        }

    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            isDownloading=false;
        //    circular_progress.setVisibility(View.INVISIBLE);
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
            return (request_products_selected.size());
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_request_list_item,null);
            // ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            /*
            TextView nameTextView = (TextView) view.findViewById(R.id.name_list);
            TextView StockCodeTextView = (TextView) view.findViewById(R.id.product_list);
            TextView costTextView = (TextView) view.findViewById(R.id.cost_list);
            StockCodeTextView.setText("Order Code: " + product_order_code_list.get(i));
            nameTextView.setText( productNameList.get(i));
            costTextView.setText("Status: " + order_delivered_date_list.get(i));
            */
            // imageView.setImageResource(R.drawable.desktop_pic_flat);
            final TextView id_view = (TextView) view.findViewById(R.id.request_product_list_id_tracker_text_view);
            ImageButton myButton = (ImageButton) view.findViewById(R.id.order_product_custom_list_item_image_button);
            final EditText qntyText = (EditText) view.findViewById(R.id.request_product_custom_list_item_qnty_edit_text);
            TextView product_name_view = (TextView) view.findViewById(R.id.order_product_custom_list_item_text_view);
            final TextView product_tot_amount = (TextView) view.findViewById(R.id.request_product_custom_list_item_amount_text_view);
            TextView product_price = (TextView) view.findViewById(R.id.request_product_custom_list_item_price_text_view);
            TextView prod_batch = (TextView) view.findViewById(R.id.request_product_custom_list_item_batch_text_view);
            id_view.setText(String.valueOf(i));
            if (i<qntyCount.size())
            {
                if (qntyCount.get(i).equals("0"))
                {
                    qntyText.setText("");
                    product_price.setText("");
                    product_tot_amount.setText("");
                }
                else {
                    qntyText.setText(qntyCount.get(i));
                    product_tot_amount.setText("Rs: " + String.valueOf(Integer.valueOf(requested_product_selling_price_list.get(i))*Integer.valueOf(qntyCount.get(i))));
                }
            }
            if (i<request_products_selected.size()) {
                product_name_view.setText(request_products_selected.get(i));
                product_price.setText("Rs: " + requested_product_selling_price_list.get(i));
                prod_batch.setText(requested_prod_batch_list.get(i));
            }
            qntyText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().equals(""))
                    {
                        if (!(Integer.valueOf(editable.toString())>Integer.valueOf(req_prod_in_qnty_list.get(Integer.valueOf(id_view.getText().toString())))))
                        {
                            qntyCount.set((Integer.valueOf(id_view.getText().toString())),editable.toString());
                            product_tot_amount.setText("Rs: " + String.valueOf(Integer.valueOf(qntyCount.get(Integer.valueOf(id_view.getText().toString())))*Integer.valueOf(requested_product_selling_price_list.get(Integer.valueOf(id_view.getText().toString())))));
                        }
                        else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.qnt_cant_great_stock)+" "+ req_prod_in_qnty_list.get(Integer.valueOf(id_view.getText().toString())), Toast.LENGTH_SHORT).show();
                            qntyText.setText("");
                        }
                    }
                    else {
                        qntyCount.set((Integer.valueOf(id_view.getText().toString())),"0");
                        product_tot_amount.setText("0");
                    }
                    int pri_int=0;
                    for (int z=0;z<requested_product_selling_price_list.size();z++)
                    {
                        pri_int= pri_int+ (Integer.valueOf(qntyCount.get(z)) * Integer.valueOf(requested_product_selling_price_list.get(z)));
                    }
                    total_view.setText(getResources().getString(R.string.total_string)+ ": " + String.valueOf(pri_int));
                }
            });
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(id_view.getText().toString())<request_products_selected.size())
                    {
                        requested_product_selling_price_list.remove(requested_product_selling_price_list.get(Integer.valueOf(id_view.getText().toString())));
                        requested_prod_batch_list.remove(requested_prod_batch_list.get(Integer.valueOf(id_view.getText().toString())));
                        request_products_selected.remove(request_products_selected.get(Integer.valueOf(id_view.getText().toString())));
                        requested_products_selected_id_list.remove(requested_products_selected_id_list.get(Integer.valueOf(id_view.getText().toString())));
                        req_prod_in_qnty_list.remove(req_prod_in_qnty_list.get(Integer.valueOf(id_view.getText().toString())));
                        qntyCount.remove(qntyCount.get(Integer.valueOf(id_view.getText().toString())));
                        int pri_int=0;
                        for (int x=0;x<requested_product_selling_price_list.size();x++)
                        {
                            pri_int= pri_int+ (Integer.valueOf(qntyCount.get(x)))*Integer.valueOf(requested_product_selling_price_list.get(x));
                        }
                        total_view.setText( getResources().getString(R.string.total_string)+ ": " + String.valueOf(pri_int));
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



            return view;
        }
    }
}

/*



    <TextView
        android:id="@+id/request_product_list_id_tracker_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:visibility="invisible" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:weightSum="2"
        android:orientation="vertical">
        <LinearLayout
            android:id="@+id/upper_horizontal"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_marginTop="8dp"
            android:orientation="horizontal"
            android:layout_weight="1"
            android:weightSum="1"
            app:layout_constraintTop_toTopOf="parent">

            <TextView
                android:id="@+id/request_product_custom_list_item_text_view"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="0.5"
                android:gravity="center_vertical"
                android:paddingLeft="8dp"
                android:text="Sunsilk Shampoo"
                android:textColor="@color/greyishBlack"
                android:textSize="16sp" />

            <View
                android:layout_width="2dp"
                android:layout_height="match_parent"
                android:background="@color/greyishBlack"
                />

            <TextView
                android:id="@+id/request_product_custom_list_item_price_text_view"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="0.35"
                android:gravity="center_vertical"
                android:paddingLeft="8dp"
                android:text="Price"
                android:textAlignment="center"
                android:textColor="@color/greyishBlack"
                android:textSize="16sp" />

            <android.support.constraint.ConstraintLayout
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="0.15">

                <ImageButton
                    android:id="@+id/request_product_custom_list_item_image_button"
                    android:layout_width="35dp"
                    android:layout_height="35dp"
                    android:background="@drawable/circular_button"
                    android:tint="@color/white"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintTop_toTopOf="parent"
                    app:srcCompat="@drawable/cross_icon" />
            </android.support.constraint.ConstraintLayout>


        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_marginBottom="8dp"
            android:layout_marginTop="8dp"
            android:layout_weight="1"
            android:orientation="horizontal"
            android:weightSum="1"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/upper_horizontal">

            <EditText
                android:id="@+id/request_product_custom_list_item_qnty_edit_text"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="0.5"
                android:background="@drawable/rounded_text_edit_stroked"
                android:ems="10"
                android:hint="Quantity"
                android:inputType="number"
                android:gravity="center_vertical"
                android:paddingLeft="16dp"
                android:textAlignment="textStart"
                android:textColor="@color/greyishBlack"
                android:textSize="16sp" />

            <TextView
                android:id="@+id/request_product_custom_list_item_amount_text_view"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="0.5"
                android:background="@drawable/rounded_text_edit_stroked"
                android:ems="10"
                android:gravity="center_vertical"

                android:hint="Amount"
                android:inputType="number"
                android:paddingLeft="16dp"
                android:textAlignment="textStart"
                android:textColor="@color/greyishBlack"
                android:textSize="16sp" />



 */

