package com.sourceforge.apklib.foundation.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Size;

import java.util.regex.Pattern;

/**
 * 颜色字体工具
 */
public class FontColorUtils {
    /**
     * 将十六进制 颜色代码 转换为 int
     *
     * @return
     */
    public static final int HextoColor(String color) {

        // #ff00CCFF
        String reg = "#[a-f0-9A-F]{8}";
        if (!Pattern.matches(reg, color)) {
            color = "#00ffffff";
        }

        return Color.parseColor(color);
    }
    /**
     * 修改颜色透明度
     * @param color
     * @param alpha
     * @return
     */
    public static final int changeColorAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 获取哦文本
     * @param displayText
     * @return
     */
    public static final float getFontWidth(String displayText){
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        return mTextPaint.measureText(displayText);
    }
    /**
     * 获取文本宽高
     * @param text
     * @return
     */
    public static final Rect getFontSize(String text){
        Paint pFont = new Paint();
        Rect rect = new Rect();

        //返回包围整个字符串的最小的一个Rect区域
        pFont.getTextBounds(text, 0, 1, rect);
        return rect;
    }
}
