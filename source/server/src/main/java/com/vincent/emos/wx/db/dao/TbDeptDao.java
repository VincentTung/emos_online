package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.TbDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbDeptDao {

    /**
     * @param keyword
     * @return
     */
    ArrayList<HashMap> searchDeptMembers(String keyword);

    /**
     * @param keyword
     * @return
     */
    ArrayList<HashMap> searchUserGroupByDept(String keyword);


    ArrayList<TbDept> searchAllDepts();

    int insertDept(String deptName);


    int deleteDept(int id);

    int updateDept(TbDept tbDept);

}