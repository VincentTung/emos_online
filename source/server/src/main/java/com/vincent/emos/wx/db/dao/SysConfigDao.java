package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Mapper
public interface SysConfigDao {
    List<SysConfig> selectAllParams();
}