package com.teducn.cn.nettest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.teducn.cn.nettest.http.HttpLoginManager;

public class MainActivity extends AppCompatActivity {

    EditText editText_UserName;
    EditText editText_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialUI();
    }

    private void initialUI() {
        editText_UserName = (EditText) findViewById(R.id.editText_UserName);
        editText_Password = (EditText) findViewById(R.id.editText_Password);
    }

    public void login(View view) {
        String username = editText_UserName.getText().toString();
        String password = editText_Password.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入完整的用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发起网络请求实现登陆处理
        // 在android4.0之后所有的网络访问必须要做异步处理
//        asyncLogin(username, password);

        asyncLogin2(username, password);
    }

    Handler hanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean result = (boolean) msg.obj;
            if (result) {
                Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void asyncLogin2(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = HttpLoginManager.httpLoginPost(username, password);
                // 发消息给主线程
                Message message = hanlder.obtainMessage();
                message.obj = result;
                // message.what=1;
                hanlder.sendMessage(message);

            }
        }).start();
    }

    private void asyncLogin(final String username, final String password) {
        // 开启一个子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 异步执行的代码
                final boolean result = HttpLoginManager.httpLoginGet(username, password);
                // UI的刷新需要在UI主线程中执行
                // 从子线程回到UI主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}
