package com.vincent.emos.wx.service;

import com.vincent.emos.wx.db.pojo.ContactList;
import com.vincent.emos.wx.db.pojo.TbEmployee;
import com.vincent.emos.wx.db.pojo.TbUser;
import org.apache.shiro.crypto.hash.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 *
 * 用户业务类
 *
 */
public interface UserService {

    /**
     * 注册用户
     * @param registerCode 激活码
     * @param code 微信临时授权码
     * @param nickName 微信昵称
     * @param photo 微信头像
     */
    int registerUser(String registerCode, String code, String nickName, String photo, TbEmployee employee);


    /**
     * 查询用户权限
     * @param userId
     * @return
     */
    Set<String> searchUserPermissions(int userId);


    /**
     *
     * @param code 微信临时授权码
     * @return
     */
    Integer login(String code);


    TbUser searchById(int userId);

    /**
     * 查询入职日期
     * @param userId
     * @return
     */
    String searchUserHireDate(int userId);


    /**
     * 查询用户的信息
     * @param userId
     * @return
     */
    HashMap searchUserSummary(int userId);

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    int updateUserInfo(TbUser user);


    ArrayList<HashMap> searchUserGroupByDept(String keyword);

    ArrayList<HashMap> searchMembers(List param);
    List<HashMap> selectUserPhotoAndName(List param);

    TbUser searchUserById(int id);

    int dimissionEmployee(int id);

    List<ContactList> searchUserListGroupByDept();
}
