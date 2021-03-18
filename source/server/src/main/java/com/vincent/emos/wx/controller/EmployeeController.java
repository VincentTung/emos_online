package com.vincent.emos.wx.controller;

import cn.hutool.core.date.DateUtil;
import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.controller.form.*;
import com.vincent.emos.wx.db.pojo.TbEmployee;
import com.vincent.emos.wx.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/employee")
@Api("员工信息相关")
@Slf4j
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employeeList")
    @ApiOperation("通讯录")
    public R refreshMessage(@RequestHeader("token")String token){
        return R.ok().put("list",employeeService.searchEmployList());
    }

    @PostMapping("/employeeSearch")
    @ApiOperation("查询通讯录")
    public R refreshMessage(@RequestHeader("token")String token,@Valid @RequestBody  SearchContactForm form){
        return R.ok().put("list",employeeService.searchContact(form.getName()));
    }


    @PostMapping("/insertEmployee")
    @ApiOperation("添加新员工")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:INSERT"}, logical = Logical.OR)
    public R insertEmployee(@RequestHeader("token")String token, @Valid @RequestBody InsertEmployeeForm form){

        TbEmployee employee = new TbEmployee();
        employee.setName(form.getName());
        employee.setEmail(form.getEmail());
        employee.setTel(form.getTel());
        employee.setHiredate(DateUtil.parseDate(form.getHiredate()));
        employee.setRole(form.getRole());
        employee.setDeptId(form.getDept_id());
        employee.setSex(form.getSex());

        employee = employeeService.insertEmployee(employee);
        return R.ok().put("result",employee);
    }


    @PostMapping("/searchUnActiveEmployees")
    @ApiOperation("查询未激活员工")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:SELECT"}, logical = Logical.OR)
    public R searchUnActiveEmployees(@RequestHeader("token")String token){
        List<TbEmployee> list =  employeeService.searchUnActiveEmployees();
        return R.ok().put("result",list);
    }



    @PostMapping("/updateState")
    @ApiOperation("修改员工状态")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:UPDATE"}, logical = Logical.OR)
    public R updateState(@RequestHeader("token")String token, @Valid @RequestBody UpdateEmployeeStateForm form){

        HashMap params = new HashMap();
        params.put("code",form.getCode());
        params.put("state",form.getState());

        employeeService.updateState(params);
        return R.ok().put("result","success");
    }

    @PostMapping("/updateEmployee")
    @ApiOperation("修改员工信息")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:UPDATE"}, logical = Logical.OR)
    public R updateEmployee(@RequestHeader("token")String token, @Valid @RequestBody UpdateEmployeeForm form){
        TbEmployee employee = new TbEmployee();
        employee.setCode(form.getCode());
        employee.setName(form.getName());
        employee.setEmail(form.getEmail());
        employee.setTel(form.getTel());
        employee.setHiredate(DateUtil.parseDate(form.getHiredate()));
        employee.setRole(form.getRole());
        employee.setDeptId(form.getDept_id());
        employeeService.updateEmployee(employee);
        return R.ok().put("result","success");
    }



    @PostMapping("/deleteEmployee")
    @ApiOperation("删除员工信息")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:DELETE"}, logical = Logical.OR)
    public R updateEmployee(@RequestHeader("token")String token, @Valid @RequestBody DeleteEmployeeForm form){

        employeeService.deleteEmployee(form.getCode());
        return R.ok().put("result","success");
    }

    @PostMapping("/searchEmployee")
    @ApiOperation("删除员工信息")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:SELECT"}, logical = Logical.OR)
    public R searchEmployee(@RequestHeader("token")String token, @Valid @RequestBody SearchEmployeeForm form){
        return R.ok().put("result",employeeService.searchEmployee(form.getCode()));
    }


}
