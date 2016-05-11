package com.sourceforge.apklib.foundation.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourceforge.apklib.foundation.R;
import com.sourceforge.apklib.foundation.utils.ScreenUtils;

import java.util.Random;

/*
com.sourceforge.apklib.foundation.ui.widget.BaseToolbar
 */
public class BaseToolbar extends RelativeLayout{
    private RippleRelativeLayout navigationIcon;
    private TextView title;
    private LinearLayout menusContainer;
    private ImageView navigationImage;
    private boolean titleToLeft;
    public BaseToolbar(Context context){
        this(context,null);
    }

    public BaseToolbar(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public BaseToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addNavigationIcon(context,attrs);
        addTitle(context);
    }
    private void addTitle(Context context){
        title=new TextView(context);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        title.setTextColor(0xffffffff);
        title.setTextSize(18);
        addView(title);
        titleToLeft=false;
    }
    private void addNavigationIcon(Context context,AttributeSet attrs){
        navigationIcon=new RippleRelativeLayout(context,attrs);
        navigationIcon.setRippleDuration(150);
        navigationIcon.setRippleColor(0xffe0e0e0);
        addView(navigationIcon);
        navigationImage=new ImageView(context);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        int imageSize= ScreenUtils.dip2px(context,32);
        lp.width=imageSize;
        lp.height=imageSize;
        navigationImage.setLayoutParams(lp);
        navigationImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_arrow_left));
        navigationIcon.addView(navigationImage);
    }

    /**
     * 设置title为左对齐
     */
    public void setTitleAlignLeft(){
        titleToLeft=true;
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        this.title.setText(title);
    }

    /**
     * 设置兼容方式的阴影
     * @param elevation
     */
    public void setCompatibleElevation(int elevation){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(elevation);
        } else {
            ViewCompat.setElevation(this, elevation);
        }
    }

    /**
     * 获取导航按钮
     * @return
     */
    public RippleRelativeLayout getNavigationIcon(){
        return this.navigationIcon;
    }

    /**
     * 设置导航图片
     * @param bitmap
     */
    public void setNavigationImage(Bitmap bitmap){
        this.navigationImage.setImageBitmap(bitmap);
    }

    /**
     * 设置导航图片
     * @param drawable
     */
    public void setNavigationImage(Drawable drawable){
        this.navigationImage.setImageDrawable(drawable);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = r - l;
        int height = b - t;
        navigationIcon.getLayoutParams().width = height;
        navigationIcon.getLayoutParams().height = height;
        if (titleToLeft) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) title.getLayoutParams();
            height = ScreenUtils.dip2px(getContext(), height+10);
            lp.leftMargin = navigationIcon.getWidth();
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
        }
    }
}
