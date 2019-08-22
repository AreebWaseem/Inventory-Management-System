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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class return_product_create_fragment_view extends Fragment implements GetRawData.OnDownloadComplete, LoginPostRequest.OnPostComplete{
    private static final String TAG = "return_product_create_f";
    GetRawData getRawData;
    ListView myView;
    SharedPreferences mySharedPreferences;
    ArrayList<String> order_product_name_list;
    ArrayList<String> product_id_list;
    ArrayList<String> remaining_quantity_list;
    ArrayList<String> requested_remaining_quantity_list;
    LoginPostRequest myRequest;
   // ProgressBar circular_progress;
    Button next_button;
    String access_token="";
    Button proceed_Button;
    String downData="";
    Boolean[] array_checked;
    int[] qntyArray;
    ArrayList<String> qntyCount;
    SearchableSpinner product_Spinner;
    ArrayList<String> request_products_selected;
    ArrayList<String> requested_products_selected_id_list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.request_product_list_updated,container,false);
       // circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_custom_order_product_select);
        order_product_name_list = new ArrayList<String>();
        product_id_list= new ArrayList<String>();
        request_products_selected = new ArrayList<String>();
        requested_products_selected_id_list= new ArrayList<String>();
        remaining_quantity_list= new ArrayList<String>();
        requested_remaining_quantity_list= new ArrayList<String>();
       // order_product_name_list.add("Select Products");
        order_product_name_list.add(getResources().getString(R.string.spin_select_products_str_resource));

        qntyCount = new ArrayList<String>();
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
                         myObject.put("ordered_quantity", Integer.valueOf(qntyCount.get(z)));
                         podArray.put(myObject);
                     }catch (JSONException e)
                     {
                         e.printStackTrace();
                     }
                 }
                 try {
                     myObjet.put("products", podArray);
                     myRequest = new LoginPostRequest(return_product_create_fragment_view.this, getActivity());
                     myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/requested_products/save", access_token,String.valueOf(myObjet));
                     Log.d(TAG, String.valueOf(myObjet));
                 }catch (JSONException e)
                 {
                     e.printStackTrace();
                 }

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
           // circular_progress.setVisibility(View.INVISIBLE);
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
                    product_id_list.add(currentObject.getString("id"));
                    remaining_quantity_list.add(currentObject.getString("remaining_quantity"));


                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, data);
            Log.d(TAG, "GOT DATAAAAAAAAAAAAAAAAAA");
            // Log.d(TAG, String.valueOf(productStockCodeList.size()));

           final customAdapter CustomAdapter = new customAdapter();
            myView.setAdapter(CustomAdapter);
            final ArrayAdapter<String> myadapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,order_product_name_list);
            product_Spinner.setAdapter(myadapter);

            product_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i!=0) {

                        request_products_selected.add(order_product_name_list.get(i));
                        requested_products_selected_id_list.add(product_id_list.get(i-1));
                        qntyCount.add("0");
                        CustomAdapter.notifyDataSetChanged();
                         Log.d(TAG, product_id_list.get(i-1));
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
            //circular_progress.setVisibility(View.INVISIBLE);
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

    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
          //  circular_progress.setVisibility(View.INVISIBLE);
            if (s != null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.successful_string), Toast.LENGTH_SHORT).show();
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
            final TextView id_view = (TextView) view.findViewById(R.id.request_and_return_product_list_id_tracker_text_view);
            ImageButton myButton = (ImageButton) view.findViewById(R.id.request_product_custom_list_item_image_button);
            EditText qntyText = (EditText) view.findViewById(R.id.request_product_custom_list_item_edit_text);
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
}


