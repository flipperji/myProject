package com.flippey.qrdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by admin on 2016/1/21.
 */
public class MyDiscountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ALLDISCOUNT_GET_SUCCESS = 1;

    private View mBackTextView;
    private EditText mCouponChangeEditText;
    private TextView mCouponChangeBtn;

    private TextView mAvailableTextView;
    private TextView mAlreadyUsedTextView;
    private TextView mInValidTextView;
    private DiscountFragment mAvailableFragment;
    private DiscountFragment mInvalidFragment;
    private DiscountFragment mUsedFragment;
    private ImageView mIndicateImageView;

    private int deviceWidth = Util.getDeviceWidth();
    private int padding = (deviceWidth / 3 - (int) (90 * Util.getDensity())) / 2;
    private int offset = (deviceWidth / 3 - (int) (90 * Util.getDensity())) / 2;

    private OnItemClickCallback mCallback;

    private int mAvailableCount = 0;
    private int mInvalidCount = 0;
    private int mUsedCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_list_layout);

        initView();
        getMyAllCoupons();
    }

    // 初始化UI界面。
    private void initView() {
        mBackTextView = findViewById(R.id.coupon_title_back);

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDiscountActivity.this.finish();
            }
        });


        mCouponChangeEditText = (EditText) findViewById(R.id.coupon_number_edittext);
        mCouponChangeBtn = (TextView) findViewById(R.id.coupon_number_btn);

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

        mAvailableTextView.setOnClickListener(MyDiscountActivity.this);
        mAlreadyUsedTextView.setOnClickListener(MyDiscountActivity.this);
        mInValidTextView.setOnClickListener(MyDiscountActivity.this);

        if (mAvailableFragment == null) {
            mAvailableFragment = new DiscountFragment();
            mAvailableFragment.setType(MyDiscountAdapter.TYPE.AVAILABLE);
        }
        if (mInvalidFragment == null) {
            mInvalidFragment = new DiscountFragment();
            mInvalidFragment.setType(MyDiscountAdapter.TYPE.INVALID);
        }
        if (mUsedFragment == null) {
            mUsedFragment = new DiscountFragment();
            mUsedFragment.setType(MyDiscountAdapter.TYPE.USED);
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.discount_framelayout, mAvailableFragment);
        transaction.commit();
        changeTranslate(0);
        changeColor(0);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (v == mAvailableTextView) {
            transaction.replace(R.id.discount_framelayout, mAvailableFragment);
        } else if (v == mAlreadyUsedTextView) {
            transaction.replace(R.id.discount_framelayout, mUsedFragment);
        } else if (v == mInValidTextView) {
            transaction.replace(R.id.discount_framelayout, mInvalidFragment);
        }
        transaction.commit();
        changeColor((Integer) v.getTag());
        changeTranslate((Integer) v.getTag());
    }

    private void changeColor(int id) {
        mAvailableTextView.setTextColor(Color.BLACK);
        mAlreadyUsedTextView.setTextColor(Color.BLACK);
        mInValidTextView.setTextColor(Color.BLACK);
        if (id == 0) {
            mAvailableTextView.setTextColor(Color.rgb(0, 190, 191));
        } else if (id == 1) {
            mAlreadyUsedTextView.setTextColor(Color.rgb(0, 190, 191));
        } else if (id == 2) {
            mInValidTextView.setTextColor(Color.rgb(0, 190, 191));
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

    public void changeTitle(MyDiscountAdapter.TYPE type, int count) {
        if (type == MyDiscountAdapter.TYPE.AVAILABLE) {
            mAvailableCount = count;
            mAvailableTextView.setText(String.format(getString(R.string.available_tip), count));
        } else if (type == MyDiscountAdapter.TYPE.INVALID) {
            mInvalidCount = count;
            mInValidTextView.setText(String.format(getString(R.string.invalid_tip), count));
        } else {
            mUsedCount = count;
            mAlreadyUsedTextView.setText(String.format(getString(R.string.already_used_tip),
                    count));
        }
    }


    // 获取全部优惠券列表接口
    private void getMyAllCoupons() {
        /*VolleySingleton volley = VolleySingleton.getInstance(MyDiscountActivity.this);
        RequestQueue queue = volley.getRequestQueue();
        MyJsonObjectRequest request = new MyJsonObjectRequest(MyDiscountActivity.this,
                Request.Method.GET, XidibuyConstants.COUPON_ALL_GET_URL + "?t="
                + System.currentTimeMillis(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (jsonObject != JSONObject.NULL) {
                            OriginParse parse = OriginParse.parse(jsonObject);
                            if (parse.getCode() == XidibuyConstants.GET_RETURN_SUCCESS) {
                                String data = parse.getData();
                                if (!TextUtils.isEmpty(data)) {
                                    XidibuyUtils.printLog(MyDiscountActivity.class.getSimpleName
                                            (), data);
                                    AllDiscounts mAllDiscounts = AllDiscounts.parseAllDiscounts
                                            (data);
                                    changeTitle(MyDiscountAdapter.TYPE.AVAILABLE, mAllDiscounts
                                            .getAvaibleTotal());
                                    changeTitle(MyDiscountAdapter.TYPE.INVALID, mAllDiscounts
                                            .getInvalidTotal());
                                    changeTitle(MyDiscountAdapter.TYPE.USED, mAllDiscounts
                                            .getAlreadyuserdTotal());
                                }
                            } else {
                                getMyAllCoupons();
                            }
                        } else {
                            getMyAllCoupons();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getMyAllCoupons();
            }
        });
        request.setPriority(Request.Priority.IMMEDIATE);
        queue.add(request);*/
    }
}
