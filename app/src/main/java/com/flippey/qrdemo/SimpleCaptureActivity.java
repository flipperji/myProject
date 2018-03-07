package com.flippey.qrdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.flippey.qrlib.CaptureActivity;


/**
 * Created by xdj on 16/9/17.
 */

public class SimpleCaptureActivity extends CaptureActivity {
    protected Activity mActivity = this;

    private AlertDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void handleResult(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(mActivity, "扫码失败", Toast.LENGTH_SHORT).show();
            restartPreview();
        } else {
            if (mDialog == null) {
                mDialog = new AlertDialog.Builder(mActivity)
                        .setMessage(resultString)
                        .setPositiveButton("确定", null)
                        .create();
                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        restartPreview();
                    }
                });
            }
            if (!mDialog.isShowing()) {
                mDialog.setMessage(resultString);
                mDialog.show();
            }
        }
    }
}
