package com.sourceforge.apklib.foundation.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * 基本Activity
 */
public abstract class BaseActivity extends Activity{
    private ActivitySession session;

    private static final String PREVIOUS_KEY="base_activity_previous_activity_name";


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        session=new ActivitySession();
        session.createTime=System.currentTimeMillis();
        session.previousActivity=getIntent().getStringExtra(PREVIOUS_KEY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        session.destoryTime=System.currentTimeMillis();
        session.currentActivity=getActivityName();
    }

    /**
     * 获取本activity的名称
     * @return
     */
    protected String getActivityName(){
        return this.getClass().getSimpleName();
    }

    @Override
    public void startActivity(Intent intent){
        intent.putExtra(PREVIOUS_KEY,getActivityName());
        this.startActivity(intent);
    }

    /**
     * 从资源中获取Drawable
     * @param resourceId
     * @return
     */
    public Drawable getResourceDrawable(int resourceId){
        return getResources().getDrawable(resourceId);
    }
    /**
     * 从资源中获取字符串
     * @param resourceId
     * @return
     */
    public String getResourceString(int resourceId){
        return getResources().getString(resourceId);
    }
    /**
     * 从资源中获取color
     * @param resourceId
     * @return
     */
    public int getResourceColor(int resourceId){
        return getResources().getColor(resourceId);
    }

    /**
     * 获取Activity会话
     * @return
     */
    public ActivitySession getActivitySession() {
        return session;
    }

    /**
     * 切换Fragment
     * @param fragment
     * @param contentId
     */
    public void changeFragment(Fragment fragment, int contentId){
        FragmentTransaction tran=getFragmentManager().beginTransaction();
        tran.replace(contentId, fragment);
        tran.commit();
    }
}
