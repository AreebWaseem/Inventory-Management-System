package com.example.areebwaseem.mdevinventorymanagementv02;

/**
 * Created by areebwaseem on 11/22/17.
 */

public class requested_product_search_custom_last {
    private String total_number="";
    private String ord_date="";
    private String ord_code="";
    private String ord_status="";
    private String product_DATA="";
    private String product_id="";

    public requested_product_search_custom_last(String tot_num, String ord_dat, String ord_cod, String ord_stat, String prod_dat, String prod_id)
    {
       this.product_DATA=prod_dat;
       this.ord_status=ord_stat;
       this.ord_code=ord_cod;
       this.ord_date=ord_dat;
       this.total_number=tot_num;
       this.product_id=prod_id;

    }
    public String getProduct_DATA()
    {
        return product_DATA;
    }
    public String getOrd_status()
    {
        return ord_status;
    }
    public String getTotal_number()
    {
        return total_number;
    }
    public String getOrd_date()
    {
        return ord_date;
    }
    public String getOrd_code()
    {
        return ord_code;
    }
    public String getProduct_id()
    {
        return product_id;
    }

}
