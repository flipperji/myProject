package com.flippey.qrdemo.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flippey.qrdemo.R;
import com.flippey.qrdemo.utils.SPUtils;
import com.flippey.qrdemo.utils.Util;
import com.flippey.qrdemo.widget.TabRadioGroup;

public class MainActivity extends AppCompatActivity implements TabRadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private MainActivity mInstance;
    private TabRadioGroup mTabGroup;
    public static final int TAB_HOME = 0;
    public static final int TAB_ME = 1;
    public static final int[] TAB_IDS = {R.id.main_trb_home, R.id.main_trb_me};
    private FrameLayout mContainer;
    private LinearLayout mLlHome;
    private LinearLayout mLlMe;
    private TextView mTvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = MainActivity.this;
        initView();
        String token = (String) SPUtils.get(mInstance, SPUtils.TOKEN, "");
        String userName = (String) SPUtils.get(mInstance, SPUtils.USERNAME, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(mInstance, LoginActivity.class));
            mInstance.finish();
        } else {
            mTvUserName.setText(userName);
        }
    }

    private void initView() {
        mTabGroup = (TabRadioGroup) findViewById(R.id.main_tab_group);
        mTvUserName = (TextView) findViewById(R.id.act_main_tv_username);
        mLlHome = findViewById(R.id.act_main_ll_home);
        mLlMe = findViewById(R.id.act_main_ll_me);
        mTabGroup.setOnCheckedChangeListener(this);
        mTabGroup.check(TAB_IDS[TAB_HOME]);
        findViewById(R.id.act_main_tv_traffic_query).setOnClickListener(this);
        findViewById(R.id.act_main_tv_change_ticket).setOnClickListener(this);
        findViewById(R.id.act_main_tv_scan_code).setOnClickListener(this);
        findViewById(R.id.act_main_tv_logout).setOnClickListener(this);
    }


    @Override
    public void onCheckedChanged(TabRadioGroup group, int checkedId) {
        int tab = TAB_HOME;
        switch (checkedId) {
            case R.id.main_trb_home:
                tab = TAB_HOME;
                break;
            case R.id.main_trb_me:
                tab = TAB_ME;
                break;
        }
        chageView(tab);
    }

    private void chageView(int tab) {
        mLlHome.setVisibility(tab == TAB_HOME ? View.VISIBLE : View.GONE);
        mLlMe.setVisibility(tab == TAB_ME ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_main_tv_traffic_query:
                //流量查询
                startActivity(new Intent(mInstance,TicketSaleQueryActivity.class));
                break;
            case R.id.act_main_tv_change_ticket:
                //换票助手
                startActivity(new Intent(mInstance,TicketAssistantActivity.class));
                break;
            case R.id.act_main_tv_scan_code:
                //扫码验票
                if (Util.isNetworkConnected(mInstance)) {
                    startActivity(new Intent(mInstance, SimpleCaptureActivity.class));
                } else {
                    Util.showToast(mInstance,"当前网络异常，请稍后重试");
                }
                break;
            case R.id.act_main_tv_logout:
                //退出登陆
                SPUtils.clear(mInstance);
                startActivity(new Intent(mInstance, LoginActivity.class));
                mInstance.finish();
                break;
        }
    }
}
