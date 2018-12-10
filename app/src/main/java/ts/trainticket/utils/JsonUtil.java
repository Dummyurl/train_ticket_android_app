package ts.trainticket.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuZOZO on 2018/1/13.
 */
public class JsonUtil {

    /**
     * 从存入的Json String获取List<Object>
     */
    public static List<Object> getListFromJsonString(String json) {
        List<Object> list = new ArrayList<>();

        if (TextUtils.isEmpty(json)) {
            return list;
        }

        try {
            JSONArray jsonPaths = new JSONArray(json);

            for (int i = 0; i < jsonPaths.length(); i++) {
                list.add(jsonPaths.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 从Json String中获取一个Object，利用反射赋值属性
     */
    public static <T> T getObjectFromJson(String json, Class<T> t) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        T classT = null;

        try {
            classT = t.newInstance();
            JSONObject jsonObject = new JSONObject(json);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                Method setMethod = t.getMethod("set" + (key.charAt(0) + "").toUpperCase() + key.substring(1), String.class);
                setMethod.invoke(classT, jsonObject.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classT;
    }


    /**
     * 通过 key 与 value 获得发送给服务器的Json
     *
     * @param keys   Json keys
     * @param values Json send values
     * @return JsonObject
     */
    public static JSONObject getSendJson(List keys, List values) {
        JSONObject sendJson = new JSONObject();

        try {
            for (int i = 0; i < keys.size(); i++) {
                sendJson.put((String) keys.get(i), values.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sendJson;
    }
}
