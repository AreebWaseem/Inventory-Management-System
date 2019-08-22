package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class orders_payment_offline_fragment_view_detail extends Fragment {
    private static final String TAG = "orders_payment_offline_";
    GetRawData getRawData;
    ListView myView;
    String invoice_no = "";
    SharedPreferences mySharedPreferences;
    ArrayList<String> prod_no;
    ArrayList<String> prod_descp;
    ArrayList<String> prod_selling;
    ArrayList<String> prod_buying;
    ArrayList<String> prod_qua;
    ArrayList<String> prod_total;
    ArrayList<String> product_id;
    ArrayList<String> payment_methods;
    ArrayList<String> prod_qnty_array;
    ArrayList<String> prod_batch_list;
    ArrayList<String> prod_ctg_main_list_array;
    ArrayList<String> prod_ctg_sub_list_array;
    ProgressBar circular_progress;
    String url_detail = "";
    String access_token = "";
    String current_customer_address = "";
    String selected_payment_method = "";
    String sub_Total = "";
    String tax = "";
    String grand_Total = "";
    String received_Amount = "";
    String remaining_Amount = "";
    TextView order_no_Top;
    String current_customer_name = "";
    TextView order_payment_method;
    TextView cust_billing_info;
    boolean downloadStatus = false;
    ImageButton order_confirmation;
    TextView _subtotal;
    TextView _discount;
    TextView _tax;
    TextView _grand_total;
    TextView _received_amount;
   // TextView _remaining_amount;
    EditText received_payment;
    EditText discount_payment;
    EditText tax_info;
    EditText chequeNo;
    EditText cheque_date;
    EditText chequeDrawee;
    LinearLayout layout_hide;
    LinearLayout.LayoutParams params;
    TextView amount_return;
    EditText ord_pay_ref;
    MaterialSpinner mySpin;
    EditText lowest;
    TextView second_top;
    LoginPostRequest order_initiate_post_reuqest;
    String current_order_id;
    ListView payment_method_view;
    int current_index = 0;
    boolean isDownloading = false;
    int qntyarray[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_setup_payment_details,container,false);
        myView = (ListView) view.findViewById(R.id.order_details_list_view_payment);
        order_no_Top = (TextView) view.findViewById(R.id.order_number_text_view_payment_payment);
        lowest = (EditText) view.findViewById(R.id.lowestViewCommentsOrder_payment);
        chequeNo = (EditText) view.findViewById(R.id.editText_cheque_no);
        cheque_date = (EditText) view.findViewById(R.id.editText115_cheque_date);
        chequeDrawee = (EditText) view.findViewById(R.id.editText171_drawee_bank);

        //  order_payment_method= (TextView) view.findViewById(R.id.order_date_method_text_view_payment);
        cust_billing_info = (TextView) view.findViewById(R.id.customer_order_billing_info_detail_text_view_payment);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_order_payment_view);
        circular_progress.setVisibility(View.INVISIBLE);
        _discount = (TextView) view.findViewById(R.id.order_discount_text_view_payment);
        layout_hide = (LinearLayout) view.findViewById(R.id.layout_to_hide);
        params = (LinearLayout.LayoutParams) layout_hide.getLayoutParams();
        params.height=0;
        layout_hide.setLayoutParams(params);
        _subtotal= (TextView) view.findViewById(R.id.order_subtotal_text_view_payment);
        _grand_total = (TextView) view.findViewById(R.id.order_grand_total_text_view_payment);
        //    _received_amount = (TextView) view.findViewById(R.id.order_received_amount_edit_text_payment);
       // _remaining_amount = (TextView) view.findViewById(R.id.order_remaining_amount_text_view_payment);
        amount_return=(TextView) view.findViewById(R.id.order_amount_returned_text_view_payment);
        ord_pay_ref = (EditText) view.findViewById(R.id.order_pay_ref_edit_text_view_payment);

        //    pay_meth = (MaterialBetterSpinner) view.findViewById(R.id.payment_method_option_spinner);
        //  second_top = (TextView) view.findViewById(R.id.order_date_method_text_view);
        product_id = new ArrayList<String>();
        prod_no = new ArrayList<String>();
        prod_descp = new ArrayList<String>();
        prod_selling = new ArrayList<String>();
        prod_buying = new ArrayList<String>();
        prod_qua = new ArrayList<String>();
        prod_total = new ArrayList<String>();
        payment_methods = new ArrayList<String>();
        prod_qnty_array = new ArrayList<String>();
        prod_batch_list = new ArrayList<String>();
        prod_ctg_main_list_array= new ArrayList<String>();
        prod_ctg_sub_list_array = new ArrayList<String>();
        received_payment = (EditText) view.findViewById(R.id.order_received_amount_edit_text_payment);
        discount_payment = (EditText) view.findViewById(R.id.order_discount_edit_text_);
        order_confirmation = (ImageButton) getActivity().findViewById(R.id.image_button_order_confirmation);
        mySpin = (MaterialSpinner) view.findViewById(R.id.cust_pay_meth_spin);




        received_payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getActivity()!=null)
                {

                    if (!received_payment.getText().toString().equals("") && !_grand_total.getText().toString().equals("")){
                        amount_return.setText(String.valueOf((Integer.valueOf(received_payment.getText().toString()))-Integer.valueOf(_grand_total.getText().toString())));
                     //   _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString())-Integer.valueOf(received_payment.getText().toString())));

                    }
                    else {
                        amount_return.setText("");
                    }
                    //   _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) - Integer.valueOf(_received_amount.getText().toString())));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        discount_payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getActivity()!=null)
                {

                    if (!discount_payment.getText().toString().equals("") ) {
                        if (Integer.valueOf(discount_payment.getText().toString())>100)
                        {
                            discount_payment.setText("");
                            _discount.setText("");
                            Toast.makeText(getActivity(), getResources().getString(R.string.enter_vali_percent), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (!discount_payment.getText().toString().equals("")&&!_grand_total.getText().toString().equals("")) {
                                _discount.setText(String.valueOf(Math.round((Float.valueOf(discount_payment.getText().toString()) / 100) * Float.valueOf(_grand_total.getText().toString()))));
                                if (!_discount.getText().toString().equals("") && !_subtotal.getText().toString().equals("")) {
                                    _grand_total.setText(String.valueOf(Integer.valueOf(_subtotal.getText().toString()) - Integer.valueOf(_discount.getText().toString())));
                                }
                            }
                            if (!received_payment.getText().toString().equals("")) {
                                if (!_grand_total.getText().toString().equals("")) {
                                    amount_return.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) - Integer.valueOf(received_payment.getText().toString())));
                                //    _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) - Integer.valueOf(received_payment.getText().toString())));
                                }
                            }
                            else {
                                if (!_grand_total.getText().toString().equals("")) {
                                 //   _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString())));
                                }

                            }
                        }

                    }
                    else {
                        _grand_total.setText(_subtotal.getText());
                        _discount.setText("");
                        if (!_grand_total.getText().toString().equals("") && !received_payment.getText().toString().equals(""))
                        {
                          //  _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) - Integer.valueOf(received_payment.getText().toString())));
                            amount_return.setText(String.valueOf((Integer.valueOf(received_payment.getText().toString()))-Integer.valueOf(_grand_total.getText().toString())));
                        }
                        else {
                           // _remaining_amount.setText(_grand_total.getText().toString());
                            amount_return.setText("");
                        }

                    }


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



