package com.flippey.qrdemo;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flippey.qrdemo.bean.TicketListBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/2/29.
 */
public class TicketAssistantAdapter extends BaseAdapter {

    public enum TYPE {
        AVAILABLE,
        INVALID,
        USED;
    }

    private Context mCtx;
    private List<TicketListBean> mList;
    private SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
    private Typeface mTypeface;
    private OnItemClickCallback mCallback;
    private TYPE type;

    public TicketAssistantAdapter(Context ctx, List<TicketListBean> list, OnItemClickCallback callback, TYPE
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.coupon_item_me, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TicketListBean ticketListBean = mList.get(position);
        holder.title.setText(ticketListBean.getTicket_title());
        holder.limitBuy.setText("购买时间："+ticketListBean.getCreatetime());
        holder.expiryDate.setText("有效期至："+ticketListBean.getEnd_date_time());

        // 50元
        Double amount = ticketListBean.getPrice();
        holder.price.setText(String.valueOf(amount/100));

        if (type == TYPE.AVAILABLE) {
            holder.first.setBackgroundResource(R.drawable.cash_coupon_left_bg);
            holder.second.setBackgroundResource(R.drawable.cash_coupon_right_bg);

        } else if (type == TYPE.INVALID) {
            holder.ineffectiveTip.setText("已失效");
            holder.first.setBackgroundResource(R.drawable.unavailable_coupon_left_bg);
            holder.second.setBackgroundResource(R.drawable.unvalid_coupon_right_bg);
        } else if (type == TYPE.USED) {
            holder.ineffectiveTip.setText("已使用");
            holder.first.setBackgroundResource(R.drawable.unavailable_coupon_left_bg);
            holder.second.setBackgroundResource(R.drawable.used_coupon_right_bg);
        }
        return convertView;

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
        //TextView goTV;

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
            //this.goTV = (TextView) view.findViewById(R.id.tv_coupon_item_me_go);
        }
    }
}