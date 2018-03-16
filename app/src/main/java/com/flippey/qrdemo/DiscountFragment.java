package com.flippey.qrdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/15.
 */
public class DiscountFragment extends Fragment {
    public static final String TAG = DiscountFragment.class.getSimpleName();

    private ListView mListView;
    private View mNoContentView;
    private List<Object> mList = new ArrayList();
    private TicketAssistantAdapter.TYPE mType = TicketAssistantAdapter.TYPE.AVAILABLE;

    private boolean mGettingData = false;
    private int mTotal;
    private int mPage;
    private View mView;
    private boolean mNeedRefresh = false;

    private OnItemClickCallback mCallback = new OnItemClickCallback() {
        @Override
        public void onItemClick(String url, boolean useWebview) {

        }

        @Override
        public void onItemClickByWebview(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
           /* if (url.startsWith(XidibuyConstants.getDomainWithSplit() + "detail/")) {
                Intent intent = new Intent(DiscountFragment.this.getActivity(),
                        ProductDetailActivity
                                .class);
                intent.putExtra(ProductDetailActivity.TAG, url);
                startActivityForResult(intent, XidibuyConstants.PRODUCT_DETAIL_REQUEST);
            } else {
                Intent intent = new Intent();
                intent.setClass(DiscountFragment.this.getActivity(), ShowByWebView.class);
                intent.putExtra(ShowByWebView.TAG, url);
                startActivityForResult(intent, XidibuyConstants.SHOW_BY_NEW_WEBVIEW_INT);
            }*/
        }
    };

    public DiscountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (mView == null || mNeedRefresh) {
            mView = inflater.inflate(R.layout.discount_layout, container, false);
            mListView = (ListView) mView.findViewById(R.id.lv_discount_show_page_items);

            mNoContentView = mView.findViewById(R.id.tv_discount_show_page_no_discount);

            getData(true);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int
                        visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount + 3 > totalItemCount && mList.size()
                            < mTotal + 1) {
                        getData(false);
                    }
                }
            });
        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showNoContentView(boolean show) {
        if (show) {
            if (mNoContentView != null) {
                mNoContentView.setVisibility(View.VISIBLE);
            }
            if (mListView != null) {
                mListView.setVisibility(View.GONE);
            }
        } else {
            if (mNoContentView != null) {
                mNoContentView.setVisibility(View.GONE);
            }
            if (mListView != null) {
                mListView.setVisibility(View.VISIBLE);
            }
        }
    }



    public void refreshListView() {
        mPage = 0;
        if (this.isVisible()) {
            getData(true);
        } else {
            mNeedRefresh = true;
        }
    }

    public TicketAssistantAdapter.TYPE getType() {
        return mType;
    }

    public void setType(TicketAssistantAdapter.TYPE mType) {
        this.mType = mType;
    }


    private void getData(final boolean first) {
        if (mGettingData) {
            return;
        }
        mNeedRefresh = false;

        mGettingData = true;
        StringBuilder param = new StringBuilder("?status=");
        if (getType() == TicketAssistantAdapter.TYPE.AVAILABLE) {
            param.append(1);
        } else if (getType() == TicketAssistantAdapter.TYPE.INVALID) {
            param.append(-1);
        } else if (getType() == TicketAssistantAdapter.TYPE.USED) {
            param.append(0);
        }
        param.append("&page=");
        param.append((mPage + 1));
        param.append("&t=");
        param.append(System.currentTimeMillis());
        /*MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest
                (DiscountFragment.this.getActivity(),
                        Request.Method.GET, XidibuyConstants.COUPON_ALL_GET_URL + param.toString
                        (), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(XidibuyConstants.CODE) == 0) {
                                showLoadingContent(false);
                                if (first) {
                                    mList.clear();
                                }

                                if (getType() == MyDiscountAdapter.TYPE.AVAILABLE) {
                                    Available te = Available.parse(response
                                            .getJSONObject(XidibuyConstants.DATA));
                                    mTotal = te.getTotal();
                                    mPage = te.getPage();
                                    for (Discount discount : te.getAvailableList()) {
                                        mList.add(discount);
                                    }
                                } else if (getType() == MyDiscountAdapter.TYPE.INVALID) {
                                    Invalid invalid = Invalid.parse(response.getJSONObject
                                            (XidibuyConstants.DATA));
                                    mTotal = invalid.getTotal();
                                    mPage = invalid.getPage();
                                    for (Discount discount : invalid.getInvalidList()) {
                                        mList.add(discount);
                                    }
                                } else if (getType() == MyDiscountAdapter.TYPE.USED) {
                                    AlreadyUsed used = AlreadyUsed.parse(response.getJSONObject
                                            (XidibuyConstants.DATA));
                                    mTotal = used.getTotal();
                                    mPage = used.getPage();
                                    for (Discount discount : used.getAlreadyUsedList()) {
                                        mList.add(discount);
                                    }
                                }
                                if (mList == null || mList.isEmpty()) {
                                    showNoContentView(true);
                                } else {
                                    showNoContentView(false);
                                    if (mList.size() >= mTotal) {
                                        if (getType() == MyDiscountAdapter.TYPE.AVAILABLE) {
                                            mList.add(getString(R.string.discount_tip_avaliable));
                                        } else if (getType() == MyDiscountAdapter.TYPE.INVALID) {
                                            mList.add(getString(R.string.discount_tip_invalid));
                                        } else {
                                            mList.add(getString(R.string.discount_tip_used));
                                        }
                                    }
                                    if (getActivity() != null) {
                                        if (mListView.getAdapter() == null) {
                                            mListView.setAdapter(new MyDiscountAdapter
                                                    (DiscountFragment.this.getActivity(),
                                                            mList, mCallback,
                                                            mType));
                                        } else {
                                            ((BaseAdapter) mListView.getAdapter())
                                                    .notifyDataSetChanged();
                                        }
                                        ((MyDiscountActivity) getActivity()).changeTitle(getType(),
                                                mTotal);
                                    }
                                }
                                mGettingData = false;
                            } else {
                                if (first) {
                                    getData(first);
                                }
                            }
                        } catch (JSONException e) {
                            XidibuyUtils.printLog(DiscountFragment.class
                                    .getSimpleName(), e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (first) {
                            getData(first);
                        }
                    }
                });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue
                (jsonObjectRequest);*/
    }
}
