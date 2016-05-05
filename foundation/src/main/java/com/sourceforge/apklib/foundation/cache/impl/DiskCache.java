package com.sourceforge.apklib.foundation.cache.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

import com.sourceforge.apklib.foundation.cache.Cacher;
import com.sourceforge.apklib.foundation.cache.DiskLruCache;
import com.sourceforge.apklib.foundation.utils.FilesystemUtils;
import com.sourceforge.apklib.foundation.utils.PhoneUtils;

/**
 * 磁盘缓存
 *
 */
public class DiskCache implements Cacher<String, InputStream> {
	private DiskLruCache mCacher;
	/**
	 * 默认放到应用目录的imageCache目录下,版本迭代后删除缓存,大小200M
	 */
	private static final long DISK_CACHE_SIZE=1024*1024*200;
	
	private static final int DISK_CACHE_INDEX=0;
	
	public DiskCache(Context context,String dir) {
		// 获取图片缓存路径
		File cacheDir = FilesystemUtils.getDiskCacheDir(context,dir);// getDiskCacheDir(context,
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		int version=PhoneUtils.getAppVersion(context);
		if (mCacher == null) {
			try {
				Long size=DISK_CACHE_SIZE;
				if(FilesystemUtils.getUsableSpace(cacheDir)<DISK_CACHE_SIZE){
					size=FilesystemUtils.getUsableSpace(cacheDir)/2;
				}
				mCacher = DiskLruCache.open(cacheDir, version, 1, size);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 初始化磁盘缓存
	 * 
	 * @param directory
	 *            存储目录
	 * @param appVersion
	 *            版本号，改变版本号后会删除所有缓存，一般不改变
	 * @param valueCount
	 *            单个节点所对应的数据的个数,一般为1
	 * @param maxSize
	 *            缓存总大小 bit
	 */
	public DiskCache(File directory, int appVersion, int valueCount, long maxSize) {
		if (mCacher == null) {
			try {
				if (!directory.exists()) {
					directory.mkdirs();
				}
				mCacher = DiskLruCache.open(directory, appVersion, valueCount, maxSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存记录,保存输入流中的内容,会关闭传入的输入流
	 */
	@Override
	public void storage(String key, InputStream in) {
		OutputStream out=null;
		try {
			DiskLruCache.Editor editor=mCacher.edit(key);
			if(editor!=null){
				out=editor.newOutputStream(DISK_CACHE_INDEX);
				if(FilesystemUtils.writeInputToOutput(in, out,1024*8)){
					editor.commit();
				}else{
					editor.abort();
				}
				mCacher.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			FilesystemUtils.close(out);
		}
	}

	@Override
	public InputStream recovery(String key) {
		try {
			DiskLruCache.Snapshot snapShot = mCacher.get(key);
			if (snapShot != null) {
				return snapShot.getInputStream(DISK_CACHE_INDEX);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将缓存记录同步到journal文件中。
	 */
	public void fluchCache() {
		if (mCacher != null) {
			try {
				mCacher.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
