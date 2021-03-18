package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.TbFaceModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbFaceModelDao {
    String searchFaceModel(int userId);
    void insert(TbFaceModel faceModel);
    void deleteFaceModel(int userId);
}