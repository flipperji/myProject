package com.flippey.qrdemo.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by flippey on 2018/3/12 17:35.
 */

public class LoginBean implements Serializable {
    public static final String SUCCESS = "success";
    public static final String TOKEN = "token";

    private String success;

    private String token;

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
}
