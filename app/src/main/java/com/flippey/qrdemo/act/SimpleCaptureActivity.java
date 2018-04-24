package com.flippey.qrdemo.act;

import android.os.Bundle;
import android.text.TextUtils;

import com.flippey.qrdemo.bean.LoginBean;
import com.flippey.qrdemo.utils.SPUtils;
import com.flippey.qrdemo.utils.Util;
import com.flippey.qrlib.CaptureActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by xdj on 16/9/17.
 */

public class SimpleCaptureActivity extends CaptureActivity {
    protected SimpleCaptureActivity mInstance = this;
    private String url = "http://shop.shenglenet.com/zhpw-admin/src/request/ticket_pool_manage/check_ticket.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mInstance = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void handleResult(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            Util.showToast(mInstance,"扫码失败，请稍后重试");
            restartPreview();
        } else {
            String token = (String) SPUtils.get(mInstance, SPUtils.TOKEN, "");
            if (TextUtils.isEmpty(token)) {
                return;
            }
            OkGo.<String>post(url)
                    .tag(mInstance)
                    .params("tk", token)
                    .params("tcode",resultString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String body = response.body();
                            try {
                                JSONObject object = new JSONObject(body);
                                LoginBean parse = LoginBean.parse(object);
                                int status = parse.getStatus();
                                switch (status) {
                                    case 0:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Util.showToast(mInstance,"token无效");
                                            }
                                        });
                                        break;
                                    case 1:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Util.showToast(mInstance,"验票成功");
                                            }
                                        });
                                        break;
                                    case 2:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Util.showToast(mInstance,"数据库出错");
                                            }
                                        });
                                        break;
                                    case 3:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Util.showToast(mInstance,"该票目前的状态不能兑换");
                                            }
                                        });
                                        break;
                                    case 4:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Util.showToast(mInstance,"当前用户所在的景区也该票所属景区不一致");
                                            }
                                        });
                                        break;
                                    case 5:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Util.showToast(mInstance,"此票已过期");
                                            }
                                        });
                                        break;

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
