package com.flippey.qrdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flippey.qrdemo.R;

/**
 * 登录注册输入框
 * Created by tonycao on 2015/10/10.
 */
public class LoginRegisterInputBox extends RelativeLayout implements View.OnClickListener {
    static final String LOG_TAG = "LoginRegisterInputView";

    /**
     * root view
     */
    private LinearLayout mRootView;

    /**
     * 左边图标
     */
    private TextView mLeftIcon;

    /**
     * 输入区域
     */
    private EditText mInputArea;

    /**
     * 清除图标
     */
    //private TextView mCleanIcon;

    /**
     * 可视图标
     */
    //private TextView mEyeIcon;
    private boolean mShowing;

    /**
     * 下方提示
     */
    // private TextView mBelowTip;

    private Typeface mTypeface;

    public LoginRegisterInputBox(Context context) {
        super(context);
        init(null, 0);
    }

    public LoginRegisterInputBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoginRegisterInputBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * 初始化
     *
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {
        // load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.LoginRegisterInputBox, defStyle, 0);
        Drawable rootViewBg = a.getDrawable(R.styleable.LoginRegisterInputBox_rootView_bg);
        int leftIconVisibility = a.getInteger(R.styleable.LoginRegisterInputBox_leftIcon_visibility, 1);
        String inputAreaHint = a.getString(R.styleable.LoginRegisterInputBox_inputArea_hint);
        int inputAreaHintColor = a.getColor(R.styleable.LoginRegisterInputBox_inputArea_hintColor,
                getResources().getColor(R.color.login_register_inputarea_hint_color));
        /*int inputAreaTextSize = a.getDimensionPixelSize(R.styleable.LoginRegisterInputBox_inputArea_textSize,
                (int) (15 * XidibuyUtils.getScaledDensity()));*/
        int inputAreaTextColor = a.getColor(R.styleable.LoginRegisterInputBox_inputArea_textColor,
                getResources().getColor(R.color.login_register_inputarea_text_color));
        int inputAreaMaxLength = a.getInteger(R.styleable.LoginRegisterInputBox_inputArea_maxLength,
                Integer.MAX_VALUE);
        int inputAreaInputType = a.getInteger(R.styleable.LoginRegisterInputBox_inputArea_inputType, 0);
        int cleanIconVisibility = a.getInteger(R.styleable.LoginRegisterInputBox_cleanIcon_visibility, 1);
        int eyeIconVisibility = a.getInteger(R.styleable.LoginRegisterInputBox_eyeIcon_visibility, 1);
        // String belowTipText = a.getString(R.styleable.LoginRegisterInputBox_belowTip_text);
        a.recycle();

        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/xidi.ttf");

        // init view
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.login_register_inputbox_layout, this, true);

        mRootView = (LinearLayout) findViewById(R.id.ll_login_register_inputbox_rootview);
        if (rootViewBg != null) {
            mRootView.setBackground(rootViewBg);
        }
        // mRootView.setElevation(3);
        // mRootView.setTranslationZ(3);

        mLeftIcon = (TextView) findViewById(R.id.tv_login_register_inputbox_lefticon);
        mLeftIcon.setTypeface(mTypeface);
        if (leftIconVisibility == 0) {
            mLeftIcon.setVisibility(View.VISIBLE);
        } else if (leftIconVisibility == 1) {
            mLeftIcon.setVisibility(View.GONE);
        }

        mInputArea = (EditText) findViewById(R.id.et_login_register_inputbox_inputarea);
        mInputArea.setHint(inputAreaHint);
        mInputArea.setHintTextColor(inputAreaHintColor);
        // mInputArea.setTextSize(inputAreaTextSize / XidibuyUtils.getScaledDensity());
        mInputArea.setTextColor(inputAreaTextColor);
        mInputArea.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputAreaMaxLength)});
        if (inputAreaInputType == 1) {
            mInputArea.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (inputAreaInputType == 2) {
            mInputArea.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (inputAreaInputType == 3) {
            mInputArea.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mInputArea.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
       /* mInputArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    mCleanIcon.setVisibility(View.GONE);
                    // mBelowTip.setVisibility(View.GONE);
                } else {
                    mCleanIcon.setVisibility(View.VISIBLE);
                    // mBelowTip.setVisibility(View.VISIBLE);
                }
            }
        });
         mInputArea.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {    // 失去焦点
                    mCleanIcon.setVisibility(View.GONE);
                    // mBelowTip.setVisibility(View.GONE);
                } else {    // 获取焦点
                    if (!mInputArea.getText().toString().equals("")) {
                        mCleanIcon.setVisibility(View.VISIBLE);
                        // mBelowTip.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

       mCleanIcon = (TextView) findViewById(R.id.tv_login_register_inputbox_cleanicon);
        if (inputAreaInputType == 2) {
            mCleanIcon.setPadding((int) (7.5 * XidibuyUtils.getDensity()), 0,
                    (int) (7.5 * XidibuyUtils.getDensity()), 0);
        } else {
            mCleanIcon.setWidth((int) getResources().getDimension(
                    R.dimen.login_register_inputbox_righticon_width));
        }
        mCleanIcon.setTypeface(mTypeface);
        mCleanIcon.setText(XidiFonts.icon_unfinished);
        if (cleanIconVisibility == 0) {
            mCleanIcon.setVisibility(View.VISIBLE);
        } else if (cleanIconVisibility == 1) {
            mCleanIcon.setVisibility(View.GONE);
        }
        mCleanIcon.setOnClickListener(this);

        mEyeIcon = (TextView) findViewById(R.id.tv_login_register_inputbox_eyeicon);
        mEyeIcon.setTypeface(mTypeface);
        mEyeIcon.setText(XidiFonts.icon_password_hide);
        if (eyeIconVisibility == 0) {
            mEyeIcon.setVisibility(View.VISIBLE);
        } else if (eyeIconVisibility == 1) {
            mEyeIcon.setVisibility(View.GONE);
        }
        mEyeIcon.setOnClickListener(this);*/

        // mBelowTip = (TextView) findViewById(R.id.tv_login_register_inputbox_below_tip);
        // mBelowTip.setText(belowTipText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_register_inputbox_cleanicon:
                mInputArea.setText("");
                mInputArea.requestFocus();
                //mCleanIcon.setVisibility(View.GONE);
                break;
            case R.id.tv_login_register_inputbox_eyeicon:
                if (mShowing) {
                    // 设置EditText文本为隐藏的
                    mInputArea.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //mEyeIcon.setText(XidiFonts.icon_password_hide);
                    //mEyeIcon.setTextColor(Color.parseColor("#C5C5C5"));
                } else {
                    // 设置EditText文本为可见的
                    mInputArea.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //mEyeIcon.setText(XidiFonts.icon_password_show);
                    //mEyeIcon.setTextColor(Color.parseColor("#00CAAF"));
                }
                mShowing = !mShowing;
                mInputArea.postInvalidate();
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = mInputArea.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                mInputArea.requestFocus();
                break;
        }
    }
}