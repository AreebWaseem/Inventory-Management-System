package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class return_products_to_inventory_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "return_products_to_inve";
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_return_products_list_view,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_return_product_to_inventory);
        productNameList = new ArrayList<String>();
        productBatchList = new ArrayList<String>();
        productqntyList = new ArrayList<String>();
        productIDList = new ArrayList<String>();

        myView = (ListView) view.findViewById(R.id.return_products_list_view);
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
            ((NavBarSample) getActivity()).setItemChecked("return_to_inventory");
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
                    String batch_num = currentObject.getString("batch_no");
                    String productName = currentObject.getString("product_name");
                    String productqnty = currentObject.getString("remaining_quantity");
                    productBatchList.add(batch_num);
                    productNameList.add(productName);
                    productqntyList.add(productqnty);
                    productIDList.add(currentObject.getString("id"));


                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            //Log.d(TAG, String.valueOf(productStockCodeList.size()));
            customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            //    ((NavBarSample)getActivity()).setViewPager("returned_products_fragment");
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    ((NavBarSample)getActivity()).setupViewPagerReturnInventoryDetail(productIDList.get(i));
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
*/

public class return_products_to_inventory_fragment_view extends Fragment implements GetRawData.OnDownloadComplete, LoginPostRequest.OnPostComplete{
    private static final String TAG = "return_products_to_inve";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> order_product_name_list;
    ArrayList<String> product_id_list;
    LoginPostRequest myRequest;
    // ProgressBar circular_progress;
    Button next_button;
    String access_token="";
    Button proceed_Button;
    String downData="";
    Boolean[] array_checked;
    int[] qntyArray;
    ArrayList<String> qntyCount;
    ArrayList<String> nameandbatch;
    ArrayList<String> requested_products_selected_batch_list;
    SearchableSpinner product_Spinner;
    ArrayList<String> batch_list;
    ArrayList<String> request_products_selected;
    ArrayList<String> requested_products_selected_id_list;
    ArrayList<String> remaining_quantity_list;
    ArrayList<String> requested_remaining_quantity_list;
    boolean isDownloading=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.request_product_list_updated,container,false);
        // circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_custom_order_product_select);
        order_product_name_list = new ArrayList<String>();
        product_id_list= new ArrayList<String>();
        request_products_selected = new ArrayList<String>();
        requested_products_selected_id_list= new ArrayList<String>();
       // order_product_name_list.add("Select Products");
        qntyCount = new ArrayList<String>();
        batch_list = new ArrayList<String>();
        nameandbatch= new ArrayList<String>();
        remaining_quantity_list = new ArrayList<String>();
        requested_remaining_quantity_list= new ArrayList<String>();
        requested_products_selected_batch_list= new ArrayList<String>();
        nameandbatch.add("Select Products");
        /*
         next_button = (Button) view.findViewById(R.id.order_product_next_button);
         next_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 boolean myCheck = true;
                 int second_check = 0;

                 if (qntyArray != null && array_checked != null) {
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
                         circular_progress.setVisibility(View.VISIBLE);
                         JSONObject myObject = new JSONObject();
                         try {


                             JSONArray myArray = new JSONArray();
                             for (int i = 0; i < qntyArray.length; i++) {
                                 JSONObject currentObj = new JSONObject();
                                 if (qntyArray[i] != 0) {
                                     currentObj.put("id", Integer.valueOf(product_id_list.get(i)));
                                     currentObj.put("ordered_quantity", Integer.valueOf(qntyArray[i]));
                                     myArray.put(currentObj);
                                 }
                             }
                             myObject.put("products", myArray);
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         if (String.valueOf(myObject)!=null)
                         {
                             myRequest = new LoginPostRequest(request_product_create_fragment_view.this, getActivity());
                             myRequest.execute("http://cloud.mdevsolutions.com/inventory/api/api/requested_products/save", access_token,String.valueOf(myObject));

                         }
                     } else {
                         Toast.makeText(getActivity(), "Invalid Selection or Quantity!", Toast.LENGTH_SHORT).show();
                     }
                 }

                      else {
                         Toast.makeText(getActivity(), "No products!", Toast.LENGTH_SHORT).show();
                     }
                 }

         });
         */
        proceed_Button = (Button) view.findViewById(R.id.request_product_proceed);
        proceed_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean qntyCheck=true;
                //  Log.d(TAG, "Total Length of qntyCount:" + String.valueOf(qntyCount.size()));
                //  Log.d(TAG, "Total length of Request Name list: " + String.valueOf(request_products_selected.size()));
                //  Log.d(TAG, "Total length of items in ID list" + String.valueOf(requested_products_selected_id_list.size()));
                if (qntyCount.size()==0)
                {
                    qntyCheck=false;
                }
                for (int i=0;i<qntyCount.size();i++)
                {
                    if (qntyCount.get(i).equals("0"))
                    {
                        qntyCheck=false;
                    }
                    Log.d(TAG, qntyCount.get(i));
                    Log.d(TAG, request_products_selected.get(i));
                    Log.d(TAG, requested_products_selected_id_list.get(i) );
                }
                if (qntyCheck==true)
                {
                    JSONObject myObjet = new JSONObject();
                    JSONArray podArray = new JSONArray();
                    for (int z=0;z<qntyCount.size();z++)
                    {
                        JSONObject myObject = new JSONObject();
                        try {
                            myObject.put("id", Integer.valueOf(requested_products_selected_id_list.get(z)));
                            myObject.put("quantity", Integer.valueOf(qntyCount.get(z)));
                            podArray.put(myObject);
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                        myRequest = new LoginPostRequest(return_products_to_inventory_fragment_view.this, getActivity());
                         myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/save", access_token, String.valueOf(podArray));
                        Log.d(TAG, String.valueOf(podArray));
                }
                else {
                    Toast.makeText(getActivity(), "Invalid Quantity Selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        product_Spinner= (SearchableSpinner) view.findViewById(R.id.requested_products_spinner);
        myView = (ListView) view.findViewById(R.id.custom_request_product_list_view);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        getRawData = new GetRawData(this,getActivity());
        //circular_progress.setVisibility(View.VISIBLE);
        getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/create?limit=10000",access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("return_to_inventory");

            if (isDownloading==false)
            {
if (order_product_name_list!=null)
{
    order_product_name_list.clear();
}
if (product_id_list!=null)
{
    product_id_list.clear();
}
if (request_products_selected!=null)
{
    request_products_selected.clear();
}
if (requested_products_selected_id_list!=null)
{
    requested_products_selected_id_list.clear();
}
if (qntyCount!=null)
{
    qntyCount.clear();
}
if (batch_list!=null)
{
    batch_list.clear();
}
if (nameandbatch!=null)
{
    nameandbatch.clear();
    nameandbatch.add("Select Products");
}
if (remaining_quantity_list!=null)
{
    remaining_quantity_list.clear();
}
if (requested_remaining_quantity_list!=null)
{
    requested_remaining_quantity_list.clear();
}
if (requested_products_selected_batch_list!=null)
{
    requested_products_selected_batch_list.clear();
}
                getRawData = new GetRawData(this,getActivity());
                //circular_progress.setVisibility(View.VISIBLE);
                getRawData.execute("http://cloud.mdevsolutions.com/inventory/develop/api/returned_products/create?limit=10000",access_token);
                isDownloading=true;

            }



        }


        //INSERT CUSTOM CODE HERE
    }


    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (getActivity()!=null) {
            // circular_progress.setVisibility(View.INVISIBLE);
            isDownloading=false;
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
                downData=data;
                JSONObject jsonData = new JSONObject(myString.toString());
                JSONArray itemsArray = jsonData.getJSONArray("data");
                array_checked = new Boolean[itemsArray.length()];
                qntyArray = new int[itemsArray.length()];
                for (int i=0; i<array_checked.length;i++)
                {
                    array_checked[i] = false;
                    qntyArray[i]=0;
                }
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentObject = itemsArray.getJSONObject(i);

                    order_product_name_list.add(currentObject.getString("product_name"));
                    batch_list.add(currentObject.getString("batch_no"));
                    product_id_list.add(currentObject.getString("id"));
                    remaining_quantity_list.add(currentObject.getString("remaining_quantity"));
                    nameandbatch.add(currentObject.getString("product_name")+ " [" + currentObject.getString("batch_no") +"]");



                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            Log.d(TAG, "GOT DATAAAAAAAAAAAAAAAAAA");
            // Log.d(TAG, String.valueOf(productStockCodeList.size()));

            final customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            final ArrayAdapter<String> myadapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,nameandbatch);
            product_Spinner.setAdapter(myadapter);

            product_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i!=0) {
                         boolean batch_check=false;
                       for (int x=0;x<requested_products_selected_batch_list.size();x++)
                       {
                           if (requested_products_selected_batch_list.get(x).equals(batch_list.get(i-1)))
                           {
                               batch_check=true;
                           }
                       }
                       if (batch_check==false) {
                           request_products_selected.add(order_product_name_list.get(i - 1));
                           requested_products_selected_id_list.add(product_id_list.get(i - 1));
                           requested_products_selected_batch_list.add(batch_list.get(i - 1));
                           requested_remaining_quantity_list.add(remaining_quantity_list.get(i-1));
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
                           Toast.makeText(getActivity(), "Product already added!", Toast.LENGTH_SHORT).show();
                       }
                       /*

                       View listItem = CustomAdapter.getView(i, null, myView);

                       ViewGroup.LayoutParams params = myView.getLayoutParams();
                       params.height = params.height + listItem.getMeasuredHeight() + myView.getDividerHeight();
                       myView.setLayoutParams(params);
                       myView.requestLayout();
                       */
                    }
                    product_Spinner.setSelection(0);


                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d(TAG, "whats up");
                }
            });
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

                   if (view instanceof EditText)
                   {
                       EditText myText = view.findViewById(R.id.order_custom_qnty_edit_text_request);
                       Log.d(TAG, "focusable to true");
                       myText.setFocusable(true);
                   }
                   else
                   {
                       EditText myText = view.findViewById(R.id.order_custom_qnty_edit_text_request);
                       myText.setFocusable(false);
                       Log.d(TAG,"On touch");
                   }
                   EditText seText = (EditText) view.findViewById(R.id.order_custom_qnty_edit_text_request);

                   if (!seText.getText().toString().equals("")) {
                       qntyArray[i] = Integer.valueOf(seText.getText().toString());
                   }
                   else {
                       qntyArray[i]=0;
                   }
                   Log.d(TAG, "Item Got Clicked");
                  CheckBox myBox = (CheckBox) view.findViewById(R.id.item_switch_request);
                  if (array_checked[i]==false) {
                      array_checked[i] = true;
                  }
                  else {
                      array_checked[i]=false;
                  }
                  myBox.setChecked(array_checked[i]);
               }
           });
           /*
           myView.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View view, MotionEvent motionEvent) {
                   Log.d(TAG, "Toucheddddddddd.........");

                   return false;
               }
           });
          //
*/
    }




    @Override
    public void downloadErrorPosting(String exception, DownloadStatus status, boolean connection) {
        if (getActivity()!=null) {
            isDownloading=false;
            //circular_progress.setVisibility(View.INVISIBLE);
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

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
            //  circular_progress.setVisibility(View.INVISIBLE);
            if (s != null) {
                Toast.makeText(getActivity(), "Successful!", Toast.LENGTH_SHORT).show();
                ((NavBarSample)getActivity()).setUpreturnedProductsFragmen();
            }
        }
    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean connection) {
        if (getActivity()!=null) {
            //  circular_progress.setVisibility(View.INVISIBLE);
            if (exception.equals("comm_error"))
            {
                Toast.makeText(getActivity(), "Error Communicating with server!", Toast.LENGTH_SHORT).show();
            }
            else if (!connection) {
                Toast.makeText(getActivity(), "No internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if (exception!=null && !exception.equals("No_internet_connection" )) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Something Went Wrong, Try Again!", Toast.LENGTH_SHORT).show();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_request_and_return_list_item,null);
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
            final TextView id_view = (TextView) view.findViewById(R.id.request_and_return_product_list_id_tracker_text_view);
            ImageButton myButton = (ImageButton) view.findViewById(R.id.request_product_custom_list_item_image_button);
           final EditText qntyText = (EditText) view.findViewById(R.id.request_product_custom_list_item_edit_text);
            TextView product_name_view = (TextView) view.findViewById(R.id.request_product_custom_list_item_text_view);
            id_view.setText(String.valueOf(i));
            if (i<request_products_selected.size()) {
                product_name_view.setText(request_products_selected.get(i) + " [" + requested_products_selected_batch_list.get(i)+"]" );
            }

            qntyText.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    /*
                    if (!editable.toString().equals(""))
                    {
                        qntyCount.set((Integer.valueOf(id_view.getText().toString())),editable.toString());
                    }
                    */

                        if (!editable.toString().equals(""))
                        {
                            if (!(Integer.valueOf(editable.toString())>Integer.valueOf(requested_remaining_quantity_list.get(Integer.valueOf(id_view.getText().toString())))))
                            {
                                qntyCount.set((Integer.valueOf(id_view.getText().toString())),editable.toString());
                            }
                            else {
                                Toast.makeText(getActivity(), "Quantity can't be greater than in stock: " + requested_remaining_quantity_list.get(Integer.valueOf(id_view.getText().toString())), Toast.LENGTH_SHORT).show();
                                qntyText.setText("");
                            }
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
                        requested_products_selected_batch_list.remove(requested_products_selected_batch_list.get(Integer.valueOf(id_view.getText().toString())));
                        requested_remaining_quantity_list.remove(requested_remaining_quantity_list.get(Integer.valueOf(id_view.getText().toString())));
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
}
