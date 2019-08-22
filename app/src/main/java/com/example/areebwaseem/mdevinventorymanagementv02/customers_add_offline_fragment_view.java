package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class customers_add_offline_fragment_view extends Fragment{
    private static final String TAG = "customers_add_offline_f";
    ListView myView;
    SharedPreferences mySharedPreferences;
    ProgressBar circular_progress;
    String access_token="";
    String customer_id;
    Context context;
    Button add_button;
    EditText cust_name_add;
    EditText cust_phone_add;
    EditText cust_address_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_add_fragment,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.progressBar_customer_add);
        circular_progress.setVisibility(View.INVISIBLE);
        context=this.getActivity();
        add_button = (Button) view.findViewById(R.id.customer_add_button);
        cust_address_add = (EditText) view.findViewById(R.id.customer_add_address);
        cust_phone_add = (EditText) view.findViewById(R.id.customer_phone_add);
        cust_name_add = (EditText) view.findViewById(R.id.customer_add_name);
        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cust_address_add.getText().toString().equals("") || cust_name_add.getText().toString().equals("") ||cust_phone_add.getText().toString().equals("") )
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cant_string),Toast.LENGTH_SHORT).show();
                }
                else {
                    if (cust_phone_add.getText().toString().length()==10) {
                        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
                        String customer_data = mySharedPreferences.getString("order_create_customer_list", "abc");
                        try {
                            JSONObject myObject = new JSONObject(customer_data);
                            JSONObject new_cust_object = new JSONObject();
                            new_cust_object.put("id", -1);
                            new_cust_object.put("name", cust_name_add.getText().toString());
                            new_cust_object.put("phone", cust_phone_add.getText().toString());
                            new_cust_object.put("address", cust_address_add.getText().toString());
                            JSONArray myArray = myObject.getJSONArray("data");
                            myArray.put(new_cust_object);
                            JSONObject final_object = new JSONObject();
                            final_object.put("data", myArray);
                            mySharedPreferences.edit().putString("order_create_customer_list", String.valueOf(final_object)).apply();
                            Log.d(TAG, String.valueOf(final_object));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), getResources().getString(R.string.successful_string), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Number must be 10 digits long!",Toast.LENGTH_SHORT).show();
                    }
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
        if (getActivity()!=null) {

            ((offline_order_activity) getActivity()).setItemChecked("addcustomers_offline");
        }
    }

}
