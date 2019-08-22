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
/*

public class order_create_product_offline_fragment_view extends Fragment{
    private static final String TAG = "order_create_product_of";
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
    ArrayList<String> order_id_list;

    ProgressBar circular_progress;
    Button next_button;
    String access_token="";
    Boolean[] array_checked;
    int[] qntyArray;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_select_product_fragment,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_custom_order_product_select);
        circular_progress.setVisibility(View.INVISIBLE);
        order_product_name_list = new ArrayList<String>();
        order_code_list = new ArrayList<String>();
        order_buying_price_list = new ArrayList<String>();
        order_id_list= new ArrayList<String>();
        order_selling_price_list=new ArrayList<String>();
        order_isStock_qnty_list= new ArrayList<String>();
        order_category_main_list= new ArrayList<String>();
        order_category_sub_list= new ArrayList<String>();
        order_product_batch_list= new ArrayList<String>();

         next_button = (Button) view.findViewById(R.id.order_product_next_button);
         next_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
              boolean myCheck =true;
              int second_check=0;

              if (qntyArray!=null && array_checked!=null) {
                  for (int i = 0; i < qntyArray.length; i++) {
                      if (qntyArray[i] == 0 && array_checked[i]) {
                          //   Toast.makeText(getActivity(), "Quantity can't be zero!", Toast.LENGTH_LONG).show();
                          myCheck = false;
                      } else if (qntyArray[i] != 0 && !array_checked[i]) {
                          // Toast.makeText(getActivity(), "Items have to be checked!", Toast.LENGTH_LONG).show();
                          myCheck = false;
                      } else if (qntyArray[i] == 0 && !array_checked[i]) {
                          second_check = second_check + 1;
                      }
                  }

                  if (second_check == qntyArray.length) {
                      Toast.makeText(getActivity(), "Make Selection First!", Toast.LENGTH_SHORT).show();
                  } else if (myCheck) {
                      Toast.makeText(getActivity(), "Successful!", Toast.LENGTH_SHORT).show();
                      ((offline_order_activity)getActivity()).setProductData(qntyArray);
                      ((offline_order_activity)getActivity()).order_select_customer_viewPager();

                  } else {
                      Toast.makeText(getActivity(), "Invalid Selection or Quantity!", Toast.LENGTH_SHORT).show();
                  }
              }
              else {
                  Toast.makeText(getActivity(), "No products!", Toast.LENGTH_SHORT).show();
              }

             }
         });
        myView = (ListView) view.findViewById(R.id.order_create_product_list_view);

        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        String dataObjectString = mySharedPreferences.getString("create_order_data","abc");

        if (!dataObjectString.equals("abc"))
        {
          ShowData(dataObjectString);
          Log.d(TAG, "Got data");

        }
        else {
            Toast.makeText(getActivity(), "No data!", Toast.LENGTH_SHORT).show();
        }

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
            ((offline_order_activity) getActivity()).setItemChecked("create_order_product");
        }


        //INSERT CUSTOM CODE HERE
    }



    public void ShowData(String data) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
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
            try {
                JSONObject jsonData = new JSONObject(myString.toString());
                JSONObject dataObject = jsonData.getJSONObject("data");
                JSONArray itemsArray = dataObject.getJSONArray("products");
                array_checked = new Boolean[itemsArray.length()];
                qntyArray = new int[itemsArray.length()];
                for (int i=0; i<array_checked.length;i++)
                {
                    array_checked[i] = false;
                    qntyArray[i]=0;
                }
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);


                    order_product_batch_list.add(currentObject.getString("batch_no"));
                    order_product_name_list.add(currentObject.getString("name"));
                    order_code_list.add(currentObject.getString("code"));
                    order_selling_price_list.add(currentObject.getString("selling_price"));
                    order_isStock_qnty_list.add(currentObject.getString("in_stock_quantity"));
                    order_category_main_list.add(currentObject.getJSONObject("category").getString("main"));
                    order_category_sub_list.add(currentObject.getJSONObject("category").getString("sub"));


                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            Log.d(TAG, "GOT DATAAAAAAAAAAAAAAAAAA");
            // Log.d(TAG, String.valueOf(productStockCodeList.size()));

           customAdapter myAdapter = new customAdapter();
           myView.setAdapter(myAdapter);
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


        }

    }



    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return order_product_name_list.size();
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
            view = getLayoutInflater().inflate(R.layout.order_cusom_product,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.order_create_custom_image_view);
            TextView nameTextView = (TextView) view.findViewById(R.id.custom_order_product_name);
            TextView order_details = (TextView) view.findViewById(R.id.custom_order_product_details);
            TextView costTextView = (TextView) view.findViewById(R.id.custom_order_product_price);
            final EditText myText = (EditText) view.findViewById(R.id.order_custom_qnty_edit_text);
            final TextView sesdView = (TextView) view.findViewById(R.id.textView23_track_prod);
           //sesdView.setText(String.valueOf(i));
           final CheckBox myBox = (CheckBox) view.findViewById(R.id.item_switch);
            /*
             myBox.setChecked(array_checked[i]);
             myText.setText(String.valueOf(qntyArray[i]));
             if (qntyArray[i]==0)
             {
                 myText.setText("");
             }

            myBox.setChecked(array_checked[i]);
            myText.setText(String.valueOf(qntyArray[i]));
            sesdView.setText(String.valueOf(i));
            if (qntyArray[i]==0)
            {
                myText.setText("");
            }
            //myText.setText(String.valueOf(qntyArray[i]));
           // myText.setFocusable(false);
           /// myText.setFocusableInTouchMode(true);
            //myBox.setFocusable(false);
           myText.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View view, MotionEvent motionEvent) {
                   Log.d(TAG,"focuses clicked");
                 view.setFocusable(true);
                 view.setFocusableInTouchMode(true);
                   return false;
               }
           });
            myText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        Log.d(TAG, "Got the focus");
                        //Toast.makeText(getActivity(), "Got the focus", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Lost the focus");
                        // Toast.makeText(getActivity(), "Lost the focus", Toast.LENGTH_SHORT).show();
                        if (!sesdView.getText().toString().equals("") ) {
                            if ( !myText.getText().toString().equals(""))
                            {
                                qntyArray[Integer.valueOf(sesdView.getText().toString())] = Integer.valueOf(myText.getText().toString());
                                // Log.d(TAG, String.valueOf(qntyArray[Integer.valueOf(sesdView.getText().toString())]));
                                //     Toast.makeText(getActivity(),String.valueOf(qntyArray[Integer.valueOf(sesdView.getText().toString())]),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                qntyArray[Integer.valueOf(sesdView.getText().toString())]=0;
                            }

                        }

                    }
                }
            });
            myText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int x, KeyEvent keyEvent) {
                    boolean handled = false;

                    if (x == EditorInfo.IME_ACTION_DONE || x==EditorInfo.IME_ACTION_NEXT || x==EditorInfo.IME_ACTION_GO )  {
                        if (!myText.getText().toString().equals("")) {
                            // qntyArray[Integer.valueOf(sesdView.getText().toString())] = Integer.valueOf(myText.getText().toString());
                        }
                        else {
                            //    qntyArray[Integer.valueOf(sesdView.getText().toString())]=0;
                        }
                        try {
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "sd,nfkjsdbfjkdsb");
                        handled = true;
                    }
                    else {
                        //qntyArray[i]=Integer.valueOf(myText.getText().toString());
                    }
                    //myText.setText(String.valueOf(qntyArray[i]));
                    return handled;
                }
            });



            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (!(view instanceof EditText)) {

                        view.findViewById(R.id.item_switch).setFocusable(false);
                        view.findViewById(R.id.order_custom_qnty_edit_text).setFocusable(false);
                        if (!sesdView.getText().toString().equals("") ) {
                            if ( array_checked[Integer.valueOf(sesdView.getText().toString())]== true) {
                                array_checked[Integer.valueOf(sesdView.getText().toString())] = false;
                                myBox.setChecked(false);
                            }
                            else {
                                array_checked[Integer.valueOf(sesdView.getText().toString())] =true;
                                myBox.setChecked(true);
                            }
                        }

                    }
                    else
                    {
                        //   view.findViewById(R.id.item_switch).setFocusable(false);
                        //  view.findViewById(R.id.order_custom_qnty_edit_text).setFocusable(false);
                    }
                    return false;
                }
            });
            order_details.setText("Batch no: " +   order_product_batch_list.get(i) + "\n" + "Code: " + order_code_list.get(i)+ "\n" + "In Stock: " + order_isStock_qnty_list.get(i) + "\n");
            nameTextView.setText("Product: " + order_product_name_list.get(i));
            costTextView.setText("Selling Price: " + order_selling_price_list.get(i));
            imageView.setImageResource(R.drawable.desktop_pic_flat);
            return view;
        }


    }
}
*/


