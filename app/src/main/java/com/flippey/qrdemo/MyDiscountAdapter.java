package com.flippey.qrdemo;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/2/29.
 */
public class MyDiscountAdapter extends BaseAdapter {

    protected enum TYPE {
        AVAILABLE,
        INVALID,
        USED;
    }

    private Context mCtx;
    private List<Object> mList;
    private SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
    private Typeface mTypeface;
    private OnItemClickCallback mCallback;
    private TYPE type;

    public MyDiscountAdapter(Context ctx, List<Object> list, OnItemClickCallback callback, TYPE
            type) {
        if (list == null) {
            mList = new ArrayList<>();
        } else {
            mList = list;
        }
        this.mCtx = ctx;
        this.mTypeface = Typeface.createFromAsset(mCtx.getAssets(), "fonts/xidi.ttf");
        if (callback != null) {
            this.mCallback = callback;
        }
        this.type = type;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object obj = mList.get(position);
        if (obj instanceof String) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.coupon_text_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_coupon_item_text);
            tv.setText(String.valueOf(obj));
            convertView.setTag(null);
            return convertView;
        } else {
            if (convertView == null || (convertView != null && convertView.getTag() == null)) {
                convertView = LayoutInflater.from(mCtx).inflate(R.layout.coupon_item_me, null);
            }
            ViewHolder holder = null;
            if (convertView.getTag() == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /*final Discount discount = (Discount) obj;
            holder.title.setText(discount.getVouchername());
            // 商品券
            holder.kind.setText(discount.getVouchercategory());
            // 50元
            String amount = discount.getVoucheramounts();
            if (!TextUtils.isEmpty(amount)) {
                if (amount.length() >= 4) {
                    holder.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                } else {
                    holder.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                }
                holder.price.setText(amount);
            }
            // limit
            holder.limitBuy.setText(discount.getVoucherdesc());
            if (TextUtils.isEmpty(discount.getUrl())) {
                holder.goTV.setVisibility(View.GONE);
                holder.view.setOnClickListener(null);
            } else {
                holder.goTV.setVisibility(View.VISIBLE);
                holder.goTV.setTypeface(mTypeface);
                holder.goTV.setText(XidiFonts.icon_triangle_right);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XidibuyUtils.printLog(MyDiscountAdapter.class.getSimpleName(), "url:" +
                                discount.getUrl());
                        if (mCallback != null && !TextUtils.isEmpty(discount.getUrl())) {
                            mCallback.onItemClickByWebview(discount.getUrl());
                        }
                    }
                });
            }
            if (type == TYPE.AVAILABLE) {
                if (TextUtils.isEmpty(discount.getUrl())) {
                    holder.first.setBackgroundResource(R.drawable.cash_coupon_left_bg);
                    holder.second.setBackgroundResource(R.drawable.cash_coupon_right_bg);
                } else {
                    holder.first.setBackgroundResource(R.drawable.goods_coupon_left_bg);
                    holder.second.setBackgroundResource(R.drawable.goods_coupon_right_bg);
                }
                long current = System.currentTimeMillis();
                Date start = new Date(discount.getStarttime());
                if (current > discount.getEndtime()) {
                    holder.ineffectiveTip.setText(R.string.invalid_tip2);
                } else {
                    // invalid_tip_time
                    long day = (discount.getEndtime() - current) / 3600 / 1000 / 24;
                    holder.ineffectiveTip.setText(String.format(mCtx.getString(R.string
                            .invalid_tip_time), day + 1));
                }
                holder.expiryDate.setText(String.format(mCtx.getString(R.string
                        .discount_valid_last_date)
                        , format.format(new Date(discount.getStarttime())), format.format(new Date
                        (discount.getEndtime()))));
            } else if (type == TYPE.INVALID) {
                holder.ineffectiveTip.setText(R.string.invalid_tip3);
                holder.expiryDate.setText(String.format(mCtx.getString(R.string
                        .invalid_tip4), format.format(new Date(discount.getEndtime()))));
                holder.first.setBackgroundResource(R.drawable.unavailable_coupon_left_bg);
                holder.second.setBackgroundResource(R.drawable.unvalid_coupon_right_bg);
            } else if (type == TYPE.USED) {
                holder.ineffectiveTip.setText(R.string.already_used_tip3);
                holder.expiryDate.setText(String.format(mCtx.getString(R.string
                        .already_used_tip4), discount.getUsageTime()));
                holder.first.setBackgroundResource(R.drawable.unavailable_coupon_left_bg);
                holder.second.setBackgroundResource(R.drawable.used_coupon_right_bg);
            }*/
            return convertView;
        }
    }

    static class ViewHolder {
        View view;
        View first;
        View second;
        TextView kind;
        TextView priceLabel;
        TextView price;
        TextView ineffectiveTip;
        TextView title;
        TextView limitBuy;
        TextView expiryDate;
        TextView goTV;

        public ViewHolder(View view) {
            this.view = view.findViewById(R.id.tv_coupon_item_me_view);
            this.first = view.findViewById(R.id.coupon_item_first);
            this.second = view.findViewById(R.id.coupon_item_second);
            this.kind = (TextView) view.findViewById(R.id.tv_coupon_item_me_kind);
            this.priceLabel = (TextView) view.findViewById(R.id.tv_coupon_item_me_price_label);
            this.price = (TextView) view.findViewById(R.id.tv_coupon_item_me_price);
            this.ineffectiveTip = (TextView) view.findViewById(R.id
                    .tv_coupon_item_me_ineffective_tip);
            this.title = (TextView) view.findViewById(R.id.tv_coupon_item_me_title);
            this.limitBuy = (TextView) view.findViewById(R.id.tv_coupon_item_me_limit_buy);
            this.expiryDate = (TextView) view.findViewById(R.id.tv_coupon_item_me_expiry_date);
            this.goTV = (TextView) view.findViewById(R.id.tv_coupon_item_me_go);
        }
    }
}