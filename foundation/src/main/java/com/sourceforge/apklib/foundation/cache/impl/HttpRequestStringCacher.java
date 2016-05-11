package com.sourceforge.apklib.foundation.cache.impl;

import android.support.v4.util.ArrayMap;

import com.sourceforge.apklib.foundation.cache.Cacher;
import com.sourceforge.apklib.foundation.cache.HttpRequestCacher;
import com.sourceforge.apklib.foundation.data.DataTyper;
import com.sourceforge.apklib.foundation.data.JsonBuilder;
import com.sourceforge.apklib.foundation.data.JsonParser;
import com.sourceforge.apklib.foundation.http.HttpCookie;
import com.sourceforge.apklib.foundation.http.HttpRequest;
import com.sourceforge.apklib.foundation.http.HttpResponse;
import com.sourceforge.apklib.foundation.http.request.HttpGet;
import com.sourceforge.apklib.foundation.utils.CoderUtils;
import com.sourceforge.apklib.foundation.utils.HttpUtils;
import com.sourceforge.apklib.foundation.utils.StringUtils;

import org.json.JSONException;

import java.util.Set;

/**
 * http字符串请求缓存
 */
public class HttpRequestStringCacher implements HttpRequestCacher{

    private Cacher cacher;

    private String charset="utf-8";
    private static final String REQ_KEY_PREFIX="http_request_string_cacher_";
    private static final String KEY_COOKIE="http_cookie_key_";
    private HttpResponse response;

    public HttpRequestStringCacher(Cacher cache){
        this.cacher=cache;
    }

    @Override
    public long getModifiedSince(HttpRequest request) {
        if(response==null){
            recovery(request);
        }
        if(response!=null){
            return response.getLastModified();
        }
        return 0;
    }

    @Override
    public void storageCookies(String hostName, Set<HttpCookie> cookies) {
        if(cacher!=null&&cookies!=null){
           String cookiesString = HttpUtils.getCookiesString(cookies,charset);
           cacher.storage(KEY_COOKIE+hostName,cookiesString);
        }
    }

    @Override
    public Set<HttpCookie> getCookies(String hostName) {
        if(cacher!=null){
            String cookiesString =(String)cacher.recovery(KEY_COOKIE+hostName);
            if(cookiesString!=null){
               return HttpUtils.parseCookies(cookiesString);
            }
        }
        return null;
    }

    @Override
    public void storage(HttpRequest request,HttpResponse response) {
        if(cacher!=null&&response.getRequestResult()!=null){
            String key= REQ_KEY_PREFIX+HttpUtils.buildRequestKey(request);
            String obj=(String)response.getRequestResult();
            final String value= CoderUtils.androidBase64Encode(obj,charset);
            String saveJson=JsonBuilder.object2Json(response,new String[]{
                "contentEncoding","mimeType","pageCharset","lastModified"
                    ,"request","requestResult"
            },null,new JsonBuilder.ValuePolicy(){
                @Override
                public Object getValue(String methodName, Object obj){
                    if(methodName.equals("request")){
                        return ((HttpRequest)obj).getUrl();
                    }else if(methodName.equals("requestResult")){
                        return value;
                    }
                    return null;
                }
            });
            cacher.storage(key,saveJson);
        }
    }
    @Override
    public HttpResponse recovery(HttpRequest request) {
        if(response==null){
            String recovery=(String)cacher.recovery(REQ_KEY_PREFIX+HttpUtils.buildRequestKey(request));
            if(recovery!=null){
                try {
                    ArrayMap<String,DataTyper> data=JsonParser.parser(recovery);
                    String name=StringUtils.lowerFirst(HttpResponse.class.getSimpleName());
                    if(data.get(name)!=null){
                        ArrayMap<String,DataTyper> respJson=data.get(name).getSignleResult();
                        HttpResponse resp=new HttpResponse();
                        resp.setContentEncoding(respJson.get("contentEncoding").getString());
                        resp.setMimeType(respJson.get("mimeType").getString());
                        resp.setPageCharset(respJson.get("pageCharset").getString());
                        resp.setLastModified(respJson.get("lastModified").getLong(0));
                        HttpGet get=new HttpGet(null);
                        get.setUrl(respJson.get("request").getString());
                        resp.setRequest(get);
                        String result=respJson.get("requestResult").getString();
                        resp.setRequestResult(CoderUtils.androidBase64Decode(result,charset));
                        response=resp;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return response;
    }
}
