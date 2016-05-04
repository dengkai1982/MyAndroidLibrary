package com.sourceforge.apklib.foundation.utils;
/**
 * 常规数据校验 
 */
public final class BasicVariableVerify {
	//IP地址正则表达式
	private static final String IPRegx="[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
	//移动号码校验,11位
	private static final String MOBILERegx="^[1][3,4,5,6,7,8,9][0-9]{9}$";
	//带区号的固定电话号码
	private static final String AREA_TEL_REGX="^[0][1-9]{2,3}-[0-9]{5,10}$";
	//不带区号的固定电话号码
	private static final String UNAREA_TEL_REGX="^[1-9]{1}[0-9]{5,8}$";
	//电子邮箱
	private static final String EMAILRegx="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	public static final boolean emailValidate(String email){
		return email.matches(EMAILRegx);
	}
	/**
	 * 校验带区号的电话号码
	 * @param tel
	 * @return
	 */
	public static final boolean areaTelValidate(String tel){
		return tel.matches(AREA_TEL_REGX);
	}
	/**
	 * 校验不带区号的电话号码
	 * @param tel
	 * @return
	 */
	public static final boolean unAreaTelValidate(String tel){
		return tel.matches(UNAREA_TEL_REGX);
	}
	/**
	 * 手机号码校验,不含国际区域号
	 * @param phoneNumber
	 * @return
	 */
	public static final  boolean mobileValidate(String phoneNumber){
		return  phoneNumber.matches(MOBILERegx);
	}
	/**
     * IP地址校验,校验通过返回true
     * @param ip
     * @return
     */
    public static final boolean ipValidate(String ip){
    	 if(!ip.matches(IPRegx)){
    		 return false;
    	 }
    	 String[] fileds=ip.split("\\.");
    	 for(int i=0;i<fileds.length;i++){
    		 Integer num=Integer.parseInt(fileds[i]);
    		 if(i==0||i==fileds.length-1){
    			 if(num<=0){
    				 return false;
    			 }
    		 }    		 
    		 if(num>=254){
    			 return false;
    		 }
    	 }
    	return true;
    }
}
