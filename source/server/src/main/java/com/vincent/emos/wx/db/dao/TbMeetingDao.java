package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.TbMeeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.shiro.crypto.hash.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface TbMeetingDao {
    int insertMeeting(TbMeeting meeting);

    ArrayList<HashMap> searchMeetingListByPage(HashMap map);

    boolean searchMeetingMembersInSameDept(String uuid);

    HashMap searchMeetingByUUID(String uuid);

    /**
     * 会议涉及的部门
     * @param uuid
     * @return
     */
    ArrayList<Integer> searchMeetingDepts(String uuid);

    int updateMeetingInstanceId(HashMap map);


    HashMap searchMeetingById(int id);

    ArrayList<HashMap> searchMeetingMembers(int id);

    int updateMeetingInfo(HashMap param);
    int updateMeetingStatus(HashMap param);

    int deleteMeetingById(int id);

    List<String> searchUserMeetingInMonth(HashMap param);

    HashMap searchMeetingStatus(String uuid);

    int updateMeetingStatusByUUID(HashMap hashMap);

}