package com.vincent.emos.wx.controller;

import cn.hutool.json.JSONUtil;
import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.controller.form.DeleteRoleForm;
import com.vincent.emos.wx.controller.form.InsertRoleForm;
import com.vincent.emos.wx.controller.form.SearchRoleOwnPermissionForm;
import com.vincent.emos.wx.controller.form.UpdateRolePermissionsForm;
import com.vincent.emos.wx.db.pojo.TbRole;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/role")
@Api("角色模块网络接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/searchRoleOwnPermission")
    @ApiOperation("查询角色权限列表")
    @RequiresPermissions(value = {"ROOT", "ROLE:SELECT"}, logical = Logical.OR)
    public R searchRoleOwnPermission(@Valid @RequestBody SearchRoleOwnPermissionForm form) {
        ArrayList list = roleService.searchRoleOwnPermission(form.getId());
        return R.ok().put("result", list);
    }

    @GetMapping("/searchAllPermission")
    @ApiOperation("查询所有权限")
    @RequiresPermissions(value = {"ROOT", "ROLE:SELECT"}, logical = Logical.OR)
    public R searchAllPermission() {
        ArrayList list = roleService.searchAllPermission();
        return R.ok().put("result", list);
    }

    @GetMapping("/searAllRoles")
    @ApiOperation("查询所有角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:SELECT"}, logical = Logical.OR)
    public R searchAllRoles() {
        ArrayList list = roleService.searchAllRoles();
        return R.ok().put("result", list);
    }


    @PostMapping("/insertRole")
    @ApiOperation("添加角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:INSERT"}, logical = Logical.OR)
    public R insertRole(@Valid @RequestBody InsertRoleForm form) {
        if (!JSONUtil.isJsonArray(form.getPermissions())) {
            throw new EmosException("权限不是数组格式");
        }
        TbRole entity = new TbRole();
        entity.setRoleName(form.getRoleName());
        entity.setPermissions(form.getPermissions());
        roleService.insertRole(entity);
        return R.ok().put("result", "success");
    }

    @PostMapping("/updateRolePermissions")
    @ApiOperation("修改角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:UPDATE"}, logical = Logical.OR)
    public R updateRolePermissions(@Valid @RequestBody UpdateRolePermissionsForm form) {
        if (!JSONUtil.isJsonArray(form.getPermissions())) {
            throw new EmosException("权限不是数组格式");
        }
        TbRole entity = new TbRole();
        entity.setId(form.getId());
        entity.setPermissions(form.getPermissions());
        roleService.updateRolePermissions(entity);
        return R.ok().put("result", "success");
    }

    @PostMapping("/deleteRole")
    @ApiOperation("删除角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:DELETE"}, logical = Logical.OR)
    public R deleteRole(@Valid @RequestBody DeleteRoleForm form) {
        roleService.deleteRole(form.getId());
        return R.ok().put("result", "success");
    }
}