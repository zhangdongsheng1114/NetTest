package com.teducn.cn.nettest.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2017/7/13.
 */

public class HttpLoginManager {

    /**
     * @param username 登陆的用户名
     * @param password 登陆的密码
     * @return
     */
    public static boolean httpLoginGet(String username, String password) {
        String path = "http://172.60.14.162:8080/MyWebTest/LoginServlet";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        StringBuilder builder = new StringBuilder();
        builder.append(path);
        builder.append("?");
        // username=admin&password=111111
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }
        String urlPath = builder.deleteCharAt(builder.length() - 1).toString();
        // 构建URL对象
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求的提交方式
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                InputStream in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                StringBuilder jsonBuilder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                String jsonStr = jsonBuilder.toString();
                // 把json字符串再转成一个json对象
                JSONObject jsonObject = new JSONObject(jsonStr);
                String result = jsonObject.getString("result");
                if ("ok".equals(result)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.i("TAG:code", statusCode + "");
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean httpLoginPost(String username, String password) {
        String path = "http://172.60.14.162:8080/MyWebTest/LoginServlet";
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求的参数
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            // username=admin&password=111111
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            byte[] datas = builder.deleteCharAt(builder.length() - 1).toString().getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(datas.length));
            connection.connect();
            // 以输出流的方式把要向服务器端提交的数据写到服务器端
            OutputStream os = connection.getOutputStream();
            os.write(datas);
            os.flush();
            os.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder jsonBuilder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                String jsonStr = jsonBuilder.toString();
                Log.i("TAG:json", jsonStr);
                JSONObject jsonObject = new JSONObject(jsonStr);
                String result = jsonObject.getString("result");
                if ("ok".equals(result)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.i("TAG:code", statusCode + "");
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
