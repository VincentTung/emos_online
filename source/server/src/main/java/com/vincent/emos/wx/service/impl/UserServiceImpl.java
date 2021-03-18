package com.vincent.emos.wx.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.vincent.emos.wx.db.dao.TbDeptDao;
import com.vincent.emos.wx.db.dao.TbEmployeeDao;
import com.vincent.emos.wx.db.dao.TbUserDao;
import com.vincent.emos.wx.db.pojo.ContactList;
import com.vincent.emos.wx.db.pojo.MessageEntity;
import com.vincent.emos.wx.db.pojo.TbEmployee;
import com.vincent.emos.wx.db.pojo.TbUser;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.EmployeeService;
import com.vincent.emos.wx.service.UserService;
import com.vincent.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.temporal.Temporal;
import java.util.*;

@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {

    public static final String ROOT_REGISTER_CODE = "1000";

    @Value("${wx.app-id}")
    private String wxAppId;

    @Value("${wx.app-secret}")
    private String wxApSecret;

    @Autowired
    private TbUserDao userDao;


    @Autowired
    private MessageTask messageTask;
    @Autowired
    private TbDeptDao  deptDao;

    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", wxAppId);
        map.put("secret", wxApSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("临时登陆凭证错误");
        }
        return openId;
    }

    @Override
    public int registerUser(String registerCode, String code, String nickName, String photo, TbEmployee employee) {

        String openId = getOpenId(code);
        if (registerCode.equals(ROOT_REGISTER_CODE)) {

            boolean hasRootUser = userDao.haveRootUser();
            if (!hasRootUser) {
                //注册管理员
                TbUser user = getUserEntity(true, openId, code, nickName, photo,employee);
                userDao.insertUser(user);
                int id = user.getId();

                MessageEntity entity = new MessageEntity();
                entity.setSenderId(0);
                entity.setSenderName("系统消息");
                entity.setUuid(IdUtil.simpleUUID());
                entity.setMsg("欢迎您注册成为超级管理员，请及时更新您的员工个人信息");
                entity.setSendTime(new Date());

               messageTask.sendAsync(id + "", entity);

            } else {
                throw new EmosException("创建超级管理员失败");
            }
        } else {
            TbUser user = getUserEntity(false, openId, code, nickName, photo,employee);
            userDao.insertUser(user);
            int userId = user.getId();
            MessageEntity entity = new MessageEntity();
            entity.setSenderId(0);
            entity.setSenderName("系统消息");
            entity.setUuid(IdUtil.simpleUUID());
            entity.setMsg("欢迎来到emos，请及时更新您的员工个人信息");
            entity.setSendTime(new Date());
           messageTask.sendAsync(userId + "", entity);


        }
        return userDao.searchIdByOpenId(openId);
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        return userDao.searchUserPermissions(userId);
    }

    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = userDao.searchIdByOpenId(openId);
        //接收消息
        messageTask.receiverAsync(id + "");
        if (id == null) {
            throw new EmosException("账户不存在");
        }
        return id;
    }

    @Override
    public TbUser searchById(int userId) {
        return userDao.searchById(userId);
    }

    @Override
    public String searchUserHireDate(int userId) {
        return userDao.searchUserHireDate(userId);
    }

    @Override
    public HashMap searchUserSummary(int userId) {
        return userDao.searchUserSummary(userId);
    }

    @Override
    public int updateUserInfo(TbUser user) {
        return userDao.updateUserInfo(user);
    }

    @Override
    public ArrayList<HashMap> searchUserGroupByDept(String keyword) {
        ArrayList<HashMap> list_1 = deptDao.searchDeptMembers(keyword);
        ArrayList<HashMap> list_2 = deptDao.searchUserGroupByDept(keyword);
        for(HashMap map_1: list_1){
            long deptId = (long) map_1.get("id");
            ArrayList members =new ArrayList();
            for(HashMap map_2: list_2){
                long id = (long) map_2.get("deptId");
                if(deptId == id){
                    members.add(map_2);
                }
            }
            map_1.put("members",members);
        }

        return list_1;
    }

    @Override
    public ArrayList<HashMap> searchMembers(List param) {
        return userDao.searchMembers(param);
    }

    @Override
    public List<HashMap> selectUserPhotoAndName(List param) {
        return userDao.selectUserPhotoAndName(param);
    }

    @Override
    public TbUser searchUserById(int id) {
        return userDao.searchById(id);
    }

    @Override
    public int dimissionEmployee(int id) {
        return userDao.dimissionEmployee(id);
    }

    @Override
    public List<ContactList> searchUserListGroupByDept() {
        return userDao.searchUserListGroupByDept();
    }

    private HashMap getUserParams(boolean isRootUser, String openId, String code, String nickName, String photo) {
        HashMap params = new HashMap();
        params.put("openId", openId);
        params.put("nickname", nickName);
        params.put("photo", photo);
        params.put("role", "[0]");
        params.put("status", 1);
        params.put("createTime", new Date());
        params.put("root", isRootUser);
        return params;
    }

    private TbUser getUserEntity(boolean isRootUser, String openId, String code, String nickName, String photo, TbEmployee employee) {

        TbUser user = new TbUser();
        user.setRoot(isRootUser);
        user.setOpenId(openId);
        user.setNickname(nickName);
        user.setPhoto(photo);
        user.setCreateTime(new Date());
        user.setStatus((byte) 1);
        user.setSex(employee.getSex());
        user.setName(employee.getName());
        user.setTel(employee.getTel());
        user.setEmail(employee.getEmail());
        user.setDeptId(employee.getDeptId());
        user.setHiredate(employee.getHiredate());
        user.setRole(employee.getRole());
        return user;
    }
}
