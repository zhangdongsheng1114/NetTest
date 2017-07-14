package com.teducn.cn.employeemanager.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.teducn.cn.employeemanager.constant.IURL;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2017/7/14.
 */

public class HttpLoginManager {

    // 保存服务器端响应验证码时写到客户端的会话ID
    public static String SESSIONID = "";

    public static Bitmap httpLoadCodeGet() {
        Bitmap bitmap = null;
        try {
            URL url = new URL(IURL.CODE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.connect();
            // 获得响应的状态码
            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                // 获得响应头中的会话ID
                SESSIONID = connection.getHeaderField("Set-Cookie").split(";")[0];
                // 获得响应结果
                InputStream is = connection.getInputStream();
                // 把流转化成一个图片
                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } else {
                Log.i("TAG:statusCode", "" + statusCode);
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param loginName 用户名
     * @param password
     * @param code      验证码
     * @return
     */
    public static boolean httpLoginPost(String loginName, String password, String code) {

        BufferedReader reader = null;
        try {
            URL url = new URL(IURL.LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // 登陆时设置请求头信息（服务器在响应状态码时保存在客户端的会话Id）
            connection.setRequestProperty("Cookie", SESSIONID);
            connection.connect();

            Map<String, String> params = new HashMap<>();
            params.put("loginname", loginName);
            params.put("password", password);
            params.put("code", code);
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            byte[] datas = builder.deleteCharAt(builder.length() - 1).toString().getBytes();
            // 获得指向服务器端的输出流
            OutputStream os = connection.getOutputStream();
            os.write(datas);
            os.flush();
            os.close();
            // 获得响应的状态码
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
                JSONObject jsonObject = new JSONObject(jsonStr);
                String result = jsonObject.getString("result");
                if ("ok".equals(result)) {
                    return true;
                } else {
                    String msg = jsonObject.getString("msg");
                    Log.i("TAG:msg", msg);
                    return false;
                }
            } else {
                Log.i("TAG:code", statusCode + "");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
