package com.vincent.emos.wx.controller;

import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.controller.form.TestSayHelloForm;
import io.swagger.annotations.Api;
import io.swagger.annotations .ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
@Api("测试Web接口")
public class TestController {

    @PostMapping("/sayHello")
    @ApiOperation("一个测试的接口")
    public R sayHello(@Valid @RequestBody TestSayHelloForm form){
        return R.ok().put("message","HelloWorld-"+form.getName());
    }


    /**
     * 权限测试
     * @return
     */
    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    @RequiresPermissions(value = {"A","B"},logical = Logical.OR)
    public  R addUser(){
        return R.ok("用户添加成功");
    }
}
