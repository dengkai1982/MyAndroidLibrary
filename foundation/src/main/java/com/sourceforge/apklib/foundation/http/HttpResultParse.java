package com.sourceforge.apklib.foundation.http;

import java.io.InputStream;

/**
 * http 请求结果解析
 */
public interface HttpResultParse<T> {
    /**
     * 解析http请求内容结果
     * @param stream
     */
    T parse(InputStream stream);
}