/*
        View view = inflater.inflate(R.layout.order_setup_payment_details, container, false);
        myView = (ListView) view.findViewById(R.id.order_details_list_view_payment);
        order_no_Top = (TextView) view.findViewById(R.id.order_number_text_view_payment_payment);
        lowest = (EditText) view.findViewById(R.id.lowestViewCommentsOrder_payment);
        order_payment_method= (TextView) view.findViewById(R.id.order_date_method_text_view_payment);
        cust_billing_info = (TextView) view.findViewById(R.id.customer_order_billing_info_detail_text_view_payment);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_order_payment_view);
        circular_progress.setVisibility(View.INVISIBLE);
        _discount = (TextView) view.findViewById(R.id.order_discount_text_view_payment);
        _subtotal= (TextView) view.findViewById(R.id.order_subtotal_text_view_payment);
        _tax = (TextView) view.findViewById(R.id.order_tax_text_view_payment);
        _grand_total = (TextView) view.findViewById(R.id.order_grand_total_text_view_payment);
        _received_amount = (TextView) view.findViewById(R.id.order_received_amount_text_view_payment);
        _remaining_amount = (TextView) view.findViewById(R.id.order_remaining_amount_text_view_payment);
      //  second_top = (TextView) view.findViewById(R.id.order_date_method_text_view);
        product_id = new ArrayList<String>();
        prod_no = new ArrayList<String>();
        prod_descp = new ArrayList<String>();
        prod_selling = new ArrayList<String>();
        prod_buying = new ArrayList<String>();
        prod_qua = new ArrayList<String>();
        prod_total = new ArrayList<String>();
        payment_methods = new ArrayList<String>();
        prod_qnty_array = new ArrayList<String>();
        prod_batch_list = new ArrayList<String>();
        prod_ctg_main_list_array= new ArrayList<String>();
        prod_ctg_sub_list_array = new ArrayList<String>();
        received_payment = (EditText) view.findViewById(R.id.editText3_payment);
        discount_payment = (EditText) view.findViewById(R.id.editText_payment);
        order_confirmation = (ImageButton) getActivity().findViewById(R.id.image_button_order_confirmation);
        tax_info = (EditText) view.findViewById(R.id.editText2_payment);
        received_payment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            if (getActivity()!=null) {
              if (!b)
              {
                  if (!received_payment.getText().toString().equals("")) {
                      _received_amount.setText(received_payment.getText().toString());
                  }
                      _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) - Integer.valueOf(_received_amount.getText().toString())));

              }
            }
            }
        });
        discount_payment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (getActivity()!=null) {
                    if (!b) {
                        if (!discount_payment.getText().toString().equals("")) {
                            _discount.setText(discount_payment.getText().toString());
                        }
                        _grand_total.setText(String.valueOf(Integer.valueOf(_subtotal.getText().toString()) + Integer.valueOf(_tax.getText().toString()) - Integer.valueOf(_discount.getText().toString())));
                        _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) -  Integer.valueOf(_received_amount.getText().toString())));

                    }
                }
            }
        });
        tax_info.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (getActivity()!=null) {
                    if(!b) {
                        if (!tax_info.getText().toString().equals("")) {
                            _tax.setText(tax_info.getText().toString());
                        }
                        _grand_total.setText(String.valueOf(Integer.valueOf(_subtotal.getText().toString()) + Integer.valueOf(_tax.getText().toString()) - Integer.valueOf(_discount.getText().toString())));
                        _remaining_amount.setText(String.valueOf(Integer.valueOf(_grand_total.getText().toString()) -  Integer.valueOf(_received_amount.getText().toString())));

                    }
                }
            }
        });
        payment_method_view = (ListView) view.findViewById(R.id.payment_method_list_view);
        downloadStatus=false;





        */


        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);


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

            ((offline_order_activity) getActivity()).setItemChecked("order_payment_offline");

            _discount.setText("");
            discount_payment.setText("");
            received_payment.setText("");
            amount_return.setText("");

            if (prod_no != null) {
                prod_no.clear();
            }
            if (prod_descp != null) {
                prod_descp.clear();
            }
            if (prod_selling != null) {
                prod_selling.clear();
            }
            if (prod_buying != null) {
                prod_buying.clear();
            }
            if (prod_qua != null) {
                prod_qua.clear();
            }
            if (prod_total != null) {
                prod_total.clear();
            }
            if (product_id != null) {
                product_id.clear();
            }
            if (prod_qnty_array != null) {
                prod_qnty_array.clear();
            }
            if (prod_batch_list != null) {
                prod_batch_list.clear();
            }
            if (prod_ctg_main_list_array != null) {
                prod_ctg_main_list_array.clear();
            }
            if (prod_ctg_sub_list_array != null) {
                prod_ctg_sub_list_array.clear();
            }
            if (payment_methods!=null)
            {
                payment_methods.clear();
            }
            if (getActivity() != null) {
                order_confirmation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() != null) {
/*
                            JSONObject order_data = new JSONObject();
                            JSONArray products_Array = new JSONArray();
                            for (int i = 0; i < prod_no.size(); i++) {
                                JSONObject myCurrentProduct = new JSONObject();
                                try {
                                    myCurrentProduct.put("id", Integer.valueOf(product_id.get(i)));
                                    myCurrentProduct.put("batch", prod_batch_list.get(i));
                                    myCurrentProduct.put("quantity", qntyarray[i]);
                                    myCurrentProduct.put("buying_price", Integer.valueOf(prod_buying.get(i)));
                                    myCurrentProduct.put("selling_price", Integer.valueOf(prod_selling.get(i)));
                                    if (!_subtotal.getText().toString().equals("")) {
                                        myCurrentProduct.put("subtotal", Integer.valueOf(_subtotal.getText().toString()));
                                    }
                                    products_Array.put(i, myCurrentProduct);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                order_data.put("customer_id", ((offline_order_activity) getActivity()).current_customer_id);
                                order_data.put("current_customer_name", current_customer_name);
                                order_data.put("products", products_Array);
                                if (!_discount.getText().toString().equals("") && !_grand_total.getText().toString().equals("")) {
                                    JSONObject myObj = new JSONObject();
                                    myObj.put("amount", Integer.valueOf(_discount.getText().toString()));
                                    myObj.put("percent", (Integer.valueOf(_discount.getText().toString()) / Integer.valueOf(_grand_total.getText().toString())) * 100);
                                    order_data.put("discount", myObj);
                                }
                                if (!_subtotal.getText().toString().equals("")) {
                                    order_data.put("sub_total", Integer.valueOf(_subtotal.getText().toString()));
                                }
                                if (!_grand_total.getText().toString().equals("")) {
                                    order_data.put("grand_total", Integer.valueOf(_grand_total.getText().toString()));
                                }
                                if (!_grand_total.getText().toString().equals("")) {
                                    order_data.put("received_amount", Integer.valueOf(_received_amount.getText().toString()));
                                }
                                if (!_grand_total.getText().toString().equals("") && !_received_amount.getText().toString().equals("")) {
                                    order_data.put("returned_amount", Integer.valueOf(_received_amount.getText().toString()) - Integer.valueOf(_grand_total.getText().toString()));
                                }
                                boolean pay_check = false;
                                if (!selected_payment_method.equals("")) {
                                    order_data.put("payment_method", selected_payment_method);
                                    pay_check = true;
                                }
                                order_data.put("payment_ref", "");
                                order_data.put("shipping_address", current_customer_address);
                                order_data.put("note", lowest.getText().toString());
                                String final_order = String.valueOf(order_data);
                                Log.d(TAG, String.valueOf(order_data));






*/


                                    JSONObject order_data = new JSONObject();
                                    JSONArray products_Array = new JSONArray();
                                    for (int i = 0; i < prod_no.size(); i++) {
                                        JSONObject myCurrentProduct = new JSONObject();
                                        try {
                                            myCurrentProduct.put("id", Integer.valueOf(product_id.get(i)));
                                            myCurrentProduct.put("batch", prod_batch_list.get(i));
                                            myCurrentProduct.put("quantity", Integer.valueOf(prod_qua.get(i)));
                                            myCurrentProduct.put("buying_price", Integer.valueOf(prod_buying.get(i)));
                                            myCurrentProduct.put("selling_price", Integer.valueOf(prod_selling.get(i)));
                                    /*
                                    if (!_subtotal.getText().toString().equals("")) {
                                        myCurrentProduct.put("subtotal", Integer.valueOf(_subtotal.getText().toString()));
                                    }
                                    */
                                            myCurrentProduct.put("subtotal", String.valueOf((Integer.valueOf(prod_selling.get(i))) * Integer.valueOf(prod_qua.get(i))));
                                            myCurrentProduct.put("serial_numbers", "");
                                            products_Array.put(i, myCurrentProduct);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    try {
                                        if (!(((offline_order_activity) getActivity()).current_customer_id.equals("nil"))) {
                                            order_data.put("customer_id", ((offline_order_activity) getActivity()).current_customer_id);
                                            order_data.put("current_customer_name", current_customer_name);
                                        } else {
                                            order_data.put("customer_id", "-1000");
                                            order_data.put("current_customer_name", current_customer_name);
                                        }
                                        order_data.put("products", products_Array);
                                        if (!_discount.getText().toString().equals("") && !discount_payment.getText().toString().equals("") && !_grand_total.getText().toString().equals("")) {
                                            JSONObject myObj = new JSONObject();
                                            myObj.put("amount", Integer.valueOf(_discount.getText().toString()));
                                            myObj.put("percent", Float.valueOf(discount_payment.getText().toString()));
                                            order_data.put("discount", myObj);
                                        } else {
                                            JSONObject myObj = new JSONObject();
                                            myObj.put("amount", 0);
                                            myObj.put("percent", 0);
                                            order_data.put("discount", myObj);
                                        }
                                        if (!_subtotal.getText().toString().equals("")) {
                                            order_data.put("sub_total", Integer.valueOf(_subtotal.getText().toString()));
                                        }
                                        if (!_grand_total.getText().toString().equals("")) {
                                            order_data.put("grand_total", Integer.valueOf(_grand_total.getText().toString()));
                                        }
                                        if (!received_payment.getText().toString().equals("")) {
                                            order_data.put("received_amount", Integer.valueOf(received_payment.getText().toString()));
                                        }
                                        if (!_grand_total.getText().toString().equals("") && !received_payment.getText().toString().equals("") && !amount_return.getText().toString().equals("")) {
                                            order_data.put("returned_amount", Integer.valueOf(amount_return.getText().toString()));
                                        } else {
                                            order_data.put("returned_amount", 0);
                                        }
                                        boolean pay_check = false;
                                        if (!selected_payment_method.equals("") && !received_payment.getText().toString().equals("")) {
                                            order_data.put("payment_method", selected_payment_method);
                                            pay_check = true;
                                            if (selected_payment_method.equals("cheque")) {
                                                if (!chequeNo.getText().toString().equals("") && !cheque_date.getText().toString().equals("") && !chequeDrawee.getText().toString().equals("")) {
                                                    JSONObject myObj = new JSONObject();
                                                    myObj.put("cheque_no", chequeNo.getText().toString());
                                                    myObj.put("cheque_date", cheque_date.getText().toString());
                                                    myObj.put("drawee_bank", chequeDrawee.getText().toString());
                                                    order_data.put("cheque_info", myObj);
                                                } else {
                                                    pay_check = false;
                                                }
                                            }
                                        }
                                        boolean grand_check=false;
                                        if (!_grand_total.getText().toString().equals("") && !received_payment.getText().toString().equals("")) {

                                            if (!(Integer.valueOf(_grand_total.getText().toString()) > Integer.valueOf(received_payment.getText().toString()))) {
                                                grand_check=true;
                                            }
                                        }
                                        order_data.put("payment_ref", "");
                                        order_data.put("payment_ref", ord_pay_ref.getText().toString());
                                        order_data.put("shipping_address", current_customer_address);
                                        order_data.put("note", lowest.getText().toString());
                                        String final_order = String.valueOf(order_data);
                                        Log.d(TAG, String.valueOf(order_data));


                                        if (final_order != null && pay_check == true && grand_check==true) {

                                            DataBaseHelper myHelper = new DataBaseHelper(getActivity());
                                            boolean myBool;
                                            myBool = myHelper.insertData_to_order(final_order);
                                            if (myBool == false) {
                                                Log.d(TAG, "Error");

                                            } else {
                                                Log.d(TAG, "Successfulllll");
                                            }

                                            Cursor res = myHelper.getAllData_Order();
                                            if (res != null) {
                                                StringBuffer buffer = new StringBuffer();
                                                while (res.moveToNext()) {
                                                    buffer.append("ID: " + res.getString(0) + "\n");
                                                    buffer.append("Name: " + res.getString(1) + "\n");

                                                }
                                                Log.d(TAG, buffer.toString());
                                            }


                                            Toast.makeText(getActivity(), getResources().getString(R.string.successful_string), Toast.LENGTH_LONG).show();
                                            ((offline_order_activity) getActivity()).order_confirmed_set_view_pager();


                                        } else {
                                            if (selected_payment_method.equals("cheque")) {
                                                if (chequeNo.getText().toString().equals("") || chequeDrawee.getText().toString().equals("") || cheque_date.getText().toString().equals("")) {
                                                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_comp_check), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            else if (received_payment.getText().toString().equals("")){
                                                Toast.makeText(getActivity(), getResources().getString(R.string.enter_recei_amount), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getActivity(), "Received Amount cant be less than grand total!", Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }



                    }
                });
            }
            qntyarray = new int[((offline_order_activity) getActivity()).myQuantity.length];

            for (int i = 0; i < ((offline_order_activity) getActivity()).myQuantity.length; i++) {
                qntyarray[i] = ((offline_order_activity) getActivity()).myQuantity[i];
            }
            ((offline_order_activity) getActivity()).setItemChecked("order_payment_offline");

            current_order_id = ((offline_order_activity) getActivity()).order_current;
            String dataObjectString = mySharedPreferences.getString("create_order_data", "abc");

            if (!dataObjectString.equals("abc")) {
                ShowData(dataObjectString);
                Log.d(TAG, "Got data");

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_data_string), Toast.LENGTH_SHORT).show();
            }

            //   url_detail = "http://cloud.mdevsolutions.com/inventory/develop/api/order/create" + current_order_id;
            //  getRawData.execute(url_detail, access_token);


        }
    }


    public void ShowData(String data) {
        if (getActivity()!=null) {

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONObject dataObject = jsonData.getJSONObject("data");
                // JSONObject json_order_info = dataObject.getJSONObject("order_info");
                // order_no_Top.setText("Order No: " + json_order_info.getString("order_no"));
                // order_payment_method.setText("Date: " + json_order_info.getString("order_date") + "\n" + " Payment Method: " + json_order_info.getString("payment_method"));
                //  cust_billing_info.setText("Name: " + json_order_info.getString("customer_name") + "\n" + "Email " + json_order_info.getString("customer_email") + "\n" + "Contact: " + json_order_info.getString("customer_phone") + "\n" + "Customer Address: " + json_order_info.getString("customer_address") + "\n" + "Shipping Address: " + json_order_info.getString("shipping_address"));
                JSONArray order_details_array = dataObject.getJSONArray("products");
                JSONArray payment_methods_array = dataObject.getJSONArray("payment_methods");
                JSONArray customer_details_array = dataObject.getJSONArray("customers");

                for (int p=0;p<payment_methods_array.length();p++)
                {
                    payment_methods.add(payment_methods_array.getString(p));
                }
                selected_payment_method= payment_methods.get(0);

                ArrayAdapter myAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,payment_methods);
                mySpin.setAdapter(myAdapter);
                mySpin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                        selected_payment_method = payment_methods.get(position);
                        cust_billing_info.setText(getResources().getString(R.string.name_string) + ": " + current_customer_name+ "\n" + getResources().getString(R.string.address_string) + ": " + current_customer_address + "\n" + getResources().getString(R.string.payment_by_string)+ ": " + selected_payment_method);

                        if (selected_payment_method.equals("cheque"))
                        {
                            params = (LinearLayout.LayoutParams) layout_hide.getLayoutParams();
                            params.height= LinearLayout.LayoutParams.WRAP_CONTENT;
                            layout_hide.setLayoutParams(params);
                        }
                        else {
                            params = (LinearLayout.LayoutParams) layout_hide.getLayoutParams();
                            params.height=0;
                            layout_hide.setLayoutParams(params);
                        }

                    }
                });

                int cust_id=0;


                if ((((offline_order_activity)getActivity()).current_customer_id).equals("nil"))
                {
                    cust_id=-1000;
                    current_customer_name = "Walking Client";
                    current_customer_address="";
                    cust_billing_info.setText(getResources().getString(R.string.name_string) + ": " + current_customer_name );
                }
                else {
                    cust_id = Integer.valueOf(((offline_order_activity)getActivity()).current_customer_id);

                    for (int c = 0; c < customer_details_array.length(); c++) {
                        JSONObject custObj = customer_details_array.getJSONObject(c);
                        if (Integer.valueOf(custObj.getString("id")) == cust_id) {
                            current_customer_name = custObj.getString("name");
                            current_customer_address = ((offline_order_activity) getActivity()).current_customer_address;
                            cust_billing_info.setText(getResources().getString(R.string.name_string) + ": " + current_customer_name + "\n" + getResources().getString(R.string.address_string) + ": " + current_customer_address + "\n" + getResources().getString(R.string.payment_by_string) + ": " + selected_payment_method);
                        }
                    }
                }


                int x=0;
                for (int i = 0; i < order_details_array.length(); i++) {
                    if (qntyarray[i]!=0) {
                        JSONObject myObject = order_details_array.getJSONObject(i);
                        x=x+1;
                        prod_no.add(String.valueOf(x));
                        product_id.add(myObject.getString("id"));
                        prod_descp.add(myObject.getString("name"));
                        prod_buying.add(myObject.getString("buying_price"));
                        prod_qnty_array.add(myObject.getString("in_stock_quantity"));
                        prod_selling.add(myObject.getString("selling_price"));
                        prod_batch_list.add(myObject.getString("batch_no"));
                        prod_ctg_main_list_array.add(myObject.getJSONObject("category").getString("main"));
                        prod_ctg_sub_list_array.add(myObject.getJSONObject("category").getString("sub"));
                        prod_qua.add(String.valueOf(qntyarray[i]));
                        int subtotal = qntyarray[i] * Integer.valueOf(myObject.getString("selling_price"));
                        prod_total.add(String.valueOf(subtotal));
                    }
                }
                int subtot=0;
                for (int sub=0;sub<prod_total.size();sub++)
                {
                    subtot = subtot+ Integer.valueOf(prod_total.get(sub));
                }
                _subtotal.setText(String.valueOf(subtot));
                _discount.setText("0");
                _grand_total.setText(_subtotal.getText().toString());
              //  _remaining_amount.setText(_grand_total.getText().toString());
                // Toast.makeText(getActivity(), String.valueOf(prod_no.size()),Toast.LENGTH_LONG).show();
                // JSONArray json_order_details = dataObject.getJSONObject();


            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            final customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            CountDownTimer countDownTimer = new CountDownTimer(500, 100) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
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
                    cancel();
                }
            }.start();



