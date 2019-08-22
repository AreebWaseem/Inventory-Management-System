package com.example.areebwaseem.mdevinventorymanagementv02;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NavBarSample extends AppCompatActivity {
    MenuItem myItem;
    Menu menuNav;



    boolean home_bar_selected=false;
    boolean customer_bar_selected=false;
    boolean requested_bar_selected=false;
    boolean returned_bar_selected=false;
    int myQuantity[];
    SharedPreferences mySharedPreferences;
    private static final String TAG = "NavBarSample";
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mainView;
    String invoice_number;
    int current_frag_index=0;
    BottomNavigationView bottomNavigationView;
    private sectionsStatePageAdapter adapter;
    public ViewPager viewPager;
    String currentFragment="";
    String stockCodeDetail="";
    ImageButton profile_view_request;
    public String order_current;
    public String current_customer_id="";
    public String requested_products_detail_view_id="";
    public String return_product_detail_id="";
    private  void checkInternetPermissions(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.INTERNET");
            if(permissionCheck!=0){
                this.requestPermissions(new String[]{Manifest.permission.INTERNET},1001);
            }

            else
            {
                Log.i("Has", "Permissions");
            }
        }
    }
    public void makeToast(String toast_text, boolean lengthy){
        if (lengthy) {
            Toast.makeText(this, toast_text, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, toast_text, Toast.LENGTH_SHORT).show();
        }
    }


    public void showrequestedproducts()
    {
        setupViewPager(viewPager,"requested_products");
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
    public void setupRequestProductsDetailView(String id)
    {
        requested_products_detail_view_id=id;
        setupViewPager(viewPager, "request_products_detail_view");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_sample);
        checkInternetPermissions();
        mySharedPreferences = getApplicationContext().getSharedPreferences("com.example.areebwaseem.inventorymanagementmdevtech", Context.MODE_PRIVATE);

        adapter = new sectionsStatePageAdapter(getSupportFragmentManager());
        mainView = (NavigationView)findViewById(R.id.nav_view_main);
        mainView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
                   if (item.getItemId()==R.id.nav_returned) {
                     // setupViewPager(viewPager, "returned_products_fragment");
                    //   Log.d(TAG, "List size" + String.valueOf(adapter.mFragmentTitleList.size()));
                     home_bar_selected=false;
                     customer_bar_selected=false;
                     requested_bar_selected=false;
                      returned_bar_selected=true;

                       bottomNavigationView.getMenu().clear();
                       bottomNavigationView.inflateMenu(R.menu.returned_products_menu);
                       bottomNavigationView.setSelectedItemId(R.id.navigation_bot_returned_products_all);

                   }
                    else if (item.getItemId()==R.id.nav_assigned){
                    setupViewPager(viewPager, "assigned_products_fragment");
                       menuNav = bottomNavigationView.getMenu();
                       myItem = menuNav.findItem(R.id.navigation_bot_home);
                     //  myItem.setChecked(true);
                }

                else if (item.getItemId()==R.id.nav_invoice){
                      // setupViewPager(viewPager, "invoices");
                       if(!home_bar_selected) {
                           Log.d(TAG, String.valueOf(bottomNavigationView.getSelectedItemId()));
                           requested_bar_selected = false;
                           customer_bar_selected = false;
                           home_bar_selected = true;
                           returned_bar_selected = false;
                           bottomNavigationView.getMenu().clear();
                           bottomNavigationView.inflateMenu(R.menu.navigation);
                           bottomNavigationView.setSelectedItemId(R.id.navigation_bot_Invoice);
                       }
                       else {
                           bottomNavigationView.setSelectedItemId(R.id.navigation_bot_Invoice);
                       }
                   }

                   else if (item.getItemId()==R.id.nav_customer){
                      // setupViewPager(viewPager, "customer_list_fragment");
                       if(!customer_bar_selected) {
                           requested_bar_selected = false;
                           customer_bar_selected = true;
                           home_bar_selected = false;
                           returned_bar_selected = false;
                           bottomNavigationView.getMenu().clear();
                           bottomNavigationView.inflateMenu(R.menu.customer_bottom_menu);
                           bottomNavigationView.setSelectedItemId(R.id.navigation_bot_customer_view_all);
                       }
                       else {
                           bottomNavigationView.setSelectedItemId(R.id.navigation_bot_customer_view_all);
                       }
                      // Log.d(TAG,String.valueOf(bottomNavigationView.getSelectedItemId()));
      /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                      // bottomNavigationView.getMenu().clear();
                     // bottomNavigationView.inflateMenu(R.menu.customer_bottom_menu);

                   }
                   else if (item.getItemId()==R.id.nav_orders)
                   {
                     //  setupViewPager(viewPager, "manage_order_fragment");
                       Intent i = new Intent(NavBarSample.this, Order_activity_final.class);
                       startActivity(i);
                       Log.d(TAG,String.valueOf(bottomNavigationView.getSelectedItemId()));
                   }
                   else if (item.getItemId()==R.id.nav_logout)
                   {
                       mySharedPreferences.edit().putString("Login_Status", "false").apply();
                       finish();
                   }

                   else if (item.getItemId()==R.id.nav_request)
                   {
                       if (!home_bar_selected) {
                           requested_bar_selected = true;
                           customer_bar_selected = false;
                           home_bar_selected = false;
                           returned_bar_selected = false;
                           bottomNavigationView.getMenu().clear();
                           bottomNavigationView.inflateMenu(R.menu.navigation);
                           bottomNavigationView.setSelectedItemId(R.id.navigation_bot_Products);
                       }
                       else {
                           bottomNavigationView.setSelectedItemId(R.id.navigation_bot_Products);
                       }
                   }




             return false;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.fragmentContainer);
       // setupViewPager(viewPager, "assigned_products_fragment");
        setupViewPager(viewPager, "all_products");
        profile_view_request = (ImageButton) findViewById(R.id.imageButton);
        profile_view_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupViewPager(viewPager, "profile_view_fragment");
            }
        });
        home_bar_selected=true;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = bottomNavigationView.getMenu();
                if (item.getItemId()==R.id.navigation_bot_Products){
                    requested_bar_selected=true;
                    customer_bar_selected=false;
                    home_bar_selected=false;
                    returned_bar_selected=false;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.received_products_menu);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_requested_products_add);
                //  setupViewPager(viewPager, "requested_products");

                    Log.d(TAG, "All products clickedddddd");
                }
                else if (item.getItemId()==R.id.navigation_bot_requested_products_all)
                {
                    setupViewPager(viewPager, "requested_products");
                }
                else if (item.getItemId()==R.id.navigation_bot_requested_products_home)
                {
                    home_bar_selected=true;
                    requested_bar_selected=false;
                    customer_bar_selected=false;
                    returned_bar_selected=false;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.navigation);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_home);

                }
                else if (item.getItemId()==R.id.navigation_bot_Invoice){

                    setupViewPager(viewPager, "invoices");

                   // bottomNavigationView.setSelectedItemId(R.id.navigation_bot_requested_products_add);
                }
                else if (item.getItemId()==R.id.navigation_bot_customer){
                    //////////// bar selection //////////////////
                    customer_bar_selected=true;
                    home_bar_selected=false;
                    requested_bar_selected=false;
                    returned_bar_selected=false;
                    //////////////////////////////////////////////
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.customer_bottom_menu);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_customer_view_all);
                }
                else if (item.getItemId()==R.id.navigation_bot_Orders)
                {
                    Intent i = new Intent(NavBarSample.this, Order_activity_final.class);
                    startActivity(i);
                }
                else if (item.getItemId()==R.id.navigation_bot_home)
                {
                   // setupViewPager(viewPager, "profile_view_fragment");
                    Menu current = bottomNavigationView.getMenu();
                    current.findItem(R.id.navigation_bot_home).setChecked(true);
                    setupViewPager(viewPager,"all_products");

                }
                else if (item.getItemId()==R.id.navigation_bot_customer_home)
                {
                    //////////// bar selection //////////////////
                    returned_bar_selected=false;
                    home_bar_selected=true;
                    customer_bar_selected=false;
                    requested_bar_selected=false;
                    //////////////////////////////////////////////
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.navigation);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_home);
                }
                else if (item.getItemId()==R.id.navigation_bot_customer_view_all)
                {
                    setupViewPager(viewPager, "customer_list_fragment");
                }
                else if (item.getItemId()==R.id.navigation_bot_customer_add)
                {
                setupViewPager(viewPager, "customer_add_fragment");
                }
                else if (item.getItemId()==R.id.navigation_bot_returned_products_all)
                {
                    setupViewPager(viewPager, "returned_products_fragment");
                }

                else if (item.getItemId()==R.id.navigation_bot_returned_home)
                {
                    returned_bar_selected=false;
                    home_bar_selected=true;
                    customer_bar_selected=false;
                    requested_bar_selected=false;
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.navigation);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_home);
                }
                else if (item.getItemId()==R.id.navigation_bot_return_products_to_inventory)
                {
                    setupViewPager(viewPager, "return_to_inventory");
                }
                else if (item.getItemId()==R.id.navigation_bot_requested_products_add)
                {
                    setupViewPager(viewPager,"request_products_actual");
                }
                /*
                else if (item.getItemId()==R.id.navigation_bot_requested_products_received)
                {
                    setupViewPager(viewPager,"acknowledge_products");
                }
                */
                /*

                if (item.getItemId()!=R.id.navigation_bot_customer && item.getItemId()) {
                    item.setChecked(true);
                }
                */
                return false;
            }
        });




    }
    public void setupCustomer_list_view_pager()
    {
        setupViewPager(viewPager,"customer_list_fragment");
    }
    public void setToolbarStatus(String status){
        TextView myView = (TextView) findViewById(R.id.nav_action_bar);
        myView.setText(status);
    }


    public String fragmentCurrentType(){
        return currentFragment;
    }
    public String getStockCodeDetail(){
        return stockCodeDetail;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupViewPagerInvoiceDetail(String invoice_no){
        boolean check=false;
        invoice_number=invoice_no;
        for (int i=0;i<adapter.mFragmentTitleList.size();i++)
        {
            if (adapter.mFragmentTitleList.get(i).equals("invoice_detail_view")){
                Log.d(TAG,"Detail view setttttttttt");
                check=true;
            }
        }
        if (check==false){
            adapter.addFragment(new invoices_fragment_view_detail(), "invoice_detail_view");
            // adapter.notifyDataSetChanged();
            Log.d(TAG,"Detail view NOTtttttttttt    setttttttttt");
            adapter.notifyDataSetChanged();
            setViewPager("invoice_detail_view");
        }
        else if (check==true)
        {
            setViewPager("invoice_detail_view");
        }


    }

    public void setUpreturnedProductsFragmen()
    {
        setupViewPager(viewPager,"returned_products_fragment");
    }





    public void setupViewPagerReturnInventoryDetail(String id)
    {
        return_product_detail_id=id;
        setupViewPager(viewPager,"return_product_detail");
    }
    public void setrequested_viewpager()
    {
        setupViewPager(viewPager, "requested_products");
    }

    public void setItemChecked(String id)
    {

        Menu current = bottomNavigationView.getMenu();
        if (this!=null) {
            if (id.equals("products") || id.equals("assigned_products_fragment") || id.equals("invoice") || id.equals("orders") || id.equals("profile")) {
                if (!home_bar_selected) {
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.navigation);
                    returned_bar_selected=false;
                    home_bar_selected=true;
                    customer_bar_selected=false;
                    requested_bar_selected=false;
                    if (id.equals("products")) {
                        setActionBarStatus("all_products");
                        current.findItem(R.id.navigation_bot_home).setChecked(true);
                    } else if (id.equals("assigned")) {
                        setActionBarStatus("assigned_products_fragment");
                        current.findItem(R.id.navigation_bot_home).setChecked(true);
                    } else if (id.equals("invoice")) {
                        setActionBarStatus("invoices");
                        current.findItem(R.id.navigation_bot_Invoice).setChecked(true);
                    } else if (id.equals("orders")) {
                        setActionBarStatus("manage_order_fragment");
                        current.findItem(R.id.navigation_bot_Orders).setChecked(true);
                    } else if (id.equals("profile")) {
                        setToolbarStatus(getResources().getString(R.string.profile_string));
                        // current.findItem(R.id.navigation_bot_home).setChecked(false);
                    }
                }
                else {
                    if (id.equals("products")) {
                        setActionBarStatus("all_products");
                        current.findItem(R.id.navigation_bot_home).setChecked(true);
                    } else if (id.equals("assigned")) {
                        setActionBarStatus("assigned_products_fragment");
                        current.findItem(R.id.navigation_bot_home).setChecked(true);
                    } else if (id.equals("invoice")) {
                      // Toast.makeText(getApplicationContext(), "yeah", Toast.LENGTH_SHORT).show();
                        setActionBarStatus("invoices");
                        current.findItem(R.id.navigation_bot_Invoice).setChecked(true);
                    } else if (id.equals("orders")) {
                        setActionBarStatus("manage_order_fragment");
                        current.findItem(R.id.navigation_bot_Orders).setChecked(true);
                    } else if (id.equals("profile")) {
                        setToolbarStatus(getResources().getString(R.string.profile_string));
                        // current.findItem(R.id.navigation_bot_home).setChecked(false);
                    }

                }


            } else if (id.equals("addcustomers") || id.equals("customers")) {
                if (!customer_bar_selected) {
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.customer_bottom_menu);
                    returned_bar_selected=false;
                    home_bar_selected=false;
                    customer_bar_selected=true;
                    requested_bar_selected=false;
                    if (id.equals("addcustomers")) {
                        setActionBarStatus("customer_add_fragment");
                        current.findItem(R.id.navigation_bot_customer_add).setChecked(true);
                    } else if (id.equals("customers")) {
                        setActionBarStatus("customer_list_fragment");
                        current.findItem(R.id.navigation_bot_customer_view_all).setChecked(true);
                    }
                }
                else {
                    if (id.equals("addcustomers")) {
                        setActionBarStatus("customer_add_fragment");
                        current.findItem(R.id.navigation_bot_customer_add).setChecked(true);
                    } else if (id.equals("customers")) {
                        setActionBarStatus("customer_list_fragment");
                        current.findItem(R.id.navigation_bot_customer_view_all).setChecked(true);
                    }
                }




            } else if (id.equals("requested_products") || id.equals("requested_products_detail") || id.equals("request_products_actual") || id.equals("acknowledge_products")) {
                if (!requested_bar_selected) {
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.received_products_menu);
                    returned_bar_selected=false;
                    home_bar_selected=false;
                    customer_bar_selected=false;
                    requested_bar_selected=true;
                    if (id.equals("requested_products")) {
                        setActionBarStatus("requested_products");
                        current.findItem(R.id.navigation_bot_requested_products_all).setChecked(true);
                    } else if (id.equals("requested_products_detail")) {
                        setActionBarStatus("request_products_detail_view");
                        current.findItem(R.id.navigation_bot_requested_products_all).setChecked(true);
                    } else if (id.equals("request_products_actual")) {
                        setActionBarStatus("request_products_actual");
                        current.findItem(R.id.navigation_bot_requested_products_add).setChecked(true);
                    }
                    /*
                    else if (id.equals("acknowledge_products")) {
                        setActionBarStatus("acknowledge_products");
                        current.findItem(R.id.navigation_bot_requested_products_received).setChecked(true);
                    }
                    */
                }
                else {
                    if (id.equals("requested_products")) {
                        setActionBarStatus("requested_products");
                        current.findItem(R.id.navigation_bot_requested_products_all).setChecked(true);
                    } else if (id.equals("requested_products_detail")) {
                        setActionBarStatus("request_products_detail_view");
                        current.findItem(R.id.navigation_bot_requested_products_all).setChecked(true);
                    } else if (id.equals("request_products_actual")) {
                        setActionBarStatus("request_products_actual");
                        current.findItem(R.id.navigation_bot_requested_products_add).setChecked(true);
                    }
                    /*

                    else if (id.equals("acknowledge_products")) {
                        setActionBarStatus("acknowledge_products");
                        current.findItem(R.id.navigation_bot_requested_products_received).setChecked(true);
                    }
                    */
                }


            /*
            else if (id.equals("customers")) {
                setActionBarStatus("customer_list_fragment");
                current.findItem(R.id.navigation_bot_customer_view_all).setChecked(true);
            }
            */

            } else if (id.equals("returned") || id.equals("return_to_inventory") || id.equals("return_product_detail")) {
                if (!returned_bar_selected) {
                    bottomNavigationView.getMenu().clear();
                    bottomNavigationView.inflateMenu(R.menu.returned_products_menu);
                    returned_bar_selected=true;
                    home_bar_selected=false;
                    customer_bar_selected=false;
                    requested_bar_selected=false;
                    if (id.equals("returned")) {
                        setActionBarStatus("returned_products_fragment");
                        current.findItem(R.id.navigation_bot_returned_products_all).setChecked(true);
                    } else if (id.equals("return_to_inventory")) {
                        setActionBarStatus("return_to_inventory");
                        current.findItem(R.id.navigation_bot_return_products_to_inventory).setChecked(true);
                    } else if (id.equals("return_product_detail")) {
                        setActionBarStatus("request_products_actual");
                        current.findItem(R.id.navigation_bot_return_products_to_inventory).setChecked(true);
                    }
                }
                else
                {
                    if (id.equals("returned")) {
                        setActionBarStatus("returned_products_fragment");
                        current.findItem(R.id.navigation_bot_returned_products_all).setChecked(true);
                    } else if (id.equals("return_to_inventory")) {
                        setActionBarStatus("return_to_inventory");
                        current.findItem(R.id.navigation_bot_return_products_to_inventory).setChecked(true);
                    } else if (id.equals("return_product_detail")) {
                        setActionBarStatus("request_products_actual");
                        current.findItem(R.id.navigation_bot_return_products_to_inventory).setChecked(true);
                    }
                }

            }
        }

    }

    public void setupOrderDetail(String fragType, String order_id)
    {
        order_current=order_id;
        setupViewPager( viewPager, "orders_detail_fragment" );
    }
    public void setupViewPagerCustomerDetail(String customer_id){
        current_customer_id=customer_id;
        setupViewPager(viewPager, "customer_detail_edit_fragment");
    }
    public void setupViewPagerDetail(String fragmentTypeDetail, String stockCode){
  boolean check=false;

       if (fragmentTypeDetail.equals("productViewDetail_returned")){
            currentFragment= "returned_products_fragment";
            stockCodeDetail=stockCode;
            Log.d(TAG, stockCodeDetail);
        }
        else if (fragmentTypeDetail.equals("productViewDetail_assigned")){
            currentFragment = "assigned_products_fragment";
            stockCodeDetail=stockCode;
        }
        for (int i=0;i<adapter.mFragmentTitleList.size();i++)
        {
            if (adapter.mFragmentTitleList.get(i).equals("product_detail_view")){
                Log.d(TAG,"Detail view setttttttttt");
                check=true;
            }
        }
        if (check==false) {
            adapter.addFragment(new products_detail_fragment_view(), "product_detail_view");
            // adapter.notifyDataSetChanged();
            Log.d(TAG,"Detail view NOTtttttttttt    setttttttttt");
            adapter.notifyDataSetChanged();
            setViewPager("product_detail_view");
        }
        else if (check==true){setViewPager("product_detail_view");
        }

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

            } else if (fragmentType.equals("returned_products_fragment")) {
                adapter.addFragment(new returned_products_fragment_view(), "returned_products_fragment");
            }
            else if (fragmentType.equals("profile_view_fragment")){
                adapter.addFragment(new profile_view_fragment(), fragmentType);
            }

            else if (fragmentType.equals("all_products")){
                adapter.addFragment(new all_products_fragment_view(), fragmentType);
                Log.d(TAG, "all product fragment added");
            }
            else if (fragmentType.equals("invoices")){
                adapter.addFragment(new invoices_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("customer_list_fragment")){
                adapter.addFragment(new customers_List_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("customer_detail_edit_fragment"))
            {
                adapter.addFragment(new customers_detail_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("manage_order_fragment"))
            {
                adapter.addFragment(new manage_order_fragment_view(),fragmentType);
            }
            else if (fragmentType.equals("orders_detail_fragment"))
            {
                adapter.addFragment(new orders_fragment_view_detail(), fragmentType);
            }
            else if (fragmentType.equals("customer_add_fragment"))
            {
                adapter.addFragment(new customers_add_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("requested_products"))
            {
                adapter.addFragment(new requested_products_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("request_products_detail_view"))
            {
                adapter.addFragment(new requested_products_detail_fragment_view(), fragmentType);
            }
            else if (fragmentType.equals("return_to_inventory"))
            {
                adapter.addFragment(new return_products_to_inventory_fragment_view(), "return_to_inventory");
            }
            else if (fragmentType.equals("return_product_detail"))
            {
                adapter.addFragment(new return_products_to_inventory_detail_fragment_view(),"return_product_detail");
            }
            else if (fragmentType.equals("request_products_actual"))
            {
                adapter.addFragment(new request_product_create_fragment_view(),"request_products_actual");
            }
            else if (fragmentType.equals("acknowledge_products"))
            {
                adapter.addFragment(new acknowledge_received_products_form_inventory_fragment_view(), "acknowledge_products");
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
    public void setBottomNavItem(int itemNumber)
    {
        bottomNavigationView.setSelectedItemId(itemNumber);
    }
    private void setActionBarStatus(String title) {

        if (title.equals("returned_products_fragment"))
        {
            setToolbarStatus(getResources().getString(R.string.returned_products_string));
            //setToolbarStatus("Returned Products");
        }
        else if (title.equals("assigned_products_fragment")){
            setToolbarStatus(getResources().getString(R.string.assign_products_string));
            //setToolbarStatus("Assigned Products");
        }
        else if (title.equals("all_products")){
            setToolbarStatus(getResources().getString(R.string.products_string));
           // setToolbarStatus("Products");
        }
        else if (title.equals("invoices")){
            setToolbarStatus(getResources().getString(R.string.invoice_string));
          //  setToolbarStatus("Invoices");
        }
        else if (title.equals("customer_list_fragment")){
            setToolbarStatus(getResources().getString(R.string.cust_string));
           // setToolbarStatus("Customers");
        }
        else if (title.equals("product_detail_view") && currentFragment.equals("returned_products_fragment")){
            setToolbarStatus(getResources().getString(R.string.retrun_products_string));
           // setToolbarStatus("Returned Products");
        }
        else if (title.equals("product_detail_view") && currentFragment.equals("assigned_products_fragment")){
            setToolbarStatus(getResources().getString(R.string.assign_products_string));
          //  setToolbarStatus("Assigned Products");
        }
        else if (title.equals("customer_detail_edit_fragment")){
            setToolbarStatus(getResources().getString(R.string.cust_string));
            //setToolbarStatus("Customer");
        }
        else if (title.equals("manage_order_fragment"))
        {
            setToolbarStatus(getResources().getString(R.string.order_string));
          //  setToolbarStatus("Orders");
        }
        else if (title.equals("customer_add_fragment"))
        {

            setToolbarStatus(getResources().getString(R.string.add_customer_string));
        }
        else if (title.equals("requested_products"))
        {
            setToolbarStatus(getResources().getString(R.string.request_products_string));
           // setToolbarStatus("Requested products");
        }
        else if (title.equals("request_products_detail_view"))
        {
           // setToolbarStatus("Requested Products");
            setToolbarStatus(getResources().getString(R.string.request_products_string));
        }
        else if (title.equals("return_to_inventory"))
        {
            setToolbarStatus(getResources().getString(R.string.returnable_products_string));
        }
        else if (title.equals("return_product_detail"))
        {
            setToolbarStatus(getResources().getString(R.string.returnable_products_string));
        }
        else if (title.equals("request_products_actual"))
        {
            setToolbarStatus(getResources().getString(R.string.request_products_string));
           // setToolbarStatus("Request");
        }
        else if (title.equals("acknowledge_products"))
        {
            setToolbarStatus("Acknowledge");
        }


    }
    public void setProductData(int myArray[]) {
        myQuantity = new int[myArray.length];
        for (int i = 0; i < myQuantity.length; i++) {
            myQuantity[i] = myArray[i];
        }
    }
    public void setUprequestConfirmViewPager()
    {

    }



}

