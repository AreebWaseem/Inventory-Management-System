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
import android.widget.BaseAdapter;
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

public class orders_fragment_view_detail extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "orders_fragment_view_de";
    GetRawData getRawData;
    ListView myView;
    String invoice_no="";
    SharedPreferences mySharedPreferences;
    ArrayList<String> prod_no;
    ArrayList<String> prod_descp;
    ArrayList<String> prod_selling;
    ArrayList<String> prod_buying;
    ArrayList<String> prod_qua;
    ArrayList<String> prod_total;
    ProgressBar circular_progress;
    String url_detail="";
    String access_token="";
    String sub_Total="";
    String tax="";
    String grand_Total="";
    String received_Amount="";
    String remaining_Amount="";
    TextView order_no_Top;
    TextView order_payment_method;
    TextView cust_billing_info;
    boolean downloadStatus=false;
    TextView _subtotal;
    TextView _discount;
    TextView _tax;
    TextView _grand_total;
    TextView _received_amount;
   // TextView _remaining_amount;
    TextView lowest;
    TextView second_top;
    String current_order_id;
    int current_index=0;
    boolean isDownloading = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_detail_fragment_view,container,false);
        myView = (ListView) view.findViewById(R.id.order_details_list_view);
        order_no_Top = (TextView) view.findViewById(R.id.order_number_text_view_payment_payment);
        lowest = (TextView) view.findViewById(R.id.lowestViewCommentsOrder);
        order_payment_method= (TextView) view.findViewById(R.id.order_date_method_text_view);
        cust_billing_info = (TextView) view.findViewById(R.id.customer_order_billing_info_detail_text_view);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_order_detail_view);
        _discount = (TextView) view.findViewById(R.id.order_discount_text_view);
        _subtotal= (TextView) view.findViewById(R.id.order_subtotal_text_view);
        _tax = (TextView) view.findViewById(R.id.order_tax_text_view);
        _grand_total = (TextView) view.findViewById(R.id.order_grand_total_text_view);
        _received_amount = (TextView) view.findViewById(R.id.order_received_amount_text_view);
       // _remaining_amount = (TextView) view.findViewById(R.id.order_remaining_amount_text_view);
      //  second_top = (TextView) view.findViewById(R.id.order_date_method_text_view);
        prod_no = new ArrayList<String>();
        prod_descp = new ArrayList<String>();
        prod_selling = new ArrayList<String>();
        prod_buying = new ArrayList<String>();
        prod_qua = new ArrayList<String>();
        prod_total = new ArrayList<String>();
        downloadStatus=false;
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        circular_progress.setVisibility(View.VISIBLE);
        current_order_id=((Order_activity_final)getActivity()).order_current;
        isDownloading=true;
        url_detail="http://cloud.mdevsolutions.com/inventory/develop/api/order/view_order/"+current_order_id;
        getRawData.execute(url_detail,access_token);
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
            ((Order_activity_final) getActivity()).setItemChecked("orders_detail");
            if (isDownloading == false) {
                isDownloading = true;
                downloadStatus = false;
                getRawData = new GetRawData(this, getActivity());
                circular_progress.setVisibility(View.VISIBLE);
                current_order_id = ((Order_activity_final) getActivity()).order_current;
                url_detail = "http://cloud.mdevsolutions.com/inventory/develop/api/order/view_order/" + current_order_id;
                getRawData.execute(url_detail, access_token);
            }
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
        }
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Log.d(TAG, data);
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            isDownloading = false;

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONObject dataObject = jsonData.getJSONObject("data");
                JSONObject json_order_info = dataObject.getJSONObject("order_info");
                order_no_Top.setText(getResources().getString(R.string.order_no)+": " + json_order_info.getString("order_no"));
                order_payment_method.setText(getResources().getString(R.string.date_string)+": " + json_order_info.getString("order_date") + "\n" + getResources().getString(R.string.payment_method_string)+": " + json_order_info.getString("payment_method"));
               // cust_billing_info.setText("Name: " + json_order_info.getString("customer_name") + "\n" + "Email " + json_order_info.getString("customer_email") + "\n" + "Contact: " + json_order_info.getString("customer_phone") + "\n" + "Customer Address: " + json_order_info.getString("customer_address") + "\n" + "Shipping Address: " + json_order_info.getString("shipping_address"));
                cust_billing_info.setText( getResources().getString(R.string.name_string)+": " + ((Order_activity_final)getActivity()).nm + "\n" + getResources().getString(R.string.email_string)+": " +  ((Order_activity_final)getActivity()).ad +"\n"+ getResources().getString(R.string.address_string)+": " + ((Order_activity_final)getActivity()).ad);
                JSONArray order_details_array = dataObject.getJSONArray("order_details");
                for (int i = 0; i < order_details_array.length(); i++) {
                    JSONObject myObject = order_details_array.getJSONObject(i);
                    prod_no.add(String.valueOf(i + 1));
                    prod_descp.add(myObject.getString("product_name"));
                    prod_selling.add(myObject.getString("selling_price"));
                 //   prod_buying.add(myObject.getString("buying_price"));
                    prod_qua.add(myObject.getString("product_quantity"));
                    prod_total.add(myObject.getString("sub_total"));
                }
                _subtotal.setText(json_order_info.getString("sub_total"));
                _discount.setText(json_order_info.getString("discount_amount"));
                _tax.setText(json_order_info.getString("total_tax"));
                _grand_total.setText(json_order_info.getString("grand_total"));
                _received_amount.setText(json_order_info.getString("received_amount"));
              //  _remaining_amount.setText(json_order_info.getString("remaining_amount"));
                // JSONArray json_order_details = dataObject.getJSONObject();
                lowest.setText(getResources().getString(R.string.note_string)+": " + json_order_info.getString("note"));


            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
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

    }

    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
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
          //  buying.setText(prod_buying.get(i));
            qnty.setText(prod_qua.get(i));
            total.setText(prod_total.get(i));

            return view;
        }
    }
}
