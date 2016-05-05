package com.sourceforge.apklib.foundation.http.request;

import com.sourceforge.apklib.foundation.data.DataTyper;
import com.sourceforge.apklib.foundation.http.HttpRequestListener;
import com.sourceforge.apklib.foundation.utils.FilesystemUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * http 文件上传
 */
public class HttpFileUpload extends HttpPost {
    private List<File> uploadFiles;

    public HttpFileUpload(HttpRequestListener listener) {
        super(listener);
    }

    /**
     * 添加上传的文件
     *
     * @param file
     */
    public void addFile(File... file) {
        if (file != null) {
            for (File f : file) {
                uploadFiles.add(f);
            }
        }
    }

    @Override
    protected InputStream getParameterInputStream(HttpURLConnection connect, String charset) throws IOException {
        DataOutputStream out = null;
        ByteArrayOutputStream baos = null;
        String prefix = "--";
        String line = "\r\n";
        String boundary = "----" + System.nanoTime();
        connect.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        Map<String, DataTyper> params = getRequestParameter();
        if (!connect.getDoOutput()) {
            connect.setDoOutput(true);
        }
        baos = new ByteArrayOutputStream();
        out = new DataOutputStream(baos);
        //写参数
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                StringBuilder sb = new StringBuilder();
                String value = params.get(key).getString();
                sb.append(prefix + boundary + line);
                sb.append("Content-Disposition: form-data; name=\"" + key + "\"" + line);
                sb.append(line);
                value = URLEncoder.encode(value, charset);
                sb.append(value + line);
                out.write(sb.toString().getBytes(charset));
            }
        }
        //写文件
        if (!uploadFiles.isEmpty()) {
            for (File file : uploadFiles) {
                out.write((prefix + boundary + line).getBytes(charset));
                out.write(("Content-Disposition: form-data; name=\"" + file.getName()
                        + "\"; filename=\"" + URLEncoder.encode(file.getName(), charset) + "\"" + line).getBytes(charset));
                out.write(("Content-Type: " + FilesystemUtils.getMimeType(file.getAbsolutePath()) + line).getBytes(charset));
                out.write(line.getBytes(charset));
                FileInputStream in = null;
                try {
                    in = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = -1;
                    while ((len = in.read(bytes, 0, bytes.length)) != -1) {
                        out.write(bytes, 0, len);
                    }
                } catch (Exception e) {

                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                out.write(line.getBytes(charset));
            }
        }
        out.write((prefix + boundary + prefix + line).getBytes(charset));
        out.write(line.getBytes(charset));
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
