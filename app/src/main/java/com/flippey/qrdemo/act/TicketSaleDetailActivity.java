package com.flippey.qrdemo.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.flippey.qrdemo.R;
import com.flippey.qrdemo.bean.LoginBean;
import com.flippey.qrdemo.bean.SaleBean;
import com.flippey.qrdemo.utils.SPUtils;
import com.flippey.qrdemo.utils.Util;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by flippey on 2018/3/19 15:40.
 */

public class TicketSaleDetailActivity extends AppCompatActivity {
    public static final String TYPE = "type";
    private int mType;
    private TextView mTvTitle;
    private TicketSaleDetailActivity mInstance;
    private TextView mTvNum;
    private TextView mTvPrice;
    private String url = "http://shop.shenglenet.com/zhpw-admin/service/app_figure.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_sale_detail);
        mInstance = TicketSaleDetailActivity.this;
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TYPE)) {
            mType = intent.getIntExtra(TYPE, 0);
        }
        mTvTitle = (TextView) findViewById(R.id.act_ticket_sale_detail_tv_title);
        switch (mType) {
            case 0:
                mTvTitle.setText("历史总销量");
                break;
            case 1:
                mTvTitle.setText("月销量");
                break;
            case 2:
                mTvTitle.setText("周销量");
                break;
            case 3:
                mTvTitle.setText("日销量");
                break;

        }
        findViewById(R.id.act_ticket_sale_detail_ibt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInstance.finish();
            }
        });
        mTvNum = (TextView) findViewById(R.id.act_ticket_sale_detail_tv_num);
        mTvPrice = (TextView) findViewById(R.id.act_ticket_sale_detail_tv_price);
        String token = (String) SPUtils.get(mInstance, SPUtils.TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            return;
        }
        OkGo.<String>get(url)
                .tag(mInstance)
                .params("tk", token)
                .params("type", mType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject object = new JSONObject(body);
                            LoginBean parse = LoginBean.parse(object);
                            List<SaleBean> data = parse.getData();
                            if (data != null && data.size() > 0) {
                                final SaleBean saleBean = data.get(0);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (mType) {
                                            case 0:
                                                mTvNum.setText("历史总销量：" + saleBean.getSold_ticket_count());
                                                mTvPrice.setText(Util.addSymble("总金额：" + saleBean.getTotal_money() / 100));
                                                break;
                                            case 1:
                                                mTvNum.setText("月销量：" + saleBean.getSold_ticket_count());
                                                mTvPrice.setText(Util.addSymble("总金额：" + saleBean.getTotal_money() / 100));
                                                break;
                                            case 2:
                                                mTvNum.setText("周销量：" + saleBean.getSold_ticket_count());
                                                mTvPrice.setText(Util.addSymble("总金额：" + saleBean.getTotal_money() / 100));
                                                break;
                                            case 3:
                                                mTvNum.setText("日销量：" + saleBean.getSold_ticket_count());
                                                mTvPrice.setText(Util.addSymble("总金额：" + saleBean.getTotal_money() / 100));
                                                break;

                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
