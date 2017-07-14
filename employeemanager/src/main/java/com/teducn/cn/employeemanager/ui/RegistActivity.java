package com.teducn.cn.employeemanager.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.teducn.cn.employeemanager.R;
import com.teducn.cn.employeemanager.entity.User;
import com.teducn.cn.employeemanager.http.HttpRegistManager;

public class RegistActivity extends Activity {

    EditText editText_LoginName;
    EditText editText_Password;
    EditText editText_OkPassword;
    EditText editText_RealName;
    EditText editText_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initialUI();
    }

    private void initialUI() {

        editText_LoginName = (EditText) findViewById(R.id.et_login_account);
        editText_Password = (EditText) findViewById(R.id.et_login_password);
        editText_OkPassword = (EditText) findViewById(R.id.et_login_repassword);
        editText_RealName = (EditText) findViewById(R.id.et_login_realname);
        editText_Email = (EditText) findViewById(R.id.et_login_emial);
    }


    public void doRegist(View view) {

        String loginname = editText_LoginName.getText().toString();
        String password = editText_Password.getText().toString();
        String okpassword = editText_OkPassword.getText().toString();
        String realName = editText_RealName.getText().toString();
        String email = editText_Email.getText().toString();

        if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(realName) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入完整的注册信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(okpassword)) {
            Toast.makeText(this, "密码和确认密码必须一致！", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(-1, loginname, password, email, realName);
        // 异步时的注册请求
        sayncRegist(user);
    }

    Handler hanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Boolean result = (boolean) msg.obj;
            if (result) {
                Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void sayncRegist(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = HttpRegistManager.httpRegistPost(user);
                Message message = hanlder.obtainMessage();
                message.obj = result;
                hanlder.sendMessage(message);
            }
        }).start();
    }
}