/**
 * Created by areebwaseem on 10/28/17.
 */

public class order_create_product_offline_fragment_view extends Fragment{
    private static final String TAG = "order_create_product_of";
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
    ArrayList<String> product_id_list;
    ArrayList<String> customer_name_list;
    ArrayList<String> req_prod_in_qnty_list;
    ArrayList<String> customer_id_list;
    ArrayList<String> qntyCount;
    ArrayList<String> request_products_selected;
    ArrayList<String> requested_products_selected_id_list;
    ArrayList<String> customer_address_list;
    ArrayList<String> name_and_qnty_list;
    ArrayList<String> requested_prod_batch_list;
    ArrayList<String> requested_product_selling_price_list;
    String current_cust_address;
    TextView total_view;
    String curren_cust_name="";
    SearchableSpinner product_spinner, customer_spinner;
    //  ProgressBar circular_progress;
    Button proceed_button;
    String access_token="";
    String current_customer_id="";
    Boolean[] array_checked;
    boolean isCust_ruuning=false;
    boolean isProd_running=false;
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
        req_prod_in_qnty_list= new ArrayList<String>();
        order_product_batch_list= new ArrayList<String>();
        customer_name_list= new ArrayList<String>();
        customer_id_list = new ArrayList<String>();
        customer_address_list = new ArrayList<String>();
        customer_name_list.add("Walking Client");
        customer_id_list.add("nil");
        customer_address_list.add("nil");
        requested_prod_batch_list = new ArrayList<String>();
        requested_product_selling_price_list = new ArrayList<String>();
         name_and_qnty_list = new ArrayList<String>();
        request_products_selected = new ArrayList<String>();
        requested_products_selected_id_list= new ArrayList<String>();
        order_product_name_list.add(getResources().getString(R.string.spin_select_products_str_resource));
        name_and_qnty_list.add(getResources().getString(R.string.spin_select_products_str_resource));
        total_view = (TextView) view.findViewById(R.id.custom_order_updated_total_amount_text_view);
        if (getActivity()!=null) {
            ((offline_order_activity) getActivity()).isNew = false;
        }

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
                        ((offline_order_activity) getActivity()).setProductData(qntyArray);
                        ((offline_order_activity) getActivity()).current_customer_id = current_customer_id;
                        ( (offline_order_activity) getActivity()).current_customer_name= curren_cust_name;
                        ((offline_order_activity) getActivity()).current_customer_address= current_cust_address;
                        ((offline_order_activity) getActivity()).setUpPaymentViewPager();

                    }
                    else if (current_customer_id.equals(""))
                    {
                        Toast.makeText(getActivity(), getResources().getString(R.string.invali_cust_string), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.invalid_qnty_string), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_prods_string), Toast.LENGTH_SHORT).show();
                }
            }
        });

        myView = (ListView) view.findViewById(R.id.custom_order_product_list_view_order);

        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);

        String dataObjectString = mySharedPreferences.getString("create_order_data", "abc");
        if (!dataObjectString.equals("abc")) {
            isProd_running = true;
            ShowData(dataObjectString);
            Log.d(TAG, "Got data");

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_data_string), Toast.LENGTH_SHORT).show();
        }
        String customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");
        if (!customer_data.equals("abc")) {

            ShowCustomerData(customer_data);
            isCust_ruuning = true;
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_cust_data_string), Toast.LENGTH_SHORT).show();
        }




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
            ((offline_order_activity) getActivity()).setItemChecked("create_order_product");

            if (((offline_order_activity) getActivity()).isNew == true) {
                ( (offline_order_activity)getActivity()).isNew=false;
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
                if (total_view != null) {
                    total_view.setText("Total:");
                }
                    if (myView!=null) {
                        ViewGroup.LayoutParams params = myView.getLayoutParams();
                        params.height = 0;
                        myView.setLayoutParams(params);
                        myView.requestLayout();
                    }

                String dataObjectString = mySharedPreferences.getString("create_order_data", "abc");
                if (!dataObjectString.equals("abc")) {
                    isProd_running = true;
                    ShowData(dataObjectString);
                    Log.d(TAG, "Got data");

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_data_string), Toast.LENGTH_SHORT).show();
                }
                String customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");
                if (!customer_data.equals("abc")) {

                    ShowCustomerData(customer_data);
                    isCust_ruuning = true;
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_cust_data_string), Toast.LENGTH_SHORT).show();
                }


            }
        }



        //INSERT CUSTOM CODE HERE
    }


    public void ShowData(String data) {
        if (getActivity() != null) {
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
                try {
                    JSONObject jsonData = new JSONObject(myString.toString());
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
                      //  name_and_qnty_list.add(order_product_name_list.get(i+1) + " [" + order_isStock_qnty_list.get(i) + "]");
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
                                Toast.makeText(getActivity(), getResources().getString(R.string.prod_alrea_added_string), Toast.LENGTH_SHORT).show();
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

            product_spinner.setSelection(0);
            }
    }
    public void ShowCustomerData(String data)
    {
        if (getActivity()!=null) {
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
                JSONArray customer_array = jsonData.getJSONArray("data");
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
                    curren_cust_name = customer_name_list.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }

/*

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_request_list_item,null);
            // ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            */
     /*
            // imageView.setImageResource(R.drawable.desktop_pic_flat);
           // final TextView id_view = (TextView) view.findViewById(R.id.request_product_list_id_tracker_text_view);
            ImageButton myButton = (ImageButton) view.findViewById(R.id.request_product_custom_list_item_image_button);
          //  EditText qntyText = (EditText) view.findViewById(R.id.request_product_custom_list_item_edit_text);
            TextView product_name_view = (TextView) view.findViewById(R.id.request_product_custom_list_item_text_view);
            id_view.setText(String.valueOf(i));
            if (i<request_products_selected.size()) {
                product_name_view.setText(request_products_selected.get(i));
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
                        qntyCount.set((Integer.valueOf(id_view.getText().toString())),editable.toString());
                    }
                    else {
                        qntyCount.set((Integer.valueOf(id_view.getText().toString())),"0");
                    }
                }
            });

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(id_view.getText().toString())<request_products_selected.size())
                    {
                        request_products_selected.remove(request_products_selected.get(Integer.valueOf(id_view.getText().toString())));
                        requested_products_selected_id_list.remove(requested_products_selected_id_list.get(Integer.valueOf(id_view.getText().toString())));
                        qntyCount.remove(qntyCount.get(Integer.valueOf(id_view.getText().toString())));
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
    */
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
    public View getView(int i, View view, ViewGroup viewGroup) {
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
                        Toast.makeText(getActivity(), "Quantity can't be greater than in stock: " + req_prod_in_qnty_list.get(Integer.valueOf(id_view.getText().toString())), Toast.LENGTH_SHORT).show();
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
                    total_view.setText(getResources().getString(R.string.total_string)+": " + String.valueOf(pri_int));
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

