package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by areebwaseem on 11/1/17.
 */

public class internet_connectivity_check {
        private Context context;

        public internet_connectivity_check(Context context) {
            this.context = context;
        }

        public boolean isNetworkAvailable() {
           ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
}
