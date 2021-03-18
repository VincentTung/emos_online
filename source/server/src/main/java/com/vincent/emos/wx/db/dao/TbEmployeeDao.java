package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.EmployeeList;
import com.vincent.emos.wx.db.pojo.TbEmployee;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TbEmployeeDao {

    TbEmployee searchByCode(int code);
    List<EmployeeList> searchEmployeeList();
    List<TbEmployee> searchByDepartId(int dept_id);
    List<TbEmployee> searchByName(String name);

    int insertEmployee(TbEmployee employee);

    List<TbEmployee> searchUnActiveEmployees();
    int updateState(HashMap params);

    int updateEmployee(TbEmployee employee);

    int deleteEmployee(int code);

}