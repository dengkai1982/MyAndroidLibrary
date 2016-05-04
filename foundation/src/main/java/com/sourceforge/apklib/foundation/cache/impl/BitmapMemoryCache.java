package com.sourceforge.apklib.foundation.cache.impl;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.sourceforge.apklib.foundation.cache.Cacher;

/**
 * 采用LruCache实现的图片内存缓存
 */
public class BitmapMemoryCache implements Cacher<String,Bitmap> {
	/*public static final LruCache<String, Bitmap> mCache;
	static{
		long maxMemory=Runtime.getRuntime().maxMemory();
		Long cacheSize=maxMemory/8;
		mCache=new LruCache<String,Bitmap>(cacheSize.intValue()){
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes()*bitmap.getHeight();
			}
		};
	}*/
	private LruCache<String, Bitmap> cache;

	public BitmapMemoryCache(){
		long maxMemory=Runtime.getRuntime().maxMemory();
		Long cacheSize=maxMemory/8;
		cache=new LruCache<String,Bitmap>(cacheSize.intValue()){
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes()*bitmap.getHeight();
			}
		};
	}

	@Override
	public void storage(String key, Bitmap t) {
		if(key!=null&&t!=null&&recovery(key)==null)
			cache.put(key, t);
	}

	@Override
	public Bitmap recovery(String key) {
		return cache.get(key);
	}

}
