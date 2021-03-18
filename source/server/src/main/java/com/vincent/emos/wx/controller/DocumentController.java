package com.vincent.emos.wx.controller;

import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.db.pojo.DocumentFile;
import com.vincent.emos.wx.tencentcloud.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document")
@Api("文档管理")
public class DocumentController {


    @Autowired
    private StorageService storageService;

    @GetMapping("/filelist")
    @ApiOperation("查询用户摘要信息")
    public R fileList(@RequestHeader("token") String token){

        List<DocumentFile> list = storageService.listFiles();
        return R.ok().put("list",list);

    }

}
