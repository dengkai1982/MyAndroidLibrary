package com.sourceforge.apklib.foundation.utils;

import java.io.BufferedWriter;

/**
 * 字符处理工具类
 */
public  final class StringUtils {
	/**
	 * 截取掉Java方法名set,get,并将首字母小写
	 * @return
	 */
	public final static String methodNameTrim(String methodName){
		if(methodName.startsWith("get")||methodName.startsWith("set")){
			methodName=methodName.substring("get".length(), methodName.length());
			char[] first=new char[]{methodName.charAt(0)};
			return new String(first).toLowerCase()+methodName.substring(1,methodName.length());
		}else{
			return methodName;
		}
	}
	/**首字母大写*/
	public final static String upperFirst(String str){
		String temp=str.toUpperCase();
		return temp.substring(0, 1)+str.substring(1, str.length());
	}
	/**首字母小写*/
	public final static String lowerFirst(String str){
		String temp=str.toLowerCase();
		return temp.substring(0, 1)+str.substring(1, str.length());
	}
	/**
	 * 计算text的长度（一个中文算两个字符）
	 * 
	 * @param text
	 * @return
	 */
	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length;
	}
	/**
	 * 替换双引号为单引号
	 * @param text
	 * @return
	 */
	public final static String singleQuotationMark(String text){
		return text.replace("\"", "'");
	}
	/**
	 * 替换单引号为双引号
	 * @param text
	 * @return
	 */
	public final static String doubleQuotationMark(String text){
		return text.replace("\'", "\"");
	}
}
