package com.teducn.cn.employeemanager.http;

import android.util.Log;

import com.teducn.cn.employeemanager.constant.IURL;
import com.teducn.cn.employeemanager.entity.User;

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
 * Created by tarena on 2017/7/13.
 */

public class HttpRegistManager {

    public static boolean httpRegistPost(User user) {

        BufferedReader reader = null;
        try {
            URL url = new URL(IURL.REGIST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Map<String, String> params = new HashMap<>();
            // 封装要提交的数据
            params.put("loginname", user.getLoginName());
            params.put("password", user.getPassword());
            params.put("realname", user.getRealName());
            params.put("email", user.getEmail());

            // 遍历集合，拼接字符串
            // loginname=xxxx&password=xxxx&realname=xxxx&email=xxxxx
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

            // 获得指向服务器端资源的输出流
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
                Log.i("TAG", statusCode + "");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
