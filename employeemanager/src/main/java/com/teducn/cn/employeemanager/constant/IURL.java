package com.teducn.cn.employeemanager.constant;

/**
 * Created by tarena on 2017/7/13.
 */

public interface IURL {

    //自己的172.60.14.162:8080   老师的172.60.50.31:8000
    String ROOT = "http://172.60.50.31:8000/EmployeeServer/";
    String REGIST_URL = ROOT + "regist.do";
    String CODE_URL = ROOT + "getCode.do";
    String LOGIN_URL = ROOT + "login.do";
    String ADDEMP_URL = ROOT + "addEmp";
    String LISTEMP_URL = ROOT + "listEmp";
}
