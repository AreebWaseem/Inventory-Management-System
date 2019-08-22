package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

public class Forgot_password_Activity_final extends AppCompatActivity {
    private static final String TAG = "Forgot_password_Activit";
    MenuItem myItem;
    Menu menuNav;
    int current_frag_index=0;
    boolean home_bar_selected=false;
    boolean customer_bar_selected=false;
    boolean requested_bar_selected=false;
    boolean returned_bar_selected=false;
    public String email_to_reset="";
    ImageButton order_confirmation_button;
    SharedPreferences mySharedPreferences;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mainView;
    String invoice_number;
    BottomNavigationView bottomNavigationView;
    private sectionsStatePageAdapter adapter;
    public ViewPager viewPager;
    String currentFragment="";
    String stockCodeDetail="";
    ImageButton profile_view_request;
    public String em;
    public String ad;
    public String nm;
    ImageButton getOrder_confirmation_button;
    public String user_id_returned="";
    public String order_current;
    public String current_customer_id="";
    public String current_customer_address="";
    public String requested_products_detail_view_id="";
    public String return_product_detail_id="";
    public int myQuantity[];
    public void makeToast(String toast_text, boolean lengthy){
        if (lengthy) {
            Toast.makeText(this, toast_text, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, toast_text, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            if (adapter!=null)
            {
                if (adapter.mFragmentTitleList.size()>=1) {
                    if (current_frag_index>0) {
                        setViewPager(adapter.mFragmentTitleList.get(current_frag_index - 1));
                    }
                    else {
                        finish();
                    }
                }

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password__final);
        mySharedPreferences = getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);

        adapter = new sectionsStatePageAdapter(getSupportFragmentManager());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout_forgot_password);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.order_action_layout);
        order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
        order_confirmation_button.setVisibility(View.INVISIBLE);

        viewPager = (ViewPager) findViewById(R.id.fragmentContainer_forgot_password);
        setupViewPager(viewPager,"request_code_fragment");
    }
    public void setresetFragment()
    {
        setupViewPager(viewPager, "enter_code_fragment");
    }




    public void setupViewPager(ViewPager viewPager, String fragmentType) {
        boolean check = false;

        for (int i = 0; i < adapter.mFragmentTitleList.size(); i++) {
            if (adapter.mFragmentTitleList.get(i).equals(fragmentType)) {
                check = true;
            }
        }
        if (check == false) {
            Log.d(TAG, "Value was False");

            if (fragmentType.equals("request_code_fragment"))
            {
                adapter.addFragment(new forgot_password_request_code_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("enter_code_fragment"))
            {
                adapter.addFragment(new forgot_password_enter_code_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("new_password_fragment"))
            {
                adapter.addFragment(new forgot_password_new_password_fragment_view(),fragmentType);
            }


            // adapter.addFragment(new invoice_fragment_view(), "invoice view");
            if(viewPager.getAdapter()==null) {
                viewPager.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
                setViewPager(fragmentType);
            }
        }
        else if (check==true){
            setViewPager(fragmentType);
            Log.d(TAG, fragmentType);
        }
    }

    public void setViewPager(String fragmentType){
        int fragmentNumber=0;
        boolean check = false;
        for (int i=0;i<adapter.mFragmentTitleList.size();i++)
        {
            if (fragmentType.equals(adapter.mFragmentTitleList.get(i))){
                fragmentNumber=i;
                check=true;
            }
        }
        if (check==true) {
            viewPager.setCurrentItem(fragmentNumber);
            current_frag_index=fragmentNumber;
            setActionBarStatus(fragmentType);
            Log.d(TAG, "Pager Setttttttttt");
            Log.d(TAG,"Fragment Type "+ fragmentType);
            Log.d(TAG, String.valueOf(adapter.mFragmentTitleList.size()));
        }

    }

    public void setActionBarStatus(String title) {

        if (title.equals("request_code_fragment"))
        {
            setToolbarStatus("Enter Phone Number");
        }
        else if (title.equals("enter_code_fragment"))
        {
            setToolbarStatus("Enter Code");
        }
        else if (title.equals("new_password_fragment"))
        {
            setToolbarStatus("New Password");
        }




    }
public void setPasswordFrag()
{
    setupViewPager(viewPager, "new_password_fragment");
}
public void startfinish()
{
    finish();
}


    public void setToolbarStatus(String status){
        TextView myView = (TextView) findViewById(R.id.order_action_bar_text_view);
        myView.setText(status);
    }

}
