package com.sourceforge.apklib.foundation.utils;
import android.util.Log;

/**
 * 日志输出
 *
 */
public class Logger {
	private String tag;
	private int VERBOSE=1;
	private int DEBUG=2;
	private int INFO=3;
	private int WARN=4;
	private int ERROR=5;
	private int NOTHING=7;
	//发布生产环境项目时将此改为NOTHING则不输出任何日志
	private int CURR_LEVEL=VERBOSE;
	
	public void v(Object msg){
		if(CURR_LEVEL<=VERBOSE){
			Log.v(tag, msg==null?"null":msg.toString());
		}
	}
	public void d(Object msg){
		if(CURR_LEVEL<=DEBUG){
			Log.d(tag, msg==null?"null":msg.toString());
		}
	}
	public void i(Object msg){
		if(CURR_LEVEL<=INFO){
			Log.i(tag, msg==null?"null":msg.toString());
		}
	}
	public void w(Object msg){
		if(CURR_LEVEL<=WARN){
			Log.w(tag, msg==null?"null":msg.toString());
		}
	}
	public void e(Object msg){
		if(CURR_LEVEL<=ERROR){
			Log.e(tag,msg==null?"null":msg.toString());
		}
	}

	public void e(Object msg,Throwable t){
		if(CURR_LEVEL<=ERROR){
			Log.e(tag,msg==null?"null":msg.toString(),t);
		}
	}
	public void n(Object msg){
		if(CURR_LEVEL<=NOTHING){
			//nothing...
		}
	}
	private Logger(Class<?> clz){
		tag=clz.getName();
	}
	
	public static final Logger getLogger(Class<?> clz){
		return new Logger(clz);
	}
}
