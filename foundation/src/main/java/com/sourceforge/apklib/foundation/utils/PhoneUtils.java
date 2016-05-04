package com.sourceforge.apklib.foundation.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import java.io.File;

/**
 * 手机相关工具类
 */
public class PhoneUtils {
    /**
     * 网络链接类型 wifi
     */
    public static final int CONNECT_TYPE_WIFI = 0;
    /**
     * 网络链接类型mobile
     */
    public static final int CONNECT_TYPE_MOBILE = 1;

    /**
     * 网络链接类型,无连接
     */
    public static final int CONNECT_TYPE_NONE = 2;
    /**
     * 获取手机最大堆内存
     *
     * @param context
     * @return
     */
    public static final int getHeapMemorySize(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager.getMemoryClass();
    }
    /**
     * 获取系统版本
     */
    public static final  String getSystemVersion(){
        return android.os.Build.VERSION.RELEASE;
    }
    /**
     * 获取手机型号
     * @return
     */
    public static final String getMobileMode(){
        return android.os.Build.MODEL;
    }
    /**
     * 获取SDK版本
     * @return
     */
    public static final  int getSDKVersion(){
        return android.os.Build.VERSION.SDK_INT;
    }
    /**
     * 获取手机IMIE
     * @param context
     * @return
     */
    public static final String getIMIE(Context context){
        TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    /**
     * 执行软件安装
     */
    public static final void startInstallApk(Context ctx, File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        ctx.startActivity(i);
    }
    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static final int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取安装应用版本号
     * @param context
     * @return
     */
    public static final String getAppVersionName(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "-1";
    }
    /**
     * 获取应用最大可用内存
     *
     * @return
     */
    public static final long getApplicationMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }
    /**
     * 获取手机屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    public static final Point getScreenSize(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        point.set(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight());
        return point;
    }
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 判断网络是否可用
     *
     * @param ctx
     * @return
     */
    public static final boolean isNetworkConnected(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    /**
     * 获取网络链接类型
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final int connectType(Context context) {
        if (isNetworkConnected(context)) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                return CONNECT_TYPE_WIFI;
            }
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo.isConnected()) {
                return CONNECT_TYPE_MOBILE;
            }
        }
        return CONNECT_TYPE_NONE;
    }
    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public String getMacAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wm.getConnectionInfo();
        return info.getMacAddress();
    }
}
