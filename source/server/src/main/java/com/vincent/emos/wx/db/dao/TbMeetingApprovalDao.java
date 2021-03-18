package com.vincent.emos.wx.db.dao;

import com.vincent.emos.wx.db.pojo.TbMeetingApproval;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
@Mapper
public interface TbMeetingApprovalDao {

    int insertApproval(TbMeetingApproval record);
    int updateApprovals(HashMap map);
    int deleteApprovalByUUID(String uuid);


    ArrayList<HashMap> searchNeedGMApprovalMeeting();
    ArrayList<HashMap> searchGMAlreadyApprovalMeeting();

    ArrayList<HashMap> searchMgrAlreadyApprovalMeeting(int userId);
    ArrayList<HashMap> searchNeedMgrApprovalMeeting(int userId);

    TbMeetingApproval searchApprovalByUUID(String uuid);
}