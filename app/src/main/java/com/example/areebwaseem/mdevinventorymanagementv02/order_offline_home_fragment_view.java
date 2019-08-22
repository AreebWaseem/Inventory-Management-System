package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by areebwaseem on 10/28/17.
 */

public class order_offline_home_fragment_view extends Fragment {
    private static final String TAG = "profile_fragment_view";
 Button sync_button;
 Button exit_button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offline_activity_home_fragment,container,false);
        sync_button = (Button) view.findViewById(R.id.sync_button);
        exit_button = (Button) view.findViewById(R.id.exit_offline_button);
        sync_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),BackgroundService.class);
                getActivity().startService(i);
            }
        });
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((offline_order_activity)getActivity()).exitSelf();
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
            ((offline_order_activity) getActivity()).setItemChecked("order_offline_home");

        }
        //INSERT CUSTOM CODE HERE
    }

}
