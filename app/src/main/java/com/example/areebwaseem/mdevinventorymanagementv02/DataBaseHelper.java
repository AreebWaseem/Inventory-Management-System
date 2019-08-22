package com.example.areebwaseem.mdevinventorymanagementv02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by areebwaseem on 11/13/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";

    public static final String DATABASE_NAME = "Orders_and_customers_offline.db";
    public static final String Column_1 = "ID";
    public static final String Column_2 = "NAME";
    public static final String Column_3 = "PHONE";
    public static final String Column_4 = "ADDRESS";
    public static final String ORDER_DETAILS_COLUMN = "ORDER_DETAILS";
    private static final String DB_TABLE = "table_image";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";

    private static final String CREATE_TABLE_IMAGE = "create table " + DB_TABLE + "("+
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " blob)";
    String name_db=""
            ;
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME,null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + "customer_info" +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PHONE TEXT, ADDRESS TEXT)");
        sqLiteDatabase.execSQL("create table " + "order_info" + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ORDER_DETAILS TEXT) ");

       // sqLiteDatabase.execSQL("create table " + "downloaded_images" + "(KEY_NAME TEXT, KEY_IMAGE BLOB) ");
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "customer_info" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "order_info" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE );
        onCreate(sqLiteDatabase);
    }
    public boolean insertData_to_customer(String name, String Phone, String Address)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_2, name);
        contentValues.put(Column_3, Phone);
        contentValues.put(Column_4, Address);
        long result = sqLiteDatabase.insert("customer_info", null, contentValues);
        if (result==-1)
        {
            return false;
        }
        else {
            return true;
        }
    }
    public boolean insertData_to_Images(String name, byte[] image)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(KEY_NAME,    name);
        cv.put(KEY_IMAGE,   image);
        long result = database.insert( DB_TABLE, null, cv );

        if (result==-1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor retreive_all_images()
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + "downloaded_images" ,null);
        return res;

    }






    public boolean insertData_to_order(String order_data)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_DETAILS_COLUMN, order_data);
        long result = sqLiteDatabase.insert("order_info", null, contentValues);
        if (result==-1)
        {
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getAllData_Customer(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + "customer_info",null);
        return res;
    }
    public Cursor getAllData_Order(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + "order_info",null);
        return res;
    }


    public boolean updateData_customer(String id, String name,String phone,String address)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_1, id);
        contentValues.put(Column_2, name);
        contentValues.put(Column_3, phone);
        contentValues.put(Column_4, address);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update("customer_info",contentValues, "ID = ?", new String[]{id});
        return true;
    }
    public boolean updateData_order(String id, String order_data)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column_1, id);
        // contentValues.put(Column_2, name);
        //  contentValues.put(Column_3, phone);
        contentValues.put(ORDER_DETAILS_COLUMN, order_data);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update("order_info",contentValues, "ID = ?", new String[]{id});
        return true;
    }
    public int delete_all_orders(String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete("order_info", "ID = ?", new String[]{id});

    }

}
