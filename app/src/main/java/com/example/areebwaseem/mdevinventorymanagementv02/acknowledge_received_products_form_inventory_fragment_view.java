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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class acknowledge_received_products_form_inventory_fragment_view extends Fragment implements GetRawData.OnDownloadComplete{
    private static final String TAG = "acknowledge_received_pr";
    LoginPostRequest myRequest;
    SharedPreferences mySharedPreferences;
    ProgressBar circular_progress;
    String access_token="";
    Context context;
    Button req_button;
    EditText req_id;
    GetRawData getRawData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.acknowledge_receive_fragment_layout,container,false);
        circular_progress = (ProgressBar) view.findViewById(R.id.acknowledge_progressBar_request_products);
        circular_progress.setVisibility(View.INVISIBLE);
        context=this.getActivity();
          req_button = (Button) view.findViewById(R.id.acknowledge_received_product_button);
          req_id = (EditText) view.findViewById(R.id.acknowledge_received_product_id_text_view);

        mySharedPreferences = getActivity().getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);
        access_token = mySharedPreferences.getString("ACCESS_TOKEN", "abc");
        req_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (req_id.getText().toString().equals("") )
                {
                    ((NavBarSample)getActivity()).makeToast("Field can't be empty!", true);
                }
                else {
                    getRawData = new GetRawData(acknowledge_received_products_form_inventory_fragment_view.this, getActivity());
                    circular_progress.setVisibility(View.VISIBLE);
                    String url= "http://cloud.mdevsolutions.com/inventory/develop/api/requested_products/received/" + req_id.getText().toString();
                    getRawData.execute(url, access_token);
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
            ((NavBarSample) getActivity()).setItemChecked("acknowledge_products");
        }

    }



    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if (data!=null) {
            Log.d(TAG, data);
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
}
