package com.example.areebwaseem.mdevinventorymanagementv02;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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

public class offline_order_activity extends AppCompatActivity {
    private static final String TAG = "offline_order_activity";

    int myQuantity[];
    MenuItem myItem;
    Menu menuNav;
    ImageButton order_confirmation_button;
    int current_frag_index=0;
    boolean home_bar_selected = true;
    boolean customer_bar_selected = false;
    boolean order_bar_selected = false;
    boolean returned_bar_selected = false;
    public boolean isNew=true;

     public String current_customer_name="";
    SharedPreferences mySharedPreferences;


    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mainView;

   public String current_customer_address;
    String invoice_number;
    BottomNavigationView bottomNavigationView;
    private sectionsStatePageAdapter adapter;
    public ViewPager viewPager;


    String currentFragment = "";
    String stockCodeDetail = "";
    ImageButton profile_view_request;
    public String order_current;
    public String current_customer_id = "";
    public String requested_products_detail_view_id = "";
    public String return_product_detail_id = "";


    private void checkInternetPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.INTERNET");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.INTERNET}, 1001);
            } else {
                Log.i("Has", "Permissions");
            }
        }
    }

    public void makeToast(String toast_text, boolean lengthy) {
        if (lengthy) {
            Toast.makeText(this, toast_text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, toast_text, Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void order_select_customer_viewPager()
    {
        setupViewPager(viewPager, "select_customer");
    }
    public void setProductData(int myArray[])
    {
        myQuantity = new int[myArray.length];
        for (int i=0;i<myQuantity.length;i++)
        {
            myQuantity[i]= myArray[i];
        }
    }

    @Override
    protected void onDestroy() {
        if (isMyServiceRunning(BackgroundService.class)) {
            Intent i = new Intent(this,BackgroundService.class);
            stopService(i);
        }

        super.onDestroy();
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

                }

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
/*
    public void setupRequestProductsDetailView(String id) {
        requested_products_detail_view_id = id;
        setupViewPager(viewPager, "request_products_detail_view");
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_order_activity);
        checkInternetPermissions();
        mySharedPreferences = getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);

        adapter = new sectionsStatePageAdapter(getSupportFragmentManager());



        /*

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout_offline_order);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.order_action_layout);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.fragmentContainer_offline_order);
        // setupViewPager(viewPager, "assigned_products_fragment");
       // setupViewPager(viewPager, "all_products");



        home_bar_selected = true;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBottom_offline_order);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                Menu menu = bottomNavigationView.getMenu();


                if (item.getItemId() == R.id.navigation_bot_offline_order_home) {
                    //  setupViewPager(viewPager, "requested_products");
                    order_bar_selected=false;
                    customer_bar_selected=false;
                    home_bar_selected=true;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.offline_order_main_menu);
                    setupViewPager(viewPager, "order_offline_home");

                } else if (item.getItemId() == R.id.navigation_bot_offline_customer) {

                    order_bar_selected=false;
                    customer_bar_selected=true;
                    home_bar_selected=false;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.offline_customer_menu);
                   setupViewPager(viewPager,"customer_all_offline");

                }


                else if (item.getItemId() == R.id.navigation_bot_order_view_all) {
                    order_bar_selected=true;
                    customer_bar_selected=false;
                    home_bar_selected=false;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.offline_order_menu);
                    setupViewPager(viewPager, "order_product_offline");

                }
//////////////////////////////////// Offline Order Menu //////////////////////////////////////////////////////


                else if (item.getItemId() == R.id.navigation_bot_order_main_home) {

                    order_bar_selected=false;
                    customer_bar_selected=false;
                    home_bar_selected=true;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.offline_order_main_menu);
                    setupViewPager(viewPager, "order_offline_home");
                }


                else if (item.getItemId() == R.id.navigation_bot_order_main_add) {

                    setupViewPager(viewPager, "order_product_offline");

                }
                else if (item.getItemId() == R.id.navigation_bot_order_main_view_all) {

                 setupViewPager(viewPager,"manage_order_offline");

                }


    /////////////////////////////////// Offline Customer Menu ///////////////////////////////////////////


                 else if (item.getItemId() == R.id.navigation_bot_offline_customer_home) {
                    order_bar_selected=false;
                    customer_bar_selected=false;
                    home_bar_selected=true;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.offline_order_main_menu);
                    setupViewPager(viewPager, "order_offline_home");
                }


                else if (item.getItemId() == R.id.navigation_bot_offline_customer_view_all) {
                    setupViewPager(viewPager,"customer_all_offline");
                }

                else if (item.getItemId() == R.id.navigation_bot_offline_customer_add) {
                 setupViewPager(viewPager,"addcustomers_offline");
                }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                return false;
            }
        });
        setupViewPager(viewPager, "order_offline_home");



    }







    public void setToolbarStatus(String status){
        TextView myView = (TextView) findViewById(R.id.order_action_bar_text_view);
        myView.setText(status);
    }

    public String fragmentCurrentType() {
        return currentFragment;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }







    public void setItemChecked(String id) {
        if (!id.equals("order_payment_offline"))
        {
            order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
            order_confirmation_button.setVisibility(View.INVISIBLE);
        }


          if (id.equals("order_offline_home") ) {
            if (!home_bar_selected) {
                order_bar_selected=false;
                customer_bar_selected=false;
                home_bar_selected=true;
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.offline_order_main_menu);
                Menu current = bottomNavigationView.getMenu();
                if (id.equals("order_offline_home"))
                {
                    setActionBarStatus("order_offline_home");
                    current.findItem(R.id.navigation_bot_offline_order_home).setChecked(true);
                }
            }
            else {
                Menu current = bottomNavigationView.getMenu();
                if (id.equals("order_offline_home"))
                {
                    setActionBarStatus("order_offline_home");
                    current.findItem(R.id.navigation_bot_offline_order_home).setChecked(true);
                }
            }


        }
       else if (id.equals("create_order_product") || id.equals("select_customer") || id.equals("order_payment_offline") ||id.equals("manage_order_offline")) {
            if (!order_bar_selected) {
                order_bar_selected=true;
                customer_bar_selected=false;
                home_bar_selected=false;
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.offline_order_menu);
                Menu current = bottomNavigationView.getMenu();
                if (id.equals("create_order_product")) {
                    setActionBarStatus("order_product_offline");
                    current.findItem(R.id.navigation_bot_order_main_add).setChecked(true);
                }
                else if (id.equals("select_customer")) {
                    setActionBarStatus("select_customer");
                    current.findItem(R.id.navigation_bot_order_main_add).setChecked(true);
                }
                else if (id.equals("order_payment_offline"))
                {
                    setActionBarStatus("order_payment_offline");
                    current.findItem(R.id.navigation_bot_order_main_add).setChecked(true);
                    order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
                    order_confirmation_button.setVisibility(View.VISIBLE);
                }
                else if (id.equals("manage_order_offline"))
                {
                    setActionBarStatus("manage_order_offline");
                    current.findItem(R.id.navigation_bot_order_main_view_all).setChecked(true);
                }
            }
            else {
                Menu current = bottomNavigationView.getMenu();
                if (id.equals("create_order_product")) {
                    setActionBarStatus("order_product_offline");
                    current.findItem(R.id.navigation_bot_order_main_add).setChecked(true);
                }
                else if (id.equals("select_customer")) {
                    setActionBarStatus("select_customer");
                    current.findItem(R.id.navigation_bot_order_main_add).setChecked(true);
                }
                else if (id.equals("order_payment_offline"))
                {
                    setActionBarStatus("order_payment_offline");
                    current.findItem(R.id.navigation_bot_order_main_add).setChecked(true);
                    order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
                    order_confirmation_button.setVisibility(View.VISIBLE);
                }
                else if (id.equals("manage_order_offline"))
                {
                    setActionBarStatus("manage_order_offline");
                    current.findItem(R.id.navigation_bot_order_main_view_all).setChecked(true);
                }
            }


        }


        else if (id.equals("customer_all_offline") || id.equals("addcustomers_offline"))
        {
            if (!customer_bar_selected) {
                order_bar_selected=false;
                customer_bar_selected=true;
                home_bar_selected=false;
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.offline_customer_menu);
                Menu current = bottomNavigationView.getMenu();
                if (id.equals("customer_all_offline"))
                {
                    setActionBarStatus("customer_all_offline");
                    current.findItem(R.id.navigation_bot_offline_customer_view_all).setChecked(true);
                }
                if (id.equals("addcustomers_offline"))
                {
                    setActionBarStatus("addcustomers_offline");
                    current.findItem( R.id.navigation_bot_offline_customer_add).setChecked(true);
                }
            }
            else {
                Menu current = bottomNavigationView.getMenu();
                if (id.equals("customer_all_offline"))
                {
                    setActionBarStatus("customer_all_offline");
                    current.findItem(R.id.navigation_bot_offline_customer_view_all).setChecked(true);
                }
                if (id.equals("addcustomers_offline"))
                {
                    setActionBarStatus("addcustomers_offline");
                    current.findItem( R.id.navigation_bot_offline_customer_add).setChecked(true);
                }
            }

        }

        /*
        else if (id.equals("addcustomers") || id.equals("customers")) {
            if (!customer_bar_selected) {
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.offline_customer_menu);
                customer_bar_selected=true;
            }
            if (id.equals("addcustomers")) {
                setActionBarStatus("customer_add_fragment");
                current.findItem(R.id.navigation_bot_customer_add).setChecked(true);
            } else if (id.equals("customers")) {
                setActionBarStatus("customer_list_fragment");
                current.findItem(R.id.navigation_bot_customer_view_all).setChecked(true);
            }


        } else if (id.equals("requested_products") || id.equals("requested_products_detail") || id.equals("request_products_actual") || id.equals("acknowledge_products")) {
            if (!order_bar_selected) {
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.offline_order_main_menu);
                order_bar_selected = true;
            }
            if (id.equals("requested_products")) {
                setActionBarStatus("requested_products");
                current.findItem(R.id.navigation_bot_requested_products_all).setChecked(true);
            } else if (id.equals("requested_products_detail")) {
                setActionBarStatus("request_products_detail_view");
                current.findItem(R.id.navigation_bot_requested_products_all).setChecked(true);
            } else if (id.equals("request_products_actual")) {
                setActionBarStatus("request_products_actual");
                current.findItem(R.id.navigation_bot_requested_products_add).setChecked(true);
            } else if (id.equals("acknowledge_products")) {
                setActionBarStatus("acknowledge_products");
                current.findItem(R.id.navigation_bot_requested_products_received).setChecked(true);
            }


        }
        */
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
            if (fragmentType.equals("assigned_products_fragment")) {
                adapter.addFragment(new products_fragment_view(), "assigned_products_fragment");

            } else if (fragmentType.equals("order_product_offline"))
            {
                adapter.addFragment(new order_create_product_offline_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("order_offline_home"))
            {
                adapter.addFragment(new order_offline_home_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("select_customer"))
            {
                adapter.addFragment(new customers_List_order_offline_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("order_payment_offline"))
            {
                adapter.addFragment(new orders_payment_offline_fragment_view_detail(),fragmentType);
            }
            else if (fragmentType.equals("customer_all_offline"))
            {
                adapter.addFragment(new customers_List_offline_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("addcustomers_offline"))
            {
                adapter.addFragment(new customers_add_offline_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("manage_order_offline"))
            {
                adapter.addFragment(new manage_order_offline_fragment_view(), fragmentType);
            }



            // adapter.addFragment(new invoice_fragment_view(), "invoice view");
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
                setViewPager(fragmentType);
            }
        } else if (check == true) {
            setViewPager(fragmentType);
            Log.d(TAG, fragmentType);
        }
    }

    public void setViewPager(String fragmentType) {
        int fragmentNumber = 0;
        boolean check = false;
        for (int i = 0; i < adapter.mFragmentTitleList.size(); i++) {
            if (fragmentType.equals(adapter.mFragmentTitleList.get(i))) {
                fragmentNumber = i;
                check = true;
            }
        }
        if (check == true) {
            viewPager.setCurrentItem(fragmentNumber);
            current_frag_index=fragmentNumber;
            setActionBarStatus(fragmentType);
            Log.d(TAG, "Pager Setttttttttt");
            Log.d(TAG, "Fragment Type " + fragmentType);
            Log.d(TAG, String.valueOf(adapter.mFragmentTitleList.size()));
        }

    }

    public void setBottomNavItem(int itemNumber) {
        bottomNavigationView.setSelectedItemId(itemNumber);
    }

    private void setActionBarStatus(String title) {

        if (title.equals("returned_products_fragment")) {

            setToolbarStatus(getResources().getString(R.string.returned_products_string));

        } else if (title.equals("order_product_offline")) {

            setToolbarStatus(getResources().getString(R.string.select_products_string));

        }
        else if (title.equals("select_customer"))
        {
            setToolbarStatus(getResources().getString(R.string.add_customer_string));
        }
        else if (title.equals("order_offline_home"))
        {
            setToolbarStatus(getResources().getString(R.string.home_string));
        }
        else if (title.equals("order_payment_offline"))
        {
            setToolbarStatus(getResources().getString(R.string.confirm_string));
        }
        else if (title.equals("customer_all_offline"))
        {
            setToolbarStatus(getResources().getString(R.string.customers_string));

        }
        else if (title.equals("addcustomers_offline"))
        {
            setToolbarStatus(getResources().getString(R.string.add_customer_string));
        }
        else if (title.equals("manage_order_offline"))
        {
            setToolbarStatus(getResources().getString(R.string.orders_string));
        }

    }
    public void exitSelf()
    {
        finish();
    }
    public void setUpPaymentViewPager()
    {
        setupViewPager(viewPager, "order_payment_offline" );
    }
    public void order_confirmed_set_view_pager()
    {
        isNew=true;
        setupViewPager(viewPager, "order_offline_home");
        order_confirmation_button = (ImageButton) findViewById(R.id.image_button_order_confirmation);
        order_confirmation_button.setVisibility(View.INVISIBLE);
        setItemChecked("order_offline_home");
    }


}

