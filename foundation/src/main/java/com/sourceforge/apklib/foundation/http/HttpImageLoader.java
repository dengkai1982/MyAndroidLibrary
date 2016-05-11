package com.sourceforge.apklib.foundation.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.sourceforge.apklib.foundation.cache.impl.BitmapMemoryCache;
import com.sourceforge.apklib.foundation.cache.impl.DiskCache;
import com.sourceforge.apklib.foundation.http.request.HttpGet;
import com.sourceforge.apklib.foundation.utils.CoderUtils;
import com.sourceforge.apklib.foundation.utils.FilesystemUtils;
import com.sourceforge.apklib.foundation.utils.ImageUtils;
import com.sourceforge.apklib.foundation.utils.Logger;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Http文件下载
 */
public class HttpImageLoader {
    private static final Logger logger=Logger.getLogger(HttpImageLoader.class);
    //http执行器
    private HttpRequestExecutor httpExecutor;
    //本地缓存
    private DiskCache diskCacher;
    //内存缓存
    private BitmapMemoryCache memoryCache;

    private int UN_REQ_SIZE=-1;


    public HttpImageLoader(Context context, HttpRequestExecutor httpExecutor) {
        this.httpExecutor = httpExecutor;
        this.diskCacher = new DiskCache(context,"image_cache");
        this.memoryCache = new BitmapMemoryCache();
    }

    private class ImageRequestListener implements HttpRequestListener<Bitmap>{
        private String key;
        private View container;
        private ImageView imageView;
        private int reqWidth;
        private int reqHeight;
        private boolean isForTag;

        private ImageRequestListener(String key,int reqWidth,int reqHeight){
            this.key=key;
            this.reqHeight=reqHeight;
            this.reqWidth=reqWidth;
        }

        public ImageRequestListener(View container,String key,int reqWidth,int reqHeight){
            this(key,reqWidth,reqHeight);
            this.container=container;
            this.isForTag=true;
        }

        public ImageRequestListener(ImageView imageView,String key,int reqWidth,int reqHeight){
            this(key,reqWidth,reqHeight);
            this.imageView=imageView;
            this.isForTag=false;
        }

        @Override
        public void onThrowException(HttpRequest request, Throwable e) {
            logger.e(e.getMessage(),e);
        }

        @Override
        public void onMessageReceived(HttpRequest request, HttpResponse response, Bitmap bitmap) {
            if(isForTag){
                showBitmapToImageView(key,container,bitmap);
            }else{
                showBitmapToImageView(imageView,bitmap);
            }
        }

        @Override
        public Bitmap onParseMessage(InputStream in, String charset) {
            diskCacher.storage(key,in);
            return loadImageForDiskCacher(key,reqWidth,reqHeight);
        }
    }

    private void showBitmapToImageView(ImageView imageView,Bitmap bitmap){
        //直接设置imageView
        imageView.setImageBitmap(bitmap);
    }

    private void showBitmapToImageView(String key,View container,Bitmap bitmap){
        //从tag中设置图片
        ImageView imageView=(ImageView) container.findViewWithTag(key);
        if(imageView!=null){
            imageView.setImageBitmap(bitmap);
        }
    }

    private void loadImageForUrl(View container,ImageView imageView,String url,int reqWidth,int reqHeight){
        //首先从缓存加载,如果缓存中有,则不需要发送http请求
        String key= CoderUtils.MD5UseCache(url);
        Bitmap bitmap=loadImageForCacher(key,reqWidth,reqHeight);
        if(bitmap!=null){
            if(container!=null){
                //container不为空，则从tag中查找
                showBitmapToImageView(key,container,bitmap);
            }else{
                showBitmapToImageView(imageView,bitmap);
            }
        }else{
            ImageRequestListener listener=null;
            if(container!=null){
                listener=new ImageRequestListener(container,key,reqWidth,reqHeight);
            }else{
                listener=new ImageRequestListener(imageView,key,reqWidth,reqHeight);
            }
            HttpGet httpGet=new HttpGet(listener);
            httpGet.setUrl(url);
            httpExecutor.doRequest(httpGet);
        }
    }

    private Bitmap loadImageForCacher(String key,int reqWidth,int reqHeight){
        Bitmap bitmap=null;
        bitmap=memoryCache.recovery(key);
        if(bitmap==null){
           return loadImageForDiskCacher(key,reqWidth,reqHeight);
        }
        return bitmap;
    }

    private Bitmap loadImageForDiskCacher(String key,int reqWidth,int reqHeight){
        Bitmap bitmap=null;
        FileInputStream in=null;
        FileDescriptor fd=null;
        try {
            in= (FileInputStream) diskCacher.recovery(key);
                if(in!=null){
                    fd=in.getFD();
                }
                if(fd!=null){
                    if(reqWidth!=UN_REQ_SIZE&&reqHeight!=UN_REQ_SIZE){
                        bitmap= ImageUtils.decodeSampledBitmap(fd, reqWidth, reqHeight);
                    }else{
                        bitmap= BitmapFactory.decodeFileDescriptor(fd);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
        }finally {
            FilesystemUtils.close(in);
        }
        if(bitmap!=null){
            memoryCache.storage(key,bitmap);
        }
        return bitmap;
    }

    /**
     * 从指定的url加载图片
     * @param imageView
     * @param url
     */
    public void loadImage(ImageView imageView,String url){
        loadImage(imageView,url,UN_REQ_SIZE,UN_REQ_SIZE);
    }

    /**
     * 从指定的url加载图片
     * @param imageView
     * @param url
     * @param reqWidth 图片请求宽度
     * @param reqHeight 图片请求高度
     */
    public void loadImage(ImageView imageView,String url,int reqWidth,int reqHeight){
        loadImageForUrl(null,imageView,url,reqWidth,reqHeight);
    }

    /**
     * 请求图片并通过tag加载,tag请在调用此方法前设置好
     * @param container
     * @param url
     */
    public void loadImageForTag(View container,String url){
        loadImageForTag(container,url,UN_REQ_SIZE,UN_REQ_SIZE);
    }

    /**
     * 请求图片并通过tag加载,tag请在调用此方法前设置好
     * @param container
     * @param url
     * @param reqWidth
     * @param reqHeight
     */
    public void loadImageForTag(View container,String url,int reqWidth,int reqHeight){
        loadImageForUrl(container,null,url,reqWidth,reqHeight);
    }
}
