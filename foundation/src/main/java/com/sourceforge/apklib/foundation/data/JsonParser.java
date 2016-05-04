package com.sourceforge.apklib.foundation.data;
import android.support.v4.util.ArrayMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * JSON数据解析
 */
public class JsonParser {
    //存放原始返回的json数据
    public static final String KEY_SRC_JSON="src_json";
    /*
     * 解析复杂的JSON到ArrayMap中
     */
    public static final ArrayMap<String, DataTyper> parser(String jsonText) throws JSONException {
        JSONObject jo = new JSONObject(jsonText);
        ArrayMap<String, DataTyper> am = new ArrayMap<String,DataTyper>();
        Iterator<String> keys = jo.keys();
        for (; keys.hasNext();) {
            String key = keys.next();
            Object obj = jo.get(key);
            if (obj instanceof JSONArray) {
                am.put(key, new DataTyper(parse((JSONArray) obj)));
            } else {
                am.put(key, new DataTyper(obj));
            }
        }
        am.put(KEY_SRC_JSON, new DataTyper(jsonText));
        return am;
    }
    private static final List<ArrayMap<String, DataTyper>> parse(JSONArray jr)
            throws JSONException {
        List<ArrayMap<String, DataTyper>> arrayList = new ArrayList<ArrayMap<String, DataTyper>>();
        for (int i = 0; i < jr.length(); i++) {
            JSONObject jo = (JSONObject) jr.get(i);
            Iterator<String> keys = jo.keys();
            ArrayMap<String, DataTyper> map = new ArrayMap<String, DataTyper>();
            for (; keys.hasNext();) {
                String key = keys.next();
                Object obj = jo.get(key);
                if (obj instanceof JSONArray) {
                    map.put(key, new DataTyper(parse((JSONArray) obj)));
                } else {
                    map.put(key, new DataTyper(obj.toString()));
                }
            }
            arrayList.add(map);
        }
        return arrayList;
    }
}
