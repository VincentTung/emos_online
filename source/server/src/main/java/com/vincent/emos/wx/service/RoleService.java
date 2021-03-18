package com.vincent.emos.wx.service;

import com.vincent.emos.wx.db.pojo.TbRole;

import java.util.ArrayList;
import java.util.HashMap;

public interface RoleService {
    ArrayList<HashMap> searchRoleOwnPermission(int id);

    ArrayList<HashMap> searchAllPermission();

    void insertRole(TbRole role);

    void updateRolePermissions(TbRole role);


    ArrayList<TbRole> searchAllRoles();

    void  deleteRole(int id);
}
