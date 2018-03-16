package com.flippey.qrdemo.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flippey.qrdemo.R;
import com.flippey.qrdemo.bean.LoginBean;
import com.flippey.qrdemo.utils.SPUtils;
import com.flippey.qrdemo.utils.Util;
import com.flippey.qrdemo.utils.XidiFonts;
import com.flippey.qrdemo.widget.LoginRegisterInputBox;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by flippey on 2018/3/7 13:21.
 */

public class LoginActivity extends AppCompatActivity {
    private LoginActivity mInstance;
    private EditText mAccountInputArea;
    private String url = "http://shop.shenglenet.com/zhpw-admin/src/request/auth.php";
    private TextView mLogin;
    private Typeface mTypeface;
    private EditText mPwdInutArea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mInstance = LoginActivity.this;
        initView();
    }

    private void initView() {
        mTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/xidi.ttf");

        LoginRegisterInputBox accountInputBox = (LoginRegisterInputBox) findViewById(R.id.act_login_account);
        mAccountInputArea = accountInputBox.findViewById(R.id.et_login_register_inputbox_inputarea);
        ((TextView) accountInputBox.findViewById(R.id.tv_login_register_inputbox_lefticon))
                .setText(XidiFonts.icon_user);
        CharSequence charSequence = mAccountInputArea.getText();
        if (charSequence != null) { // 将光标置于末尾
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        LoginRegisterInputBox pwdInputBox = (LoginRegisterInputBox) findViewById(R.id.act_login_pwd);
        mPwdInutArea = pwdInputBox.findViewById(R.id.et_login_register_inputbox_inputarea);
        ((TextView) pwdInputBox.findViewById(R.id.tv_login_register_inputbox_lefticon)).setText
                (XidiFonts.icon_lock);
        mLogin = (TextView) findViewById(R.id.act_login_tv_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

    }

    private void getData() {
        final String userName = mAccountInputArea.getText().toString();
        String password = mPwdInutArea.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Util.showToast(mInstance,"用户名不能为空");
        } else if (TextUtils.isEmpty(password)) {
            Util.showToast(mInstance, "密码不能为空");
        } else {
            OkGo.<String>post(url)
                    .tag(mInstance)
                    .params("username", userName)
                    .params("password",password)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String body = response.body();
                            try {
                                JSONObject object = new JSONObject(body);
                                LoginBean parse = LoginBean.parse(object);
                                if ("0".equals(parse.getSuccess())) {
                                    Util.showToast(mInstance, "验证失败");
                                } else {
                                    SPUtils.put(mInstance,SPUtils.TOKEN,parse.getToken());
                                    SPUtils.put(mInstance,SPUtils.USERNAME,userName);
                                    startActivity(new Intent(mInstance,MainActivity.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
