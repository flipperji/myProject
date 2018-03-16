package com.flippey.qrdemo.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flippey on 2018/3/13 15:09.
 */

public class TicketListBean {
    public static final String CREATETIME = "createtime";
    public static final String END_DATE_TIME = "end_date_time";
    public static final String ID = "id";
    public static final String IDCARD = "idcard";
    public static final String ORDER_ID = "order_id";
    public static final String PRICE = "price";
    public static final String STATUS = "status";
    public static final String TEL = "tel";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_TITLE = "ticket_title";
    public static final String TICKET_TYPE = "ticket_type";


    private String createtime;
    private String end_date_time;
    private String id;
    private String idcard;
    private String order_id;
    private Double price;
    private String status;
    private String tel;
    private String ticket_code;
    private String ticket_title;
    private String ticket_type;

    private TicketListBean() {

    }


    public static List<TicketListBean> parse(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<TicketListBean> ticketList = new ArrayList<>();
        int length = array.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                JSONObject obj = (JSONObject) array.get(i);
                ticketList.add(parse(obj));
            }
        }
        return ticketList;
    }


    private static TicketListBean parse(JSONObject object) {
        TicketListBean bean = new TicketListBean();

        try {
            if (object.has(CREATETIME) && object.get(CREATETIME) != JSONObject.NULL) {
                bean.createtime = object.getString(CREATETIME);
            }
            if (object.has(END_DATE_TIME) && object.get(END_DATE_TIME) != JSONObject.NULL) {
                bean.end_date_time = object.getString(END_DATE_TIME);
            }
            if (object.has(ID) && object.get(ID) != JSONObject.NULL) {
                bean.id = object.getString(ID);
            }
            if (object.has(IDCARD) && object.get(IDCARD) != JSONObject.NULL) {
                bean.idcard = object.getString(IDCARD);
            }
            if (object.has(ORDER_ID) && object.get(ORDER_ID) != JSONObject.NULL) {
                bean.order_id = object.getString(ORDER_ID);
            }
            if (object.has(PRICE) && object.get(PRICE) != JSONObject.NULL) {
                bean.price = object.getDouble(PRICE);
            }
            if (object.has(STATUS) && object.get(STATUS) != JSONObject.NULL) {
                bean.status = object.getString(STATUS);
            }
            if (object.has(TEL) && object.get(TEL) != JSONObject.NULL) {
                bean.tel = object.getString(TEL);
            }
            if (object.has(TICKET_CODE) && object.get(TICKET_CODE) != JSONObject.NULL) {
                bean.ticket_code = object.getString(TICKET_CODE);
            }
            if (object.has(TICKET_TITLE) && object.get(TICKET_TITLE) != JSONObject.NULL) {
                bean.ticket_title = object.getString(TICKET_TITLE);
            }
            if (object.has(TICKET_TYPE) && object.get(TICKET_TYPE) != JSONObject.NULL) {
                bean.ticket_type = object.getString(TICKET_TYPE);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(String end_date_time) {
        this.end_date_time = end_date_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(String ticket_code) {
        this.ticket_code = ticket_code;
    }

    public String getTicket_title() {
        return ticket_title;
    }

    public void setTicket_title(String ticket_title) {
        this.ticket_title = ticket_title;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }
}
