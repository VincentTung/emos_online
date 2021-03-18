package com.vincent.emos.wx.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbWorkdayDao {
  Integer searchTodayIsWorkday();
  ArrayList<String> searchWorkDayInRange(HashMap param);
}