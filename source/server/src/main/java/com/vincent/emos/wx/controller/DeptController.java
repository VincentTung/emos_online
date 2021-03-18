package com.vincent.emos.wx.controller;

import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.controller.form.DeleteDeptForm;
import com.vincent.emos.wx.controller.form.InsertDeptForm;
import com.vincent.emos.wx.controller.form.UpdateDeptForm;
import com.vincent.emos.wx.db.pojo.TbDept;
import com.vincent.emos.wx.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/dept")
@Api("部门网络接口")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping("/searchAllDepts")
    @ApiOperation("查询所有部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:SELECT"}, logical = Logical.OR)
    public R searchAllDepts() {
        ArrayList list = deptService.searchAllDepts();
        return R.ok().put("result", list);
    }

    @PostMapping("/deleteDept")
    @ApiOperation("删除部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:DELETE"}, logical = Logical.OR)
    public R deleteDept(@Valid @RequestBody DeleteDeptForm form) {
        deptService.deleteDept(form.getId());
        return R.ok().put("result", "success");
    }

    @PostMapping("/updateDept")
    @ApiOperation("更新部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:DELETE"}, logical = Logical.OR)
    public R updateDept(@Valid @RequestBody UpdateDeptForm form) {
        TbDept dept = new TbDept();
        dept.setId(form.getId());
        dept.setDeptName(form.getDeptName());
        deptService.updateDept(dept);
        return R.ok().put("result", "success");
    }


    @PostMapping("/insertDept")
    @ApiOperation("新建部门")
    @RequiresPermissions(value = {"ROOT", "DEPT:INSERT"}, logical = Logical.OR)
    public R insertDept(@Valid @RequestBody InsertDeptForm form) {
        deptService.insertDept(form.getDeptName());
        return R.ok().put("result", "success");
    }
}