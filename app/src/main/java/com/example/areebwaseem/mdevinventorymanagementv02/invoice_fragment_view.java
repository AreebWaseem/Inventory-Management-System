package com.example.areebwaseem.mdevinventorymanagementv02;

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

public class invoice_fragment_view extends Fragment {
    private static final String TAG = "profile_fragment_view";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products,container,false);
        return view;
    }
}
