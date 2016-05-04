package com.sourceforge.apklib.foundation.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 优化的Toast工具
 *
 */
public class ToastUtils {

    private static Toast toast = null;

    public static void showShortToast(Context context, int retId){
        if (toast == null) {
            toast = Toast.makeText(context, retId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(retId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }


        toast.show();
    }


    public static void showShortToastByString(Context context,String hint){
        if (toast == null) {
            toast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
        } else {
            toast.setText(hint);
            toast.setDuration(Toast.LENGTH_SHORT);
        }


        toast.show();
    }


    public static void showLongToast(Context context,int retId){
        if (toast == null) {
            toast = Toast.makeText(context, retId, Toast.LENGTH_LONG);
        } else {
            toast.setText(retId);
            toast.setDuration(Toast.LENGTH_LONG);
        }


        toast.show();
    }


    public static void showLongToastByString(Context context,String hint){
        if (toast == null) {
            toast = Toast.makeText(context, hint, Toast.LENGTH_LONG);
        } else {
            toast.setText(hint);
            toast.setDuration(Toast.LENGTH_LONG);
        }


        toast.show();
    }
}