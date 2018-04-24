package com.flippey.qrdemo.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flippey.qrdemo.OnItemClickCallback;
import com.flippey.qrdemo.R;
import com.flippey.qrdemo.TicketAssistantAdapter;
import com.flippey.qrdemo.bean.LoginBean;
import com.flippey.qrdemo.bean.TicketListBean;
import com.flippey.qrdemo.utils.SPUtils;
import com.flippey.qrdemo.utils.Util;
import com.flippey.qrdemo.widget.BottomMenuDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/1/21.
 */
public class TicketAssistantActivity extends AppCompatActivity implements View.OnClickListener {
    private String url = "http://shop.shenglenet.com/zhpw-admin/service/ticket_list_for_hp.php";
    private String exchangeUrl = "http://shop.shenglenet.com/zhpw-admin/src/request/ticket_pool_manage/exchange_ticket.php";
    private static final int ALLDISCOUNT_GET_SUCCESS = 1;

    private View mBackTextView;
    private EditText mCouponChangeEditText;
    private TextView mCouponChangeBtn;

    private TextView mAvailableTextView;
    private TextView mAlreadyUsedTextView;
    private TextView mInValidTextView;
    private ImageView mIndicateImageView;

    private int deviceWidth = Util.getDeviceWidth();
    private int padding = (deviceWidth / 3 - (int) (90 * Util.getDensity())) / 2;
    private int offset = (deviceWidth / 3 - (int) (90 * Util.getDensity())) / 2;

    private OnItemClickCallback mCallback;

