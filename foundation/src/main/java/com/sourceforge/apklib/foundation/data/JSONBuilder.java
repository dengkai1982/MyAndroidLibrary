package com.sourceforge.apklib.foundation.data;

import android.support.v4.util.ArrayMap;

import com.sourceforge.apklib.foundation.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 之前写的FastJsonString在处理复杂json时有问题
 */
public class JsonBuilder {

    public static final <T> String object2Json(T t, String[] reflectKey, ReplaceKeyPolicy replaceKeyPolicy, ValuePolicy valueProlicy) {
        if (t == null) return "{}";
        List<T> list = new ArrayList<T>();
        list.add(t);
        return collection2Json(list, reflectKey, replaceKeyPolicy, valueProlicy);
    }

    public static final <T> String collection2Json(Collection<T> c,
                                                   String[] reflectKey, ReplaceKeyPolicy replaceKeyPolicy, ValuePolicy valueProlicy) {
        if (c == null || c.isEmpty()) {
            return getEmptyJson();
        }
        JSONObject jo = new JSONObject();
        Class<?> clz = c.iterator().next().getClass();
        Method[] methods = clz.getMethods();
        String key = StringUtils.lowerFirst(clz.getSimpleName());
        JSONArray array = getJsonArray(c, reflectKey, methods, replaceKeyPolicy, valueProlicy);
        try {
            jo.put(key, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    /**
     * 值替换策略
     */
    public interface ValuePolicy {
        Object getValue(String methodName, Object obj);
    }

    /**
     * KEY替换策略
     */
    public interface ReplaceKeyPolicy {
        /**
         * 替换Key名称,如果不替换,直接返回key
         *
         * @param key
         * @return
         */
        public String replace(String key);
    }

    public static final <T> JSONArray getJsonArray(Collection<T> c,
                                                   String[] reflectKey, Method[] methods,
                                                   ReplaceKeyPolicy replaceKeyPolicy, ValuePolicy valueProlicy) {
        JSONArray array = new JSONArray();
        if (c != null && !c.isEmpty() && methods != null) {
            for (Iterator<T> it = c.iterator(); it.hasNext(); ) {
                T obj = it.next();
                JSONObject jo = new JSONObject();
                for (String ref : reflectKey) {
                    String methodName = "get" + StringUtils.upperFirst(ref);
                    Method method = getMatchMethod(methods,
                            methodName);
                    try {
                        Object value = method.invoke(obj);
                        String key = ref;
                        if (replaceKeyPolicy != null) {
                            key = replaceKeyPolicy.replace(ref);
                        }
                        if (key == null) {
                            key = ref;
                        }
                        Object newValue = null;
                        if (valueProlicy != null) {
                            newValue = valueProlicy.getValue(ref, value);
                        }
                        if (newValue != null) {
                            value = newValue;
                        }
                        if (value == null) {
                            value = "";
                        }
                        jo.put(key, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                array.put(jo);
            }
        }
        return array;
    }

    public static final String getEmptyJson() {
        return "{}";
    }

    /**
     * 组合多个JSON字符串,组合的字符串必须以{...}开头和结尾
     *
     * @param jsons
     * @return
     */
    public static final String linkMultiJsons(String... jsons) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> al = new ArrayList<String>();
        for (String json : jsons) {
            if (json.length() > 3) {
                al.add(json);
            }
        }
        if (al.size() == 1) {
            return al.get(0);
        }
        for (int i = 0; i < al.size(); i++) {
            String j = al.get(i);
            if (i == 0) {
                builder.append(j.substring(0, j.length() - 1));
            } else if (i != al.size() - 1) {
                builder.append(",").append(j.substring(1, j.length() - 1));
            } else if (i == al.size() - 1) {
                builder.append(",").append(j.substring(1, j.length()));
            }
        }
        return builder.toString();
    }

    // 从Methods中获取匹配的方法
    private static final Method getMatchMethod(Method[] methods, String methodName) {
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    public static final String simpleToJson(String key,String value){
        JSONObject root = new JSONObject();
        try {
            root.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root.toString();
    }

    public static final String mapToJson(ArrayMap<String, String> map) {
        JSONObject root = new JSONObject();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            try {
                root.put(key, map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return root.toString();
    }
}
