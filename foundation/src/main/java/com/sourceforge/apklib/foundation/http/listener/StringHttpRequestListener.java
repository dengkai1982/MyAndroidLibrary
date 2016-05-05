package com.sourceforge.apklib.foundation.http.listener;
import com.sourceforge.apklib.foundation.http.AbstractHttpRequestListener;
import com.sourceforge.apklib.foundation.utils.FilesystemUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
/**
 * Http请求返回String
 */
public abstract class StringHttpRequestListener extends AbstractHttpRequestListener<String>{

    @Override
    public String onParseMessage(InputStream in, String charset) {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        FilesystemUtils.writeInputToOutput(in,out,1024);
        try {
            return new String(out.toByteArray(),charset);
        } catch (UnsupportedEncodingException e) {
            logger.e(e.getMessage());
        } finally{
            FilesystemUtils.close(out);
        }
        return "";
    }
}
