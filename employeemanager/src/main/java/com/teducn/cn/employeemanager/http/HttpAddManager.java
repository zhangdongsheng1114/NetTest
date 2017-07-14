package com.teducn.cn.employeemanager.http;

import android.util.Log;

import com.teducn.cn.employeemanager.constant.IURL;
import com.teducn.cn.employeemanager.entity.Employee;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tarena on 2017/7/14.
 */

public class HttpAddManager {

    /**
     * 添加一个员工信息
     *
     * @param employee 要添加的员工对象
     * @return
     */
    public static boolean httpAddPost(Employee employee) {
        BufferedReader reader = null;
        try {
            URL url = new URL(IURL.ADDEMP_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求的参数
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            Map<String, String> params = new HashMap<>();
            params.put("name", employee.getName());
            params.put("salary", String.valueOf(employee.getSalary()));
            params.put("age", String.valueOf(employee.getAge()));
            params.put("gender", employee.getGender());

            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            byte[] datas = builder.deleteCharAt(builder.length() - 1).toString().getBytes();
            // 向服务器端输入数据
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
                Log.i("TAG:msg", statusCode + "");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Employee> HttpEmployeesGet() {
        BufferedReader reader = null;
        List<Employee> employees = new ArrayList<>();
        try {
            URL url = new URL(IURL.LISTEMP_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String jsonStr = builder.toString();
                JSONObject jsonObject = new JSONObject(jsonStr);
                String result = jsonObject.getString("result");
                if ("ok".equals(result)) {
                    JSONArray jsonEmployees = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonEmployees.length(); i++) {
                        // 每循环一次获得一个json对象
                        JSONObject jsonEmp = jsonEmployees.getJSONObject(i);
                        int id = jsonEmp.getInt("id");
                        String name = jsonEmp.getString("name");
                        int age = jsonEmp.getInt("age");
                        double salary = jsonEmp.getDouble("salary");
                        String gender = jsonEmp.getString("gender");

                        Employee employee = new Employee(id, name, age, salary, gender);
                        employees.add(employee);
                    }
                } else {
                    String msg = jsonObject.getString("msg");
                    Log.i("TAG:msg", msg);
                }
            } else {
                Log.i("TAG:code", statusCode + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }
}
