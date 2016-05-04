package com.sourceforge.apklib.foundation.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.webkit.MimeTypeMap;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * 文件系统相关工具类
 */
public class FilesystemUtils {
    private static final Logger logger=Logger.getLogger(FilesystemUtils.class);
    /**
     * 关闭输入或输出流
     * @param c
     */
    public static final void close(Closeable c){
        if (c != null) {
            try {
                c.close();
                c=null;
            } catch (IOException e) {
                logger.e(e.getMessage(),e);
            }
        }
    }
    /**
     * 从输入流写到输出流
     * @param in 输入流
     * @param out 输出流
     * @param buffSize buff大小啊
     * @throws IOException
     */
    public static final boolean writeInputToOutput(InputStream in, OutputStream out,
        int buffSize){
        try{
            if(buffSize<=1024){
                buffSize=1024;
            }
            byte[] bytes=new byte[buffSize];
            int len=-1;
            while((len=in.read(bytes, 0,buffSize))!=-1){
                out.write(bytes, 0, len);
            }
            out.flush();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    /**
     * 获取本应用目录
     *
     * @param context
     * @return
     */
    public static final File getApplicationPath(Context context) {
        return context.getFilesDir();
    }
    /**
     * 获取本应用缓存目录
     *
     * @param context
     * @return
     */
    public static final File getCachePath(Context context) {
        return context.getCacheDir();
    }
    /**
     * 获取磁盘缓存路径,如果sdcard不存在,则在cache目录下创建, level>8
     *
     * @param context
     * @param uniqueName
     *            唯一标示,表示目录
     * @return
     */
    public static final File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    /**
     * 获取SDCard访问路径
     * @throws IOException 无SDCard或未挂载抛出异常
     */
    public static final File getSdcardPath() throws IOException {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            return dir;
        } else {
            throw new IOException("未挂载SDCard");
        }
    }

    /**
     * 获取系统图片库路径
     * @return
     */
    public static final File getGalleryPath(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取文件mimeType
     * @param file
     * @return
     */
    public static final String getMimeType(File file) {
        return getMimeType(file.getAbsolutePath());
    }
    /**
     * 获取文件mimetype
     *
     * @param filePath 文件路径
     * @return
     */
    public static final String getMimeType(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            String mime = "text/plain";
            if (filePath != null) {
                try {
                    mmr.setDataSource(filePath);
                    mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
                } catch (IllegalStateException e) {
                    return mime;
                } catch (IllegalArgumentException e) {
                    return mime;
                } catch (RuntimeException e) {
                    return mime;
                }
            }
            return mime;
        }else{
            String suffix = getSuffix(new File(filePath));
            if (suffix == null) {
                return "file/*";
            }
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
            if (type != null && !type.isEmpty()) {
                return type;
            }
            return "file/*";
        }
    }

    /**
     * 获取文件后缀名
     * @param file
     * @return
     */
    public static final String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.CHINA);
        } else {
            return null;
        }
    }
    /**
     * 返回指定路径下的文件空间可用大小
     *
     * @param path
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
