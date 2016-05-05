package com.sourceforge.apklib.foundation.http.request;

import android.support.v4.util.ArrayMap;

import com.sourceforge.apklib.foundation.data.DataTyper;
import com.sourceforge.apklib.foundation.http.HttpHeaderNames;
import com.sourceforge.apklib.foundation.http.HttpMethod;
import com.sourceforge.apklib.foundation.http.HttpRequest;
import com.sourceforge.apklib.foundation.http.HttpRequestListener;
import com.sourceforge.apklib.foundation.utils.FilesystemUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Set;

/**
 * Http Post 方式提交
 */
public class HttpPost extends HttpRequest {
    private ArrayMap<String,DataTyper> params;

    private String content;

    public HttpPost(HttpRequestListener listener) {
        super(listener);
    }

    /**
     * 添加参数
     * @param name
     * @param typer
     */
    public void addParameter(String name,Object typer) {
        if(params==null){
            params=new ArrayMap<>();
        }
        params.put(name,new DataTyper(typer));
    }

    /**
     * 设置提交内容,如果设置提交内容,addParameter将无效
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
        if(params!=null){
            params.clear();
            params=null;
        }
    }

    @Override
    public HttpMethod getRequestMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected ArrayMap<String, DataTyper> getRequestParameter() {
        return this.params;
    }

    @Override
    protected InputStream getParameterInputStream(HttpURLConnection connect, String charset) throws IOException {
        connect.setRequestProperty(HttpHeaderNames.CONTENT_TYPE.toString(), "application/x-www-form-urlencoded");
        connect.setDoOutput(true);
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            if(params!=null&!params.isEmpty()){
                StringBuilder sb=new StringBuilder();
                Set<String> keys=params.keySet();
                for(String key:keys){
                        sb.append(URLEncoder.encode(key,charset)).append("=")
                                .append(URLEncoder.encode(params.get(key).getString(),charset)).append("&");

                }
                String param=sb.deleteCharAt(sb.length()-1).toString();
                byte[] bytes=param.getBytes(charset);
                out.write(bytes,0,bytes.length);
                out.flush();
            }else if(content!=null){
                out.write(content.getBytes(charset));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally{
            FilesystemUtils.close(out);
        }
        return new ByteArrayInputStream((out.toByteArray()));
    }
}
