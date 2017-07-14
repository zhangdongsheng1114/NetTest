package com.teducn.cn.employeemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.teducn.cn.employeemanager.R;
import com.teducn.cn.employeemanager.entity.Employee;
import com.teducn.cn.employeemanager.http.HttpAddManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.et_add_username)
    EditText etAddUsername;
    @BindView(R.id.et_add_salary)
    EditText etAddSalary;
    @BindView(R.id.et_add_age)
    EditText etAddAge;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.btn_add)
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        String username = etAddUsername.getText().toString();
        double salary = Double.parseDouble(etAddSalary.getText().toString());
        int age = Integer.parseInt(etAddAge.getText().toString());
        String gender = "";
        if (rbFemale.isChecked()) {
            gender = "m";
        } else if (rbMale.isChecked()) {
            gender = "f";
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (age <= 0 || salary <= 0) {
            Toast.makeText(this, "请填写有效的年龄或薪水", Toast.LENGTH_SHORT).show();
            return;
        }

        Employee employee = new Employee(-1, username, age, salary, gender);
        asyncAddEmployee(employee);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Boolean result = (Boolean) msg.obj;
            if (result) {
                Toast.makeText(AddActivity.this, "添加员工成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, BrowseActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AddActivity.this, "添加员工失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void asyncAddEmployee(final Employee employee) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = HttpAddManager.httpAddPost(employee);
                Message message = handler.obtainMessage();
                message.obj = result;
                handler.sendMessage(message);
            }
        }).start();
    }
}
