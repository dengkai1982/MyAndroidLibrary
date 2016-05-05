package com.sourceforge.apklib.foundation.http;

import com.sourceforge.apklib.foundation.utils.Logger;

public abstract class AbstractHttpRequestListener<T> implements HttpRequestListener<T>{
    protected static Logger logger;
    public AbstractHttpRequestListener(){
        logger=Logger.getLogger(this.getClass());
    }

    @Override
    public void onThrowException(HttpRequest request, Throwable e) {
        logger.e(e.getMessage(),e);
    }

    /*

    @Override
    public void onMessageReceived(HttpRequest request,HttpResponse response,byte[] result) {

    }

    @Override
    public byte[] onParseMessage(InputStream in,String charset) {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        AndroidUtils.writeInputToOutput(in,out,1024,false);
        return out.toByteArray();
    }*/
}
