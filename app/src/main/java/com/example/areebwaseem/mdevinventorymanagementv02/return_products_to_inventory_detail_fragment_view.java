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
import android.widget.Button;
import android.widget.EditText;
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

public class return_products_to_inventory_detail_fragment_view extends Fragment implements GetRawData.OnDownloadComplete,LoginPostRequest.OnPostComplete{
    private static final String TAG = "return_products_to_inve_det";
    GetRawData getRawData;
    ListView myView;
    int myInt=0;
    SharedPreferences mySharedPreferences;
    ArrayList<String> productNameList;
    ArrayList<String> productBatchList;
    ArrayList<String> productqntyList;
    ArrayList<String> productIDList;
    Button myButton;
    ProgressBar circular_progress;
    String access_token="";
    TextView NameView;
    TextView RestView;
    Button ret_Button;
    EditText qntyEditText;
    String final_id="";
    String final_quantity="";
    boolean isDownloading=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.return_product_detail,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_return_product_to_inventory_detail);
        productNameList = new ArrayList<String>();
        productBatchList = new ArrayList<String>();
        productqntyList = new ArrayList<String>();
        productIDList = new ArrayList<String>();
         NameView = (TextView) view.findViewById(R.id.return_product_name_detail_text);
         RestView = (TextView) view.findViewById(R.id.return_product_detail_view_text_view);
         ret_Button = (Button) view.findViewById(R.id.product_return_button);
         qntyEditText = (EditText) view.findViewById(R.id.editText_return_detail_qnty);
         ret_Button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (qntyEditText.getText().toString().equals("") )
                 {
                     ( (NavBarSample)getActivity()).makeToast("Set Quantity!", true);
                 }
                 else if (Integer.valueOf(qntyEditText.getText().toString())>Integer.valueOf(final_quantity))
                 {
                     ( (NavBarSample)getActivity()).makeToast("Quantity can't be greater than no. of Products!", true);
                 }
                 else
                 {
                    LoginPostRequest myRequest = new LoginPostRequest(return_products_to_inventory_detail_fragment_view.this, getActivity());
                     circular_progress.setVisibility(View.VISIBLE);
                     myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/save", access_token, final_id, qntyEditText.getText().toString() );
                 }
             }
         });
         isDownloading=true;
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        circular_progress.setVisibility(View.VISIBLE);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/create?limit=10000",access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("return_product_detail");
            if (isDownloading == false) {
                isDownloading = true;
                getRawData = new GetRawData(this, getActivity());
                circular_progress.setVisibility(View.VISIBLE);
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/create?limit=10000", access_token);
            }
        }

        //INSERT CUSTOM CODE HERE
    }


    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        isDownloading=false;
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("data");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);
                    if (currentObject.getString("id").equals(((NavBarSample)getActivity()).return_product_detail_id)) {
                        final_id = currentObject.getString("product_id");
                        final_quantity = currentObject.getString("remaining_quantity");
                        NameView.setText("Product Name: " + currentObject.getString("product_name"));
                        RestView.setText("Code: " + currentObject.getString("product_code") + "\n" + "Batch No: " + currentObject.getString("batch_no") + "\n" + "Buying Price: " + currentObject.getString("buying_price") + "\n"+ "Selling Price: " + currentObject.getString("selling_price") + "\n" + "Remaining Quantity: " + currentObject.getString("remaining_quantity") + "\n"+ "Category: " + currentObject.getString("category_name")+ "\n" +"Subcategory: " + currentObject.getString("subcategory_name") );

                    }


                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            //Log.d(TAG, String.valueOf(productStockCodeList.size()));
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
                Toast.makeText(getActivity(), "No products have been returned by you!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            if (s != null) {
                Log.d(TAG, s);
                ((NavBarSample) getActivity()).makeToast("Product Successfully Returned!", true);
            }
        }
    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean connection) {
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
            view = getLayoutInflater().inflate(R.layout.custom_return_product_list_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView111);
            TextView nameTextView = (TextView) view.findViewById(R.id.return_product_product_name);
            TextView batchview = (TextView) view.findViewById(R.id.return_product_batch_no);
            TextView qntyView = (TextView) view.findViewById(R.id.return_product_remaining_quantity);




           // batchview.setText("Batch NO: " + productBatchList.get(i));
           // nameTextView.setText("Name: " + productNameList.get(i));
           // qntyView.setText("Remaining Quantity: " + productqntyList.get(i));
             batchview.setText( "Batch No: " + productBatchList.get(i));
             nameTextView.setText("Name: " + productNameList.get(i));
             qntyView.setText("Quantity: " + productqntyList.get(i));
            imageView.setImageResource(R.drawable.desktop_pic_flat);
            return view;
        }
    }
}
