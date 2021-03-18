package com.vincent.emos.wx.db.dao;


import com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer;
import com.vincent.emos.wx.db.pojo.ContactList;
import com.vincent.emos.wx.db.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.shiro.crypto.hash.Hash;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Mapper
public interface TbUserDao {
    boolean haveRootUser();

    int insert(HashMap params);

    int insertUser(TbUser user);

    Integer searchIdByOpenId(String openId);

    Set<String> searchUserPermissions(int userId);

    List<ContactList> searchUserListGroupByDept();
    TbUser searchById(int userId);

    HashMap searchNameAndDept(int userId);

    String searchUserHireDate(int userId);

    HashMap searchUserSummary(int userId);

    int updateUserInfo(TbUser user);

    int updateUserInfoBySys(TbUser user);

    ArrayList<HashMap> searchMembers(List params);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    HashMap searchUserInfo(int userId);

    /**
     * 查询部门经理id
     *
     * @param id
     * @return
     */
    Integer searchDeptManagerId(int id);

    /**
     * 查询总经理id
     *
     * @return
     */
    int searchGmId();

    List<HashMap> selectUserPhotoAndName(List param);

    int searchDeptManagerIdByDeptId(int dept_id);

    int dimissionEmployee(int id);
}