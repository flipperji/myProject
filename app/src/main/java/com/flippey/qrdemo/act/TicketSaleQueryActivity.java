package com.flippey.qrdemo.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flippey.qrdemo.R;

/**
 * Created by flippey on 2018/3/19 09:51.
 */

public class TicketSaleQueryActivity extends AppCompatActivity implements View.OnClickListener {
    private TicketSaleQueryActivity mInstance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_sale_query);
        mInstance = TicketSaleQueryActivity.this;
        initView();
    }

    private void initView() {
        findViewById(R.id.act_ticket_sale_query_tv_history).setOnClickListener(this);
        findViewById(R.id.act_ticket_sale_query_tv_month).setOnClickListener(this);
        findViewById(R.id.act_ticket_sale_query_tv_week).setOnClickListener(this);
        findViewById(R.id.act_ticket_sale_query_tv_daily).setOnClickListener(this);
        findViewById(R.id.act_ticket_sale_query_ibt).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(mInstance, TicketSaleDetailActivity.class);
        switch (view.getId()) {
            case R.id.act_ticket_sale_query_tv_history:
                intent.putExtra(TicketSaleDetailActivity.TYPE, 0);
                startActivity(intent);
                break;
            case R.id.act_ticket_sale_query_tv_month:
                intent.putExtra(TicketSaleDetailActivity.TYPE, 1);
                startActivity(intent);
                break;
            case R.id.act_ticket_sale_query_tv_week:
                intent.putExtra(TicketSaleDetailActivity.TYPE, 2);
                startActivity(intent);
                break;
            case R.id.act_ticket_sale_query_tv_daily:
                intent.putExtra(TicketSaleDetailActivity.TYPE, 3);
                startActivity(intent);
                break;
            case R.id.act_ticket_sale_query_ibt:
                mInstance.finish();
                break;
        }
    }
}
