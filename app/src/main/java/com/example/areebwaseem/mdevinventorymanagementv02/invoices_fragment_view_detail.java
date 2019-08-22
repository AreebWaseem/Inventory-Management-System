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

public class invoices_fragment_view_detail extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "invoices_fragment_view_";
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
    TextView invoice_no_Top;
    TextView invoice_payment_method;
    TextView cust_billing_info;
    boolean downloadStatus=false;
    TextView _subtotal;
    TextView _tax;
    TextView _grand_total;
    TextView _received_amount;
    //TextView _remaining_amount;
    TextView second_top;
    String invo_number;
    int current_index=0;
    boolean isDownloading = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.invoice_detail_fragment_view,container,false);
        myView = (ListView) view.findViewById(R.id.product_details);
        invoice_no_Top = (TextView) view.findViewById(R.id.Invoice_number_text_view);
        invoice_payment_method = (TextView) view.findViewById(R.id.invoice_date_method_text_view);
        cust_billing_info = (TextView) view.findViewById(R.id.customer_billing_info_detail_text_view);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_invoice_detail_view);
        _subtotal= (TextView) view.findViewById(R.id.invoice_subtotal_text_view);
        _tax = (TextView) view.findViewById(R.id.invoice_tax_text_view);
        _grand_total = (TextView) view.findViewById(R.id.invoice_grand_total_text_view);
        _received_amount = (TextView) view.findViewById(R.id.invoice_received_amount_text_view);
       // _remaining_amount = (TextView) view.findViewById(R.id.invoice_remaining_amount_text_view);
        second_top = (TextView) view.findViewById(R.id.invoice_date_method_text_view);
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
        invo_number=((NavBarSample)getActivity()).invoice_number;
        isDownloading=true;
        url_detail="http://cloud.mdevsolutions.com/inventory/develop/api/invoice/view/"+invo_number;
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
            ((NavBarSample) getActivity()).setItemChecked("invoices");
            if (isDownloading == false) {
                isDownloading = true;
                downloadStatus = false;
                getRawData = new GetRawData(this, getActivity());
                circular_progress.setVisibility(View.VISIBLE);
                invo_number = ((NavBarSample) getActivity()).invoice_number;
                url_detail = "http://cloud.mdevsolutions.com/inventory/develop/api/invoice/view/" + invo_number;
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
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            isDownloading = false;
            if (downloadStatus == false) {
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONObject dataObject = jsonData.getJSONObject("data");
                    String invoice_no = "";
                    String product_method = "";
                    String cust_name = "";
                    invoice_no = dataObject.getString("invoice_no");
                    cust_name = dataObject.getJSONObject("customer").getString("name");
                    product_method = dataObject.getString("paid_via");
                    invoice_no_Top.setText(getResources().getString(R.string.INVOICE_string)+": " + invoice_no);
                    if (!cust_name.equals("null")) {
                        cust_billing_info.setText(cust_name);
                    }
                    else {
                        cust_billing_info.setText("Walking Client");
                    }
                    JSONArray product_Array = dataObject.getJSONArray("products");
                    for (int i = 0; i < product_Array.length(); i++) {
                        JSONObject currentObject = product_Array.getJSONObject(i);
                        prod_no.add(String.valueOf(i + 1));
                        prod_descp.add(currentObject.getString("name"));
                        prod_selling.add(currentObject.getString("selling_price"));
                       // prod_buying.add(currentObject.getString("buying_price"));
                        prod_qua.add(currentObject.getString("quantity"));
                        prod_total.add(currentObject.getString("sub_total"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                if (getActivity() != null) {

                    getRawData = new GetRawData(this, getActivity());
                    circular_progress.setVisibility(View.VISIBLE);
                    Log.d(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    url_detail = "http://cloud.mdevsolutions.com/inventory/develop/api/order/manage_order?limit=1000";
                    getRawData.execute(url_detail, access_token);
                    downloadStatus = true;
                    isDownloading = true;
                }

            } else if (downloadStatus == true) {
                downloadStatus = false;
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
                    JSONObject jsonData_1 = new JSONObject(myString.toString());
                    JSONArray dataArray_1 = jsonData_1.getJSONArray("data");
                    String sbt = "";
                    String tx = "";
                    String gt = "";
                    String ra = "";
                    String rea = "";

                    for (int i = 0; i < dataArray_1.length(); i++) {
                        JSONObject currentObject = dataArray_1.getJSONObject(i);
                        if (currentObject.getString("invoice_no").equals(invo_number)) {
                            current_index = i;
                        }

                    }
                    JSONObject myObject = dataArray_1.getJSONObject(current_index);
                    sbt = myObject.getString("sub_total");
                    tx = myObject.getString("total_tax");
                    gt = myObject.getString("grand_total");
                    ra = myObject.getString("received_amount");
                   // rea = myObject.getString("remaining_amount");

                    _subtotal.setText(sbt);
                    _tax.setText(tx);
                    _grand_total.setText(gt);
                    _received_amount.setText(ra);
                  //  _remaining_amount.setText(rea);
                    second_top.setText(getResources().getString(R.string.invo_date_string)+": " + myObject.getString("invoice_date") + "\n" + getResources().getString(R.string.payment_method_string)+ ": " + myObject.getString("payment_method"));

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            Log.d(TAG, data);
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
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
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
           // buying.setText(prod_buying.get(i));
            qnty.setText(prod_qua.get(i));
            total.setText(prod_total.get(i));

            return view;
        }
    }
}
