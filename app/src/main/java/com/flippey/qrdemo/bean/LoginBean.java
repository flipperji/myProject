package com.flippey.qrdemo.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flippey on 2018/3/12 17:35.
 */

public class LoginBean implements Serializable {
    public static final String SUCCESS = "success";
    public static final String TOKEN = "token";
    public static final String STATUS = "status";
    public static final String DATA = "data";

    private String success;

    private String token;
    private int status;
    private List<SaleBean> data = new ArrayList<>();

    private LoginBean() {

    }

    public static LoginBean parse(JSONObject object) {
        LoginBean loginBean = new LoginBean();

        try {
            if (object.has(SUCCESS)) {
                loginBean.success = object.getString(SUCCESS);
            }
            if (object.has(TOKEN)) {
                loginBean.token = object.getString(TOKEN);
            }
            if (object.has(STATUS)) {
                loginBean.status = object.getInt(STATUS);
            }

            if (object.has(DATA) && object.get(DATA) instanceof JSONArray) {
                JSONArray array = object.getJSONArray(DATA);
                int length = array.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = (JSONObject) array.get(i);
                        loginBean.data.add(SaleBean.parse(obj));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginBean;

}

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SaleBean> getData() {
        return data;
    }

    public void setData(List<SaleBean> data) {
        this.data = data;
    }
}
