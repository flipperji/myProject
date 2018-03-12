package com.flippey.qrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements TabRadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private MainActivity mInstance;
    private TabRadioGroup mTabGroup;
    public static final int TAB_HOME = 0;
    public static final int TAB_ME = 1;
    public static final int[] TAB_IDS = {R.id.main_trb_home, R.id.main_trb_me};
    private FrameLayout mContainer;
    private LinearLayout mLlHome;
    private LinearLayout mLlMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SimpleCaptureActivity.class);
                MainActivity.this.startActivityForResult(i, 2);
            }
        });*/
        mInstance = MainActivity.this;
        initView();
    }

    private void initView() {
        mTabGroup = (TabRadioGroup) findViewById(R.id.main_tab_group);
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
                break;
            case R.id.act_main_tv_change_ticket:
                //换票助手
                startActivity(new Intent(mInstance,MyDiscountActivity.class));
                break;
            case R.id.act_main_tv_scan_code:
                //扫码验票
                startActivity(new Intent(mInstance,SimpleCaptureActivity.class));
                break;
            case R.id.act_main_tv_logout:
                //退出登陆
                break;
        }
    }
}
