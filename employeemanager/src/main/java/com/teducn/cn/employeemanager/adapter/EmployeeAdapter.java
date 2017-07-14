package com.teducn.cn.employeemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teducn.cn.employeemanager.R;
import com.teducn.cn.employeemanager.entity.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2017/7/14.
 */

public class EmployeeAdapter extends BaseAdapter {

    List<Employee> employeeList = new ArrayList<>();
    public Context context = null;

    public EmployeeAdapter(Context context) {
        this.context = context;
    }

    public void addEmployee(List<Employee> employees) {
        if (employees != null) {
            employeeList.addAll(employees);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return employeeList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
            holder = new ViewHolder();
            holder.textView_Age = (TextView) convertView.findViewById(R.id.textView_Emp_Age);
            holder.textView_Id = (TextView) convertView.findViewById(R.id.textView_Emp_Id);
            holder.textView_Name = (TextView) convertView.findViewById(R.id.textView_Emp_Name);
            holder.textView_Salary = (TextView) convertView.findViewById(R.id.textView_Emp_Salary);
            holder.textView_Gender = (TextView) convertView.findViewById(R.id.textView_Emp_Gender);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Employee employee = (Employee) getItem(position);

        holder.textView_Id.setText(String.valueOf(employee.getId()));
        holder.textView_Name.setText(employee.getName());
        holder.textView_Age.setText(String.valueOf(employee.getAge()));
        holder.textView_Salary.setText(String.valueOf(employee.getSalary()));
        String gender = "";
        if (employee.getGender().equals("m")) {
            gender = "男";
        } else {
            gender = "女";
        }
        holder.textView_Gender.setText(gender);
        return convertView;
    }

    private class ViewHolder {

        TextView textView_Id;
        TextView textView_Name;
        TextView textView_Age;
        TextView textView_Salary;
        TextView textView_Gender;
    }
}
