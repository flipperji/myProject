package com.flippey.qrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * Created by flippey on 2018/3/7 13:21.
 */

public class LoginActivity extends AppCompatActivity {
    private LoginActivity mInstance;
    private EditText mAccountInputArea;
    private String url = "http://www.love360.net/zhpw/zhpw-admin/src/request/auth.php";
    private TextView mLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mInstance = LoginActivity.this;
        initView();
    }

    private void initView() {
        LoginRegisterInputBox accountInputBox = (LoginRegisterInputBox) findViewById(R.id.act_login_account);
        mAccountInputArea = accountInputBox.findViewById(R.id.et_login_register_inputbox_inputarea);
        CharSequence charSequence = mAccountInputArea.getText();
        if (charSequence != null) { // 将光标置于末尾
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }

        mLogin = (TextView) findViewById(R.id.act_login_tv_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mInstance,MainActivity.class));
                //getData();
            }
        });

    }

    private void getData() {
        OkGo.<String>post(url)
                .tag(mInstance)
                .params("username", "ck1")
                .params("password","111111")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("tttt", "onSuccess: " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

    }
}
