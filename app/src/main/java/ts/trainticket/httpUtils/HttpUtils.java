package ts.trainticket.httpUtils;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import ts.trainticket.utils.ApplicationPreferences;

public class HttpUtils {


    //RequestBody requestBody = requestBodyBuilder.add("key","vsds").build();
    public static String postData(String url_post, RequestBody requestBody ) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().addHeader("Cookie","YsbCaptcha=B5D850BB397A4725B1C985955B2738F0").url(url_post).post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        if(response.isSuccessful()){
            return response.body().string();
        }else{
            throw new IOException("Unexcepted cde " + response);
        }
    }



    public static String postDataWithHeader(String url_post, String loginId, String loginToken, RequestBody requestBody ) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        String cookie = "jenkins-timestamper-offset=-28800000; loginId="+ loginId+"; JSESSIONID=7E2A0BE075AF166D9FAAF91D0FBB537D; loginToken="+loginToken +"; YsbCaptcha=E2DDD2D58ACD4E40BDD874BBBEF1F5AD";

        Request request = new Request.Builder()
                .addHeader("Cookie",cookie)
                .url(url_post)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if(response.isSuccessful()){
            return response.body().string();
        }else{
            throw new IOException("Unexcepted cde " + response);
        }
    }



    public static String sendPostRequest(String url, String jsonStr) 
		{
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; // 写
        //InputStream in = null;  // 读
        int responseCode = 0; // 远程主机响应的http状态吗
        String result = "";
        try {
            URL sendUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            // post 方法请求
            httpURLConnection.setRequestMethod("POST");
            // 设置头部信息, content-type 一定要设置
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // 指示应用程序将数据写入url连接, 其值默认为false
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);// post 请求不能使用缓存
            httpURLConnection.setConnectTimeout(15000); //连接超时
            httpURLConnection.setReadTimeout(15000); //读取超时
            // 传入参数
            out = httpURLConnection.getOutputStream();
            out.write(jsonStr.getBytes());
            out.flush(); // 清空缓存区,发送数据
            out.close();
            responseCode = httpURLConnection.getResponseCode();
            // 获取请求的资源
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            result = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String sendGetRequest(String uri) {
        String info = null;
        String allInfo = "";
        InputStream tempInput = null;
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(12000);
            connection.setReadTimeout(20000);

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            tempInput = connection.getInputStream();
            Log.i("TAG", "uri" + uri);
            if (tempInput != null) {
                InputStreamReader reader = new InputStreamReader(tempInput, "utf-8");
                BufferedReader br = new BufferedReader(reader);

                while ((info = br.readLine()) != null) {
                    Log.i("TAG", "info" + info);
                    allInfo += info;
                }
                br.close();
                reader.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allInfo;
    }

    public static String getContacts(String uri, String loginId, String loginToken) {
        String info = null;
        String allInfo = "";
        InputStream tempInput = null;
        URL url = null;
        HttpURLConnection connection = null;
        String cookie = "jenkins-timestamper-offset=-28800000; loginId="+ loginId+"; JSESSIONID=7E2A0BE075AF166D9FAAF91D0FBB537D; loginToken="+loginToken +"; YsbCaptcha=E2DDD2D58ACD4E40BDD874BBBEF1F5AD";
        try {
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(12000);
            connection.setReadTimeout(20000);
            connection.setRequestProperty("Cookie",cookie);

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            tempInput = connection.getInputStream();
            Log.i("TAG", "uri" + uri);
            if (tempInput != null) {
                InputStreamReader reader = new InputStreamReader(tempInput, "utf-8");
                BufferedReader br = new BufferedReader(reader);

                while ((info = br.readLine()) != null) {
                    Log.i("TAG", "info" + info);
                    allInfo += info;
                }
                br.close();
                reader.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allInfo;
    }


}
