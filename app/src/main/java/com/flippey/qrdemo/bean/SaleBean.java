package com.flippey.qrdemo.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flippey on 2018/3/19 16:38.
 */

public class SaleBean {
    public static final String SOLD_TICKET_COUNT = "sold_ticket_count";
    public static final String TOTAL_MONEY = "total_money";


    private long sold_ticket_count;
    private double total_money;


    private SaleBean() {

    }


    public static SaleBean parse(JSONObject object) {
        SaleBean saleBean = new SaleBean();
        try {
            if (object.has(SOLD_TICKET_COUNT)) {
                saleBean.sold_ticket_count = object.getLong(SOLD_TICKET_COUNT);
            }

            if (object.has(TOTAL_MONEY)) {
                saleBean.total_money = object.getDouble(TOTAL_MONEY);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return saleBean;
    }

    public long getSold_ticket_count() {
        return sold_ticket_count;
    }

    public void setSold_ticket_count(long sold_ticket_count) {
        this.sold_ticket_count = sold_ticket_count;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }
}