    private int mAvailableCount = 0;
    private int mInvalidCount = 0;
    private int mUsedCount = 0;
    private TicketAssistantActivity mInstance;
    private ListView mLvAvailable;
    private ListView mLvUsed;
    private ListView mLvInvalid;
    private LinearLayout mLlEmptyView;
    private int mCurrentTab = 0;
    private List<TicketListBean> mAvailableList = new ArrayList<>();
    private List<TicketListBean> mUsedList = new ArrayList<>();
    private List<TicketListBean> mInvalidList = new ArrayList<>();
    private TicketAssistantAdapter mInvalidAdapter;
    private TicketAssistantAdapter mUsedAdapter;
    private TicketAssistantAdapter mAvailableAdapter;
    private BottomMenuDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_assiatant);
        mInstance = TicketAssistantActivity.this;
        initView();
    }

    // 初始化UI界面。
    private void initView() {
        mBackTextView = findViewById(R.id.coupon_title_back);

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TicketAssistantActivity.this.finish();
            }
        });


        mCouponChangeEditText = (EditText) findViewById(R.id.coupon_number_edittext);
        mCouponChangeBtn = (TextView) findViewById(R.id.coupon_number_btn);
        mCouponChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = mCouponChangeEditText.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    Util.showToast(mInstance,"请输入手机号");
                } else {
                    if (Util.isPhoneNum(num)) {
                        getMyTickets(num);
                    } else {
                        Util.showToast(mInstance,"请输入正确的手机号");
                    }
                }
            }
        });
        mCouponChangeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 5fd9da
                //                if (TextUtils.isEmpty(s)) {
                //                    mCouponChangeBtn.setBackgroundColor(Color.argb(153, 95, 217, 218));
                //                    mCouponChangeBtn.setTextColor(Color.argb(153, 255, 255, 255));
                //                } else {
                //                    mCouponChangeBtn.setBackgroundColor(Color.argb(255, 95, 217, 218));
                //                    mCouponChangeBtn.setTextColor(Color.argb(255, 255, 255, 255));
                //                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAvailableTextView = (TextView) findViewById(R.id.tv_discount_available);
        mAlreadyUsedTextView = (TextView) findViewById(R.id.tv_discount_alreadyuesed);
        mInValidTextView = (TextView) findViewById(R.id.tv_discount_invalid);
        mAvailableTextView.setTag(0);
        mAlreadyUsedTextView.setTag(1);
        mInValidTextView.setTag(2);

        mIndicateImageView = (ImageView) findViewById(R.id.iv_discount_index);

        mAvailableTextView.setOnClickListener(TicketAssistantActivity.this);
        mAlreadyUsedTextView.setOnClickListener(TicketAssistantActivity.this);
        mInValidTextView.setOnClickListener(TicketAssistantActivity.this);
        mLvAvailable = (ListView) findViewById(R.id.act_ticket_assistant_lv_available);
        mLvUsed = (ListView) findViewById(R.id.act_ticket_assistant_lv_alreadyuesed);
        mLvInvalid = (ListView) findViewById(R.id.act_ticket_assistant_lv_invalid);
        mLlEmptyView = (LinearLayout) findViewById(R.id.act_ticket_assistant_no_ticket);
        changeTranslate(0);
        changeColor(0);
    }

    @Override
    public void onClick(View v) {
        if (v == mAvailableTextView) {
            mCurrentTab = 0;
            changeLvVisibility(mCurrentTab, mAvailableCount);
        } else if (v == mAlreadyUsedTextView) {
            mCurrentTab = 1;
            changeLvVisibility(mCurrentTab, mUsedCount);
        } else if (v == mInValidTextView) {
            mCurrentTab = 2;
            changeLvVisibility(mCurrentTab, mInvalidCount);
        }
        changeColor((Integer) v.getTag());
        changeTranslate((Integer) v.getTag());
    }

    private void changeColor(int id) {
        mAvailableTextView.setTextColor(Color.BLACK);
        mAlreadyUsedTextView.setTextColor(Color.BLACK);
        mInValidTextView.setTextColor(Color.BLACK);
        if (id == 0) {
            mAvailableTextView.setTextColor(Color.rgb(27, 134, 214));
        } else if (id == 1) {
            mAlreadyUsedTextView.setTextColor(Color.rgb(27, 134, 214));
        } else if (id == 2) {
            mInValidTextView.setTextColor(Color.rgb(27, 134, 214));
        }
    }

    private void changeTranslate(int id) {
        RelativeLayout.LayoutParams indicateLayoutParams = (RelativeLayout.LayoutParams)
                mIndicateImageView.getLayoutParams();
        if (id == 0) {
            indicateLayoutParams.setMargins(offset, 0, 0, 0);
        } else if (id == 1) {
            indicateLayoutParams.setMargins((int) (offset + deviceWidth
                    / 3), 0, 0, 0);
        } else if (id == 2) {
            indicateLayoutParams.setMargins((int) (offset + 2 * deviceWidth
                    / 3), 0, 0, 0);
        }
        mIndicateImageView.setLayoutParams(indicateLayoutParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (XidibuyConstants.SHOW_BY_NEW_WEBVIEW_INT == requestCode) {
            if (resultCode == RESULT_CANCELED) {
            } else if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra(ShowByWebView.TAG)) {
                    int index = data.getIntExtra(ShowByWebView.TAG, MainActivity.TAB_IDS[0]);
                    Intent intent = getIntent();
                    intent.putExtra(MyDiscountActivity.class.getSimpleName(), index);
                    setResult(Activity.RESULT_OK, intent);
                    MyDiscountActivity.this.finish();
                }
            }
        } else if (requestCode == XidibuyConstants.PRODUCT_DETAIL_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
            } else if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra(ProductDetailActivity.TAG)) {
                    int index = data.getIntExtra(ProductDetailActivity.TAG, MainActivity
                            .TAB_IDS[MainActivity.TAB_HOME]);
                    Intent intent = getIntent();
                    intent.putExtra(MyDiscountActivity.class.getSimpleName(), index);
                    setResult(Activity.RESULT_OK, intent);
                    this.finish();
                }
            }
        }*/
    }

    // 获取全部优惠券列表接口
    private void getMyTickets(String num) {
        //num = "15618329706";
        String token = (String) SPUtils.get(mInstance, SPUtils.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            OkGo.<String>get(url)
                    .tag(mInstance)
                    .params("tk", token)
                    .params("tel", num)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String body = response.body();
                            try {
                                JSONObject object = new JSONObject(body);
                                if (object.has("data") && object.get("data") != JSONObject.NULL&&object.getJSONArray("data").length()>0) {
                                    List<TicketListBean> ticketLists = TicketListBean.parse(object.getJSONArray("data"));
                                    if (ticketLists != null && ticketLists.size() > 0) {
                                        mAvailableList.clear();
                                        mUsedList.clear();
                                        mInvalidList.clear();
                                        for (int i = 0; i < ticketLists.size(); i++) {
                                            TicketListBean ticketListBean = ticketLists.get(i);
                                            String status = ticketListBean.getStatus();
                                            if ("0".equals(status)) {
                                                mAvailableList.add(ticketListBean);
                                            } else if ("1".equals(status) || "4".equals(status)) {
                                                mUsedList.add(ticketListBean);
                                            } else {
                                                mInvalidList.add(ticketListBean);
                                            }
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAvailableCount = mAvailableList.size();
                                                mAvailableTextView.setText(String.format(getString(R.string.available_tip), mAvailableCount));
                                                mInvalidCount = mInvalidList.size();
                                                mInValidTextView.setText(String.format(getString(R.string.invalid_tip), mInvalidCount));
                                                mUsedCount = mUsedList.size();
                                                mAlreadyUsedTextView.setText(String.format(getString(R.string.already_used_tip), mUsedCount));
                                                changeView();
                                                changeLvVisibility(mCurrentTab, mAvailableCount);
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAvailableList.clear();
                                            mUsedList.clear();
                                            mInvalidList.clear();
                                            mAvailableCount = mAvailableList.size();
                                            mAvailableTextView.setText(String.format(getString(R.string.available_tip), mAvailableCount));
                                            mInvalidCount = mInvalidList.size();
                                            mInValidTextView.setText(String.format(getString(R.string.invalid_tip), mInvalidCount));
                                            mUsedCount = mUsedList.size();
                                            mAlreadyUsedTextView.setText(String.format(getString(R.string.already_used_tip), mUsedCount));
                                            changeView();
                                            changeLvVisibility(mCurrentTab, mAvailableCount);
                                            Util.showToast(mInstance,"未查询到符合条件的票");
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAvailableCount = mAvailableList.size();
                                        mAvailableTextView.setText(String.format(getString(R.string.available_tip), mAvailableCount));
                                        mInvalidCount = mInvalidList.size();
                                        mInValidTextView.setText(String.format(getString(R.string.invalid_tip), mInvalidCount));
                                        mUsedCount = mUsedList.size();
                                        mAlreadyUsedTextView.setText(String.format(getString(R.string.already_used_tip), mUsedCount));
                                        changeView();
                                        changeLvVisibility(mCurrentTab, mAvailableCount);
                                        Util.showToast(mInstance, "未查询到符合条件的票");
                                        changeLvVisibility(mCurrentTab, mAvailableCount);
                                    }
                                });
                            }
                        }
                    });
        }
    }

    private void changeView() {
        if (mAvailableCount > 0) {
            if (mAvailableAdapter == null) {
                mAvailableAdapter = new TicketAssistantAdapter(mInstance, mAvailableList, new OnItemClickCallback() {
                    @Override
                    public void onItemClick(final String ticket_id) {
                        mDialog = new BottomMenuDialog.Builder(mInstance)
                                .addMenu("换票", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                        exchangeTicket(ticket_id);
                                    }
                                }).create();

                        mDialog.show();
                    }
                }, TicketAssistantAdapter.TYPE.AVAILABLE);
                mLvAvailable.setAdapter(mAvailableAdapter);
            } else {
                mAvailableAdapter.notifyDataSetChanged();
            }
        }

        if (mUsedCount > 0) {
            if (mUsedAdapter == null) {
                mUsedAdapter = new TicketAssistantAdapter(mInstance, mUsedList, new OnItemClickCallback() {
                    @Override
                    public void onItemClick(final String ticket_id) {

                    }
                }, TicketAssistantAdapter.TYPE.USED);
                mLvUsed.setAdapter(mUsedAdapter);
            } else {
                mUsedAdapter.notifyDataSetChanged();
            }
        }

        if (mInvalidCount > 0) {
            if (mInvalidAdapter == null) {
                mInvalidAdapter = new TicketAssistantAdapter(mInstance, mInvalidList, new OnItemClickCallback() {
                    @Override
                    public void onItemClick(String ticket_id) {
                    }
                }, TicketAssistantAdapter.TYPE.INVALID);
                mLvInvalid.setAdapter(mInvalidAdapter);
            } else {
                mInvalidAdapter.notifyDataSetChanged();
            }
        }
    }

    private void exchangeTicket(String ticket_id) {
        String token = (String) SPUtils.get(mInstance, SPUtils.TOKEN, "");
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(ticket_id)) {
            return;
        }
        OkGo.<String>post(exchangeUrl)
                .tag(mInstance)
                .params("tk", token)
                .params("ticket_id",ticket_id)
                .params("type",0)
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
                                            String num = mCouponChangeEditText.getText().toString();
                                            getMyTickets(num);
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

    private void changeLvVisibility(int currentTab, int count) {
        if (count == 0) {
            mLlEmptyView.setVisibility(View.VISIBLE);
            mLvAvailable.setVisibility(View.GONE);
            mLvUsed.setVisibility(View.GONE);
            mLvInvalid.setVisibility(View.GONE);
        } else {
            mLlEmptyView.setVisibility(View.GONE);
            mLvAvailable.setVisibility(currentTab == 0 ? View.VISIBLE : View.GONE);
            mLvUsed.setVisibility(currentTab == 1 ? View.VISIBLE : View.GONE);
            mLvInvalid.setVisibility(currentTab == 2 ? View.VISIBLE : View.GONE);
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            ((EditText) v).setCursorVisible(true);
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                ((EditText) v).setCursorVisible(false);
                return true;
            }
        }
        return false;
    }
}
