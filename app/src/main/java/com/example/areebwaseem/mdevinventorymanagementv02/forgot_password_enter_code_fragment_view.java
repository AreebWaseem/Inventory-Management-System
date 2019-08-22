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
import android.webkit.JsResult;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class forgot_password_enter_code_fragment_view extends Fragment implements  LoginPostRequest.OnPostComplete{
    private static final String TAG = "forgot_password_enter_c";
    GetRawData getRawData;
    ListView myView;
    LoginPostRequest myRequest;
    SharedPreferences mySharedPreferences;
    ProgressBar circular_progress;
    String access_token="";
    String customer_id;
    Context context;
    Button add_button;
    EditText user_code_add;
    String email_reseted="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.enter_code_for_forgot_password_layout,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.forgot_password_enter_code_progress_bar);
        circular_progress.setVisibility(View.INVISIBLE);
        context=this.getActivity();
        add_button = (Button) view.findViewById(R.id.enter_code_forgot_password_button);

        user_code_add = (EditText) view.findViewById(R.id.enter_code_email_forgot_password_text_view);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_code_add.getText().toString().equals("")  )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cant_string), Toast.LENGTH_SHORT).show();
                }
                else {
                    email_reseted=  ((Forgot_password_Activity_final)getActivity()).email_to_reset;
                    myRequest = new LoginPostRequest(forgot_password_enter_code_fragment_view.this, getActivity());
                    circular_progress.setVisibility(View.VISIBLE);
                    myRequest.execute("http://cloud.mdevsolutions.com/inventory/develop/api/auth/enter_reset_code",email_reseted, user_code_add.getText().toString());
                }

            }
        });


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
        ((Forgot_password_Activity_final)getActivity()).setActionBarStatus("enter_code_fragment");

    }



    @Override
    public void onPostComplete(String s, PostStatus postStatus) {
        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
            if (s != null) {
                try {
                    JSONObject myObject = new JSONObject(s);
                    ((Forgot_password_Activity_final)getActivity()).user_id_returned= myObject.getJSONObject("data").getString("user_id");
                    ((Forgot_password_Activity_final)getActivity()).setPasswordFrag();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(),  "Valid Code!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showErrorPosting(String exception, PostStatus status, boolean connection) {


        if (getActivity()!=null) {
            circular_progress.setVisibility(View.INVISIBLE);
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


/*
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
            view = getLayoutInflater().inflate(R.layout.custom_product_list_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.custom_imageView);
            TextView customerName = (TextView) view.findViewById(R.id.name_list);
            TextView customerID = (TextView) view.findViewById(R.id.product_list);
            TextView customer_address= (TextView) view.findViewById(R.id.cost_list);
            customerName.setText("Name: " + customerNameList.get(i));
            customerID.setText("ID: " + customerIDList.get(i));
            customer_address.setText("Address: " + customerAddressList.get(i));
            imageView.setImageResource(R.drawable.customer_stock_flat);
            return view;
        }
    }
    */
}
