package com.vincent.emos.wx.service.impl;

import cn.hutool.json.JSONObject;
import com.vincent.emos.wx.db.dao.TbRoleDao;
import com.vincent.emos.wx.db.pojo.TbRole;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Service
@Slf4j
@Scope("prototype")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private TbRoleDao roleDao;

    @Override
    public ArrayList<HashMap> searchRoleOwnPermission(int id) {
        return handleData(roleDao.searchRoleOwnPermission(id));
    }

    /**
     * 将查询结果按照模块名称分组
     *
     * @param list
     * @return
     */
    private ArrayList<HashMap> handleData(ArrayList<HashMap> list) {
        ArrayList permsList = new ArrayList();
        ArrayList actionList = new ArrayList();
        HashSet set = new HashSet();
        HashMap data = new HashMap();
        for (HashMap map : list) {
            long permissionId = (Long) map.get("id");
            String moduleName = (String) map.get("moduleName");
            String actionName = (String) map.get("actionName");
            String selected = map.get("selected").toString();
            if (set.contains(moduleName)) {
                JSONObject json = new JSONObject();
                json.set("id", permissionId);
                json.set("actionName", actionName);
                json.set("selected", selected.equals("1") ? true : false);
                actionList.add(json);
            } else {
                set.add(moduleName);
                data = new HashMap();
                data.put("moduleName", moduleName);
                actionList = new ArrayList();
                JSONObject json = new JSONObject();
                json.set("id", permissionId);
                json.set("actionName", actionName);
                json.set("selected", selected.equals("1") ? true : false);
                actionList.add(json);
                data.put("action", actionList);
                permsList.add(data);
            }
        }
        return permsList;
    }


    @Override
    public ArrayList<HashMap> searchAllPermission() {
        ArrayList<HashMap> list = roleDao.searchAllPermission();
        list = handleData(list);
        return list;
    }

    @Override
    public void insertRole(TbRole role) {
        int row = roleDao.insertRole(role);
        if (row != 1) {
            throw new EmosException("添加角色失败");
        }
    }

    @Override
    public void updateRolePermissions(TbRole role) {
        int row = roleDao.updateRolePermissions(role);
        if (row != 1) {
            throw new EmosException("修改角色失败");
        }
    }

    @Override
    public ArrayList<TbRole> searchAllRoles() {
        return roleDao.searchAllRoles();
    }

    @Override
    public void deleteRole(int id) {
        int row = roleDao.deleteRole(id);
        if(row !=1){
            throw new EmosException("删除角色失败");
        }
    }
}
