package com.vincent.emos.wx.service.impl;

import cn.hutool.core.date.DateUtil;
import com.vincent.emos.wx.db.dao.TbEmployeeDao;
import com.vincent.emos.wx.db.pojo.EmployeeList;
import com.vincent.emos.wx.db.pojo.TbEmployee;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
@Scope("prototype")
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private TbEmployeeDao employeeDao;

    @Override
    public TbEmployee searchByCode(int code) {
        return employeeDao.searchByCode(code);
    }

    @Override
    public List<EmployeeList> searchEmployList() {
        return employeeDao.searchEmployeeList();
    }

    @Override
    public List<TbEmployee> searchContact(String name) {
        return employeeDao.searchByName(name);
    }

    @Override
    public TbEmployee insertEmployee(TbEmployee employee) {
        employeeDao.insertEmployee(employee);
        return employee;
    }

    @Override
    public List<TbEmployee> searchUnActiveEmployees() {
        return employeeDao.searchUnActiveEmployees();
    }

    @Override
    public void updateState(HashMap params) {
        int row = employeeDao.updateState(params);
        if (row != 1) {

            throw new EmosException("更新临时员工状态出错");
        }
    }

    @Override
    public void updateEmployee(TbEmployee employee) {
        int row = employeeDao.updateEmployee(employee);
        if (row != 1) {
            throw new EmosException("更新临时员工信息出错");
        }

    }

    @Override
    public void deleteEmployee(int code) {
        int row = employeeDao.deleteEmployee(code);

        if (row != 1) {
            throw new EmosException("更新临时员工信息出错");
        }

    }

    @Override
    public TbEmployee searchEmployee(int code) {
        TbEmployee tbEmployee =  employeeDao.searchByCode(code);
        return tbEmployee;
    }
}
