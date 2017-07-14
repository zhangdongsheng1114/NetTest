package com.teducn.cn.employeemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.teducn.cn.employeemanager.R;
import com.teducn.cn.employeemanager.adapter.EmployeeAdapter;
import com.teducn.cn.employeemanager.entity.Employee;
import com.teducn.cn.employeemanager.http.HttpAddManager;

import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    ListView listView_Employee;
    EmployeeAdapter adapter = null;
    ImageView imageView_Add = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        initialUI();
        asyncLoadEmployees();
    }

    private void initialUI() {
        imageView_Add = (ImageView) findViewById(R.id.iv_add_employee);
        listView_Employee = (ListView) findViewById(R.id.listview_employee);
        adapter = new EmployeeAdapter(this);
        listView_Employee.setAdapter(adapter);

        imageView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseActivity.this, AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<Employee> employees = (List<Employee>) msg.obj;
            // 把集合添加到适配器中
            adapter.addEmployee(employees);
        }
    };

    private void asyncLoadEmployees() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Employee> employees = HttpAddManager.HttpEmployeesGet();
                Message message = handler.obtainMessage();
                message.obj = employees;
                handler.sendMessage(message);
            }
        }).start();
    }
}
