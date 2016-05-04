package com.sourceforge.apklib.foundation.cache;
/**
 * 缓存 
 *
 */
public interface Cacher<K,V> {
	/**
	 * 进行缓存
	 * @param key 缓存的key
	 * @param t 缓存的数据
	 */
	void storage(K key, V t);
	/**
	 * 读取缓存
	 * @param key 缓存的key
	 * @return 读取失败返回null
	 */
	V recovery(K key);
}
