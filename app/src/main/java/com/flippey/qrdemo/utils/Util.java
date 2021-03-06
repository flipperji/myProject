package com.flippey.qrdemo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.flippey.qrdemo.R;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by flippey on 2018/3/12 16:43.
 */

public class Util {
    public enum IMAGE_SIZE {
        FULL_SIZE,
        HALF_SIZE,
        ONETHIRD_SIZE;
    }

    private static String deviceInfo;
    private static boolean isLogin = false;
    private static final String TAG = "XidibuyUtils";
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String regex = "((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})" +
            "(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|((m|www|m-beta).[a-zA-Z0-9\\" +
            ".\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";
    private static Pattern pattern = Pattern.compile(regex);

    private static LinkedList<Activity> activities = new LinkedList<Activity>();

    private static String fullScaleTail;
    private static String halfScaleTail;
    private static String oneThirdScaleTail;

    private static int unreadMsgNum;

    private static int deviceHeight = -1;
    private static int deviceWidth = -1;
    private static float density = 1.0f;

    // 购物车数量。
    private static int mShoppingCount = 0;
    private static int mValidShoppingCount = 0;
    private static int mUnvalidShoppingCount = 0;

    private static HashMap<String, String> paySuccessOrder = new HashMap<String, String>();

    private static boolean mAliAvailable;
    private static boolean mWeChatAvailable;

    private static File bufferFile;


    private static int mSize;
    private static boolean page_welcome_show;

    private static boolean from_refund_apply;

    public static boolean getFrom_refund_apply() {
        return from_refund_apply;
    }


    public static LinkedList<Activity> getActivities() {
        return activities;
    }

    private static ExecutorService mThreadPool = null;

    /**
     * @return device density
     */
    public static float getDensity() {
        return density;
    }

    /**
     * @return device Height with pixel
     */
    public static int getDeviceHeight() {
        return deviceHeight;
    }

    /**
     * @return device Width with pixel
     */
    public static int getDeviceWidth() {
        return deviceWidth;
    }



    public static void init(Context ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        deviceHeight = metric.heightPixels;
        deviceWidth = metric.widthPixels;
        density = metric.density;


        int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
        mThreadPool = Executors.newFixedThreadPool(THREAD_COUNT * 2);

        mSize = (int) (Runtime.getRuntime().totalMemory() / 1024f / 1024);

        // 初始化 buffer directory
        bufferFile = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (bufferFile == null || !bufferFile.exists() || !bufferFile.canWrite()) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && Environment.getExternalStorageDirectory().canWrite()) {
                // 安装sdcard，
                bufferFile = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/xidibuy");
            } else {
                // 未安装sdcard，
                bufferFile = new File(Environment.getDownloadCacheDirectory()
                        .getAbsolutePath() + "/xidibuy");
            }
        }
        if (bufferFile.exists() && !bufferFile.isDirectory()) {
            bufferFile.delete();
        }
        if (!bufferFile.exists()) {
            bufferFile.mkdirs();
        }
    }

    public static void addActivity(Activity act) {
        if (act != null && !act.isFinishing()) {
            activities.add(act);
        }
    }




    private static final String getMd5(String str) {

        try {
            MessageDigest mD = MessageDigest.getInstance("MD5");
            mD.update(str.getBytes());

            byte[] temp = mD.digest();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < temp.length; i++) {
                builder.append(HEX_DIGITS[(temp[i] >> 4) & 0x0f]);
                builder.append(HEX_DIGITS[temp[i] & 0x0f]);
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {

        }

        return null;
    }

    public static boolean isLogin() {
        return isLogin;
    }


    /**
     * @param diameter crop diameter
     * @param color    crop color
     */
    public static Bitmap getCroppedBitmap(int diameter, int color) {
        if (diameter <= 0) {
            diameter = 60;
        }
        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);

        Rect rect = new Rect(0, 0, diameter, diameter);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        int radius = diameter / 2;
        canvas.drawCircle(radius, radius, Math.max(radius, radius), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return output;
    }

    /**
     * @param diameter crop diameter
     * @param color    crop color
     */
    public static BitmapDrawable getCroppedBitmapDrawable(Context ctx, int diameter, int color) {
        if (diameter <= 0) {
            diameter = 60;
        }
        try {
            Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
            Rect rect = new Rect(0, 0, diameter, diameter);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);

            int radius = diameter / 2;
            canvas.drawCircle(radius, radius, Math.max(radius, radius), paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(output, rect, rect, paint);

            return new BitmapDrawable(ctx.getResources(), output);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static void showToast(Context context, String text) {
        final Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_textview);
        if (tv != null) {
            tv.setText(text);
        }
        toast.setView(view);
        toast.show();
        tv.getRootView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                toast.cancel();
                return false;
            }
        });
    }


    /**
     * 校验是否是手机号
     *
     * @param num
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPhoneNum(String num) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(num);
        return m.matches();
    }


    public static String addSymble(String price) {
        if (TextUtils.isEmpty(price)) return "";
        if (Character.isDigit(price.charAt(0))) {
            price = " ¥ " + price;
            return price;
        } else if (price.contains("￥")) {
            price = price.replace("￥", " ¥ ");
            return price;
        }
        return price;
    }


    /**
     * 判断当前网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
