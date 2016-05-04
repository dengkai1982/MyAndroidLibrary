package com.sourceforge.apklib.foundation.activity;

import java.io.Serializable;

/**
 * Activity会话
 */
public class ActivitySession implements Serializable{
    /**
     * Activity创建时间
     */
    public long createTime;
    /**
     * Activity结束时间
     */
    public long destoryTime;
    /**
     * 前一个Activity名称
     */
    public String previousActivity;
    /**
     * 当前Activity名称
     */
    public String currentActivity;
}