/*

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONObject dataObject = jsonData.getJSONObject("data");
                JSONArray order_details_array = dataObject.getJSONArray("products");
                JSONArray payment_methods_array = dataObject.getJSONArray("payment_methods");
               // JSONArray customer_details_array = dataObject.getJSONArray("customers");
                String customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");

                int cust_id = Integer.valueOf(((offline_order_activity)getActivity()).current_customer_id);

                for (int p=0;p<payment_methods_array.length();p++)
                {
                    payment_methods.add(payment_methods_array.getString(p));
                }
                ArrayAdapter myAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,payment_methods);
                payment_method_view.setAdapter(myAdapter);
                payment_method_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selected_payment_method = payment_methods.get(i);
                        cust_billing_info.setText("Name: " + current_customer_name+ "\n" + "Payment by: " + selected_payment_method);
                    }
                });
                if (!customer_data.equals("abc")) {
                    JSONObject myObj = new JSONObject(customer_data);
                    JSONArray customer_details_array =  myObj.getJSONArray("data");
                    for (int c = 0; c < customer_details_array.length(); c++) {
                        JSONObject custObj = customer_details_array.getJSONObject(c);
                        if (custObj.getString("name").equals( ((offline_order_activity)getActivity()).current_customer_name) && custObj.getString("address").equals( ((offline_order_activity)getActivity()).current_customer_address) ) {
                            current_customer_name = custObj.getString("name");
                            current_customer_address = ((offline_order_activity) getActivity()).current_customer_address;
                            cust_billing_info.setText("Name: " + current_customer_name);
                        }
                    }
                }

                int x=0;
                for (int i = 0; i < order_details_array.length(); i++) {
                    if (qntyarray[i]!=0) {
                        Log.d(TAG, "yayyyyyyyyyyyyyyyyyyyyyyyyy");
                        JSONObject myObject = order_details_array.getJSONObject(i);
                        prod_no.add(String.valueOf(x + 1));
                        product_id.add(myObject.getString("id"));
                        prod_descp.add(myObject.getString("name"));
                        prod_buying.add(myObject.getString("buying_price"));
                        prod_qnty_array.add(myObject.getString("in_stock_quantity"));
                        prod_selling.add(myObject.getString("selling_price"));
                        prod_batch_list.add(myObject.getString("batch_no"));
                        prod_ctg_main_list_array.add(myObject.getJSONObject("category").getString("main"));
                        prod_ctg_sub_list_array.add(myObject.getJSONObject("category").getString("sub"));
                        prod_qua.add(String.valueOf(qntyarray[i]));
                        int subtotal = qntyarray[i] * Integer.valueOf(myObject.getString("selling_price"));
                        prod_total.add(String.valueOf(subtotal));
                    }
                }
               int subtot=0;
                for (int sub=0;sub<prod_total.size();sub++)
                {
                    subtot = subtot+ Integer.valueOf(prod_total.get(sub));
                }
                _subtotal.setText(String.valueOf(subtot));
                _discount.setText("0");
                _tax.setText("0");
                _grand_total.setText(_subtotal.getText().toString());
                _received_amount.setText("0");
                _remaining_amount.setText(_grand_total.getText().toString());
                // JSONArray json_order_details = dataObject.getJSONObject();
               Log.d(TAG, "Prod Sizeeeeee    " + String.valueOf(prod_no.size()));

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            customAdapter CustomAdapter = new customAdapter();
            Log.d(TAG, "Custom Array Adapter Created");
            myView.setAdapter(CustomAdapter);
*/
        }


    }


    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return prod_no.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_invoice_list_item,null);
            TextView number = (TextView) view.findViewById(R.id.product_no);
            TextView description = (TextView) view.findViewById(R.id.invoice_product_description);
            TextView selling = (TextView) view.findViewById(R.id.invoice_product_selling_price);
            // TextView buying = (TextView) view.findViewById(R.id.invoice_product_buying_price);
            TextView qnty = (TextView) view.findViewById(R.id.invoice_product_qnty);
            TextView total = (TextView) view.findViewById(R.id.invoice_product_total);
            number.setText(prod_no.get(i));
            description.setText(prod_descp.get(i));
            selling.setText(prod_selling.get(i));
            // buying.setText(prod_qnty_array.get(i));
            qnty.setText(prod_qua.get(i));
            total.setText(prod_total.get(i));

            return view;
        }
    }


}





