package com.teducn.cn.employeemanager.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.teducn.cn.employeemanager.R;
import com.teducn.cn.employeemanager.http.HttpLoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.et_login_verification)
    EditText etLoginVerification;
    @BindView(R.id.iv_verification)
    ImageView ivVerification;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 加载验证码
        asyncLoadCode();
    }

    // 点击图片加载验证码
    public void loadCode(View view) {
        asyncLoadCode();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 1) {
                // 验证码加载成功的消息
                Bitmap bitmap = (Bitmap) msg.obj;
                // 把验证码设置到控件上
                if (bitmap != null) {
                    ivVerification.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(LoginActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
                }
            } else if (what == 2) {
                Boolean result = (Boolean) msg.obj;
                if (result) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void asyncLoadCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap codeBitmap = HttpLoginManager.httpLoadCodeGet();
                // 通知UI主线程把加载的图片设置到控件上
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = codeBitmap;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        // 获得登录信息
        String username = etLoginUsername.getText().toString();
        String password = etLoginPassword.getText().toString();
        String verification = etLoginVerification.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verification)) {
            Toast.makeText(this, "请填写完整的登录信息", Toast.LENGTH_SHORT).show();
            return;
        }
        asyncLogin(username, password, verification);
    }

    /**
     * 实现异步登录操作
     *
     * @param username
     * @param password
     * @param verification
     */
    private void asyncLogin(final String username, final String password, final String verification) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = HttpLoginManager.httpLoginPost(username, password, verification);
                // 获得一个消息对象
                Message message = handler.obtainMessage();
                message.obj = result;
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }
}
