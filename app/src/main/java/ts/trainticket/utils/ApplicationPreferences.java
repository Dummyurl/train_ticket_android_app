package ts.trainticket.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuZOZO on 2018/1/13.
 */
public class ApplicationPreferences {

    public static final String PREFERENCES_NAME = "TrainTicket";

    public static final String STATE_OFFLINE = "stateOffline";
    public static final String STATE_ONLINE =  "stateOnline";

    public static final String ACCOUNT_STATE = "accountState";

    public static final String USER_NAME = "userName";
    public static final String USERREALNAME_NAME = "userRealName";
    public static final String ACCOUNT_PASSWORD = "accountPassword";
    public static final String USER_PHONE = "userPhone";
    public static final String REAL_ICARD = "realIcard";
    public static final String HEAD_PIC ="headPicSrc";

    // 用户登录时写入信息
    public static void setUserInfo(Context context ,String userName, String userRealName,String password,String userPhone,String realIcard, String  headPicSrc){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE).edit();
        editor.putString(USER_NAME,userName);
        editor.putString(USERREALNAME_NAME,userRealName);
        editor.putString(ACCOUNT_PASSWORD,password);
        editor.putString(USER_PHONE,userPhone);
        editor.putString(REAL_ICARD,realIcard);
        editor.putString(HEAD_PIC,headPicSrc);
        editor.apply();
    }

    // 查询账户是否登录
    public static boolean isUserOnLine(Context context){
        String state = getOneInfo(context,ACCOUNT_STATE);
        return STATE_ONLINE.equals(state);
    }
    // 退出时，清除登录信息
    public static void clearLoginInfo(Context context){
        setOneInfo(context,ACCOUNT_STATE,STATE_OFFLINE);
    }
    // 写入一个信息
    public static void setOneInfo(Context context, String key, String values){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, values);
        editor.apply();
    }
    // 获取一个存储的信息
    public static String getOneInfo(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }
    // 插入常用路线
    public static void setMemoryPaths(Context context, List<Object> paths){
        JSONArray jsonPaths = new JSONArray(paths);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("MemoryPaths1",jsonPaths.toString());
        editor.apply();
    }
    // 获取常用路线
    public static List<Object> getMemoryPaths(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        return JsonUtil.getListFromJsonString(preferences.getString("MemoryPaths1",""));
    }





    // 插入搜索的城市或站点
    public static void  setMemoryCities(Context context, List<Object> cities){
        JSONArray jsonCities = new JSONArray(cities);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE).edit();
        editor.putString("MemoryCities",jsonCities.toString());
        editor.apply();
    }
    // 获取搜索的城市或站点
    public static List<Object> getMemoryCitiess(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        return JsonUtil.getListFromJsonString(preferences.getString("MemoryCities",""));
    }
    // 写入所有城市
    public static void setAllCities(Context context, List<Object> cities){
        JSONArray jsonArray = new JSONArray(cities);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE).edit();
        editor.putString("allCities",jsonArray.toString());
        editor.apply();
    }


    // 查询出所有的城市
    public static List<Object> getAllCities(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        return JsonUtil.getListFromJsonString(preferences.getString("allCities",""));
    }

    // 车站停留时间
    public static void setStayTimeStation(Context context, List<Object> stationTimes){
        JSONArray jsonArray = new JSONArray(stationTimes);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE).edit();
        editor.putString("stationTimes",jsonArray.toString());
        editor.apply();
    }

    public static List<Object>  getStayTimeStation(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        return  JsonUtil.getListFromJsonString(preferences.getString("stationTimes",""));
    }
}
