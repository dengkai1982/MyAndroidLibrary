package com.sourceforge.apklib.foundation.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

/**
 * 图片相关工具类
 */
public class ImageUtils {
    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
    /**
     * 以最佳采样率加载图片
     * @param filePath 文件路径
     * @param reqWidth 请求加载的图片宽度,即ImageView的宽度
     * @param reqHeight 请求加载的图片高度,即ImageView的高度
     * @return
     */
    public static Bitmap decodeSampledBitmap(String filePath,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize=computerInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(filePath,options);
    }

    /**
     * 以最佳采样率加载图片
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmap(FileDescriptor fd,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize=computerInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }
    /**
     * 以最佳采样率从资源文件中加载图片
     * @param res
     * @param resId
     * @param reqWidth 请求加载的图片宽度,即ImageView的宽度
     * @param reqHeight 请求加载的图片高度,即ImageView的高度
     * @return
     */
    public static Bitmap decodeSampledBitmap(Resources res,int resId,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res, resId,options);
        options.inSampleSize=computerInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res, resId,options);
    }

    /**
     * 计算bitmap大小
     * @param bitmap
     * @return
     */
    public static final int computerBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    /**
     * 计算请求图片的最佳采样率
     * @param options
     * @param reqWidth 请求加载的图片宽度,即ImageView的宽度
     * @param reqHeight 请求加载的图片高度,即ImageView的高度
     * @return
     */
    public static final int computerInSampleSize(BitmapFactory.Options options,
            int reqWidth,int reqHeight){
        int inSampleSize=1;
        if(reqWidth<=0||reqHeight<=0){
            return inSampleSize;
        }
        final int width=options.outWidth;
        final int height=options.outHeight;
        if(height>reqHeight||width>reqWidth){
            final int halfHeight=height/2;
            final int halfWidth=width/2;
            while((halfHeight/inSampleSize)>=reqHeight
                    &&(halfWidth/inSampleSize)>=reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
    /**
     * 构建存储到DCIM目录下的图片文件
     *
     * @param dirs
     *            文件夹 string[]{"/android","/image","/mypicture"}
     * @param fileName
     *            文件名
     * @return
     */
    public static final Uri generatorSaveImageToDCIM(String[] dirs, String fileName) {
        StringBuilder dir = new StringBuilder();
        for (String d : dirs) {
            dir.append("/").append(d);
        }
        File outputImage = new File(FilesystemUtils.getGalleryPath(), dir + "/" + fileName);
        return Uri.fromFile(outputImage);
    }
    /**
     * 返回调用系统拍照的Intent
     * @param saveUri 保存文件的uri
     * @return
     */
    public static final Intent getSystemCaptureIntent(Uri saveUri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,saveUri);
        return intent;
    }
    /**
     * 返回打开系统自带的图片浏览器的Intent
     * @param file
     */
    public static final Intent callSystemImageBorwser(File file) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        return intent;
    }
    /**
     * 调用系统进行图片剪裁,返回Intent
     * @param imageUri 剪裁后图片存储uri
     * @param aspectX 宽高比
     * @param aspectY 宽高比
     * @param outputX 输出大小
     * @param outputY 输出大小
     * @param reflush 是否广播刷新图库
     * @param activity 广播刷新图库的activity
     * @return
     */
    public static Intent getCropPhoto(Uri imageUri,int aspectX,int aspectY,
            int outputX,int outputY,boolean reflush,Context activity){
        Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("scale", true);
        //设置宽高比例
        intent.putExtra("aspectX",aspectX);
        intent.putExtra("aspectY",aspectY);
        //设置裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //广播刷新相册
        if(reflush){
            Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intentBc.setData(imageUri);
            activity.sendBroadcast(intentBc);
        }
        return intent;
    }
    /**
     * 获取剪裁后的图片
     * @return
     */
    public static Bitmap getCropPhotoForResult(Uri imageUri,Context activity)
        throws FileNotFoundException{
        Bitmap bitmap= BitmapFactory.decodeStream(
                activity.getContentResolver().openInputStream(imageUri));
        return bitmap;
    }
}
