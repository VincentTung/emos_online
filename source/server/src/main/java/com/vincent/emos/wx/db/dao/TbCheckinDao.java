package com.vincent.emos.wx.db.dao;
import com.vincent.emos.wx.db.pojo.TbCheckin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.shiro.crypto.hash.Hash;

import java.util.ArrayList;
import java.util.HashMap;
@Mapper
public interface TbCheckinDao {
  Integer haveCheckin(HashMap params);
  void insert(TbCheckin tbCheckin);
  //今天签到状态
  HashMap searchTodayCheckin(int userId);
  //总签到天数
  long searchCheckinDays(int userId);
  //时间段内的签到数据
  ArrayList<HashMap> searchWeekCheckin(HashMap param);

  int searchHighRiskCheckin(int userId);
  int searchMiddleRiskCheckin(int userId);

  int searchRiskCount(HashMap param);
}