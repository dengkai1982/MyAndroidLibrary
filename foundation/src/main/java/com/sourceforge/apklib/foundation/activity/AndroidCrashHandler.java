package com.sourceforge.apklib.foundation.activity;

import android.content.Context;

import java.io.Serializable;

/**
 * Android 未处理异常捕获
 * 使用说明
 * 需要在Application的onCreate方法中对改类进行初始化，并调用init方法
 * 修改AndroidManifest.xml
 * <application
     android:icon="@drawable/ic_launcher"
     android:label="@string/app_name"
     android:name=".OurApplication"
     android:debuggable="true"
    >
 *
 */
public class AndroidCrashHandler implements Thread.UncaughtExceptionHandler,Serializable {

    private static final long serialVersionUID = 8976791190609499659L;

    private Context context;
    private AndroidCrashHandler(){

    }

    static class AndroidCrashHandlerHolder{
        private static final AndroidCrashHandler _instance=new AndroidCrashHandler();
    }
    public static final AndroidCrashHandler getInstance(){
        return AndroidCrashHandlerHolder._instance;
    }
    private UncaughtExceptionHandler handler;
    /**
     * 初始化，把当前对象设置成UncaughtExceptionHandler处理器
     */
    public void init(Context ctx,UncaughtExceptionHandler handler){
        this.context=ctx;//拿到全局的Context
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.handler=handler;
    }

    public interface UncaughtExceptionHandler{
        void uncaughtException(Context ctx,Thread thread, Throwable ex);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(this.handler!=null){
            handler.uncaughtException(context,thread,ex);
        }
    }
}
