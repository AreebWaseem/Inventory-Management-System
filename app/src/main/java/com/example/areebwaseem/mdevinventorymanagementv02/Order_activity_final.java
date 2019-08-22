package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

public class Order_activity_final extends AppCompatActivity  {
    private static final String TAG = "Order_activity_final";
    MenuItem myItem;
    Menu menuNav;
    int current_frag_index=0;
    boolean home_bar_selected=false;
    boolean customer_bar_selected=false;
    boolean requested_bar_selected=false;
    boolean returned_bar_selected=false;
    public boolean isNew=true;
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
        setContentView(R.layout.activity_order_final);

        mySharedPreferences = getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);

        adapter = new sectionsStatePageAdapter(getSupportFragmentManager());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout_order);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.order_action_layout);

        viewPager = (ViewPager) findViewById(R.id.fragmentContainer_order);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBottom_order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.navigation_bot_order_view_all)
                {
                    setupViewPager(viewPager,"manage_order_fragment");
                }
                else if (item.getItemId()==R.id.navigation_bot_order_add)
                {
                    setupViewPager(viewPager, "create_order_product");
                }
                else if (item.getItemId()==R.id.navigation_bot_order_home)
                {
                    finish();
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_bot_order_view_all);

    }
    public void setupOrderDetail(String fragType, String order_id)
    {
        order_current=order_id;
        setupViewPager( viewPager, "orders_detail_fragment" );
    }
    public void setProductData(int myArray[])
    {
        myQuantity = new int[myArray.length];
        for (int i=0;i<myQuantity.length;i++)
        {
            myQuantity[i]= myArray[i];
        }
    }

    public void setItemChecked(String id) {
        if (this!=null) {
            if (!id.equals("order_payment")) {
                order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
                order_confirmation_button.setVisibility(View.INVISIBLE);
            }
            Menu current = bottomNavigationView.getMenu();
            if (id.equals("orders")) {
                setActionBarStatus("manage_order_fragment");
                current.findItem(R.id.navigation_bot_order_view_all).setChecked(true);
            } else if (id.equals("orders_detail")) {
                setActionBarStatus("orders_detail_fragment");
                current.findItem(R.id.navigation_bot_order_view_all).setChecked(true);
            } else if (id.equals("create_order_product")) {
                setActionBarStatus("create_order_product");
                current.findItem(R.id.navigation_bot_order_add).setChecked(true);
            } else if (id.equals("select_customer")) {
                setActionBarStatus("select_customer");
                current.findItem(R.id.navigation_bot_order_add).setChecked(true);
            } else if (id.equals("order_payment")) {
                setActionBarStatus("order_payment");
                current.findItem(R.id.navigation_bot_order_add).setChecked(true);
                order_confirmation_button.setVisibility(View.VISIBLE);
            }
        }
    }
    public void order_select_customer_viewPager()
    {
        setupViewPager(viewPager, "select_customer");
    }
    public void setUpPaymentViewPager()
    {
setupViewPager(viewPager, "order_payment");
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

             if (fragmentType.equals("manage_order_fragment"))
            {
                adapter.addFragment(new manage_order_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("orders_detail_fragment"))
            {
                adapter.addFragment(new orders_fragment_view_detail(), fragmentType);
            }
            else if (fragmentType.equals("create_order_product"))
             {
                 adapter.addFragment(new order_create_product_fragment_view(), fragmentType);
             }
             else if (fragmentType.equals("select_customer"))
             {
                 adapter.addFragment(new customers_List_order_fragment_view(), fragmentType);
             }
             else if (fragmentType.equals("order_payment"))
             {
                 adapter.addFragment(new orders_payment_fragment_view_detail(), fragmentType);
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
    public void order_confirmed_set_view_pager()
    {
      setupViewPager(viewPager, "manage_order_fragment");
        order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
        order_confirmation_button.setVisibility(View.INVISIBLE);
        isNew=true;
        setItemChecked("orders");
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
    public void setCustomerData(String a, String b, String c)
    {
em=a;
ad=b;
nm=c;

    }
    public void setBottomNavItem(int itemNumber)
    {
        bottomNavigationView.setSelectedItemId(itemNumber);
    }
    private void setActionBarStatus(String title) {

        if (title.equals("orders_detail_fragment"))
        {
            setToolbarStatus("Order Detail");
            setToolbarStatus(getResources().getString(R.string.order_detail_string));
        }

        else if (title.equals("manage_order_fragment"))
        {
            setToolbarStatus(getResources().getString(R.string.orders_string));
           // setToolbarStatus("Orders");
        }
        else if (title.equals("create_order_product"))
        {
           // setToolbarStatus("Select Product(s)");
            setToolbarStatus(getResources().getString(R.string.select_products_string));
        }
        else if (title.equals("select_customer"))
        {
            //setToolbarStatus(getResources().getString(R.string.sele));
           // setToolbarStatus("Select Customer");
            setToolbarStatus(getResources().getString(R.string.add_customer_string));
        }
        else if (title.equals("order_payment"))
        {
          //  setToolbarStatus(getResources().getString(R.string.confirm_string));
          setToolbarStatus(getResources().getString(R.string.confirm_string));
        }



    }
   public void setDiscount(String dis)
   {

   }
   public void setCustomer(String cust_id, String cust_name, String cust_address)
   {

   }
  public void set_products(JSONArray productsArray)
   {

   }
  public void set_payment_details(String sub_tot, String grand_tot, String receive_amount, String returned_amount, String Payment_Method,String payment_ref)
  {


  }


    public void setToolbarStatus(String status){
        TextView myView = (TextView) findViewById(R.id.order_action_bar_text_view);
        myView.setText(status);
    }

}
