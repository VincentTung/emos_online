package com.vincent.emos.wx.service;

import com.vincent.emos.wx.db.pojo.TbMeeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MeetingService {
    void insertMeeting(TbMeeting meeting);


    ArrayList<HashMap> searchMeetingListByPage(HashMap map);

    HashMap searchMeetingById(int id);

    void updateMeetingInfo(HashMap param);

    void deleteMeetingById(int id);
    List<String> searchUserMeetingInMonth(HashMap param);


    ArrayList<HashMap> searchAlreadyApprovalMeeting(int userId);
    ArrayList<HashMap> searchNeedApprovalMeeting(int userId);


    int approvalMeeting(HashMap hashMap);

    Integer searchMeetingStatus(String uuid);
}
