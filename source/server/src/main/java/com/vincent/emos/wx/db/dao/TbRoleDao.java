package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.TbRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbRoleDao {
    /**
     * 查询用户的角色权限
     *
     * @param id
     * @return
     */
    ArrayList<HashMap> searchRoleOwnPermission(int id);

    ArrayList<HashMap> searchAllPermission();

    int insertRole(TbRole role);

    int updateRolePermissions(TbRole role);

    ArrayList<TbRole> searchAllRoles();

    int deleteRole(int id);
}