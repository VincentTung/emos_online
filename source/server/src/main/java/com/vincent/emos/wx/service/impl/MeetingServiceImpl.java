package com.vincent.emos.wx.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.vincent.emos.wx.db.dao.TbMeetingApprovalDao;
import com.vincent.emos.wx.db.dao.TbMeetingDao;
import com.vincent.emos.wx.db.dao.TbUserDao;
import com.vincent.emos.wx.db.pojo.MessageEntity;
import com.vincent.emos.wx.db.pojo.TbMeeting;
import com.vincent.emos.wx.db.pojo.TbMeetingApproval;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.MeetingService;
import com.vincent.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private TbMeetingDao meetingDao;

    @Autowired
    private TbMeetingApprovalDao approvalDao;
    @Autowired
    private TbUserDao userDao;


    @Autowired
    private MessageTask messageTask;

    //    通知回调地址
    private String receiveNotify;
    //慕课网授权字符串
    private String code;


    @Override
    public void insertMeeting(TbMeeting meeting) {
        int row = meetingDao.insertMeeting(meeting);
        long id = ((BigInteger) meetingDao.searchMeetingStatus(meeting.getUuid()).get("id")).longValue();
        if (row != 1) {
            throw new EmosException("会议添加失败");
        }
        startMeetingWorkflow(id, meeting.getUuid(), meeting.getCreatorId().intValue(),meeting.getTitle());

    }

    @Override
    public ArrayList<HashMap> searchMeetingListByPage(HashMap params) {
        ArrayList<HashMap> list = meetingDao.searchMeetingListByPage(params);
        String date = "";
        ArrayList resultList = new ArrayList();
        HashMap resultHashMap = null;
        JSONArray array = null;
        for (HashMap map : list) {
            String tempDate = map.get("date").toString();

            if (!tempDate.equals(date)) {
                date = tempDate;
                resultHashMap = new HashMap();
                resultList.add(resultHashMap);
                resultHashMap.put("date", date);
                array = new JSONArray();
                resultHashMap.put("list", array);
            }
            array.put(map);

        }
        return resultList;
    }

    @Override
    public HashMap searchMeetingById(int id) {
        HashMap map = meetingDao.searchMeetingById(id);
        ArrayList<HashMap> list = meetingDao.searchMeetingMembers(id);
        map.put("members", list);
        return map;
    }

    @Override
    public void updateMeetingInfo(HashMap param) {
        int id = (int) param.get("id");
        HashMap oldMeeting = meetingDao.searchMeetingById(id);
        String uuid = oldMeeting.get("uuid").toString();
        Integer creatorId = Integer.parseInt(oldMeeting.get("creatorId").toString());
        int row = meetingDao.updateMeetingInfo(param);
        if (row != 1) {
            throw new EmosException("会议更新失败");
        }


        // TODO: 2021/2/23 实现工作流
        approvalDao.deleteApprovalByUUID(uuid);

        //创建新的工作流
        startMeetingWorkflow(id, uuid, creatorId,oldMeeting.get("title").toString());

    }

    @Override
    public void deleteMeetingById(int id) {
        HashMap meeting = meetingDao.searchMeetingById(id); //查询会议信息
        String uuid = meeting.get("uuid").toString();
        String instanceId = meeting.get("instanceId").toString();
        DateTime date = DateUtil.parse(meeting.get("date") + " " + meeting.get("start"));
        DateTime now = DateUtil.date();
        //会议开始前20分钟，不能删除会议
        if (now.isAfterOrEquals(date.offset(DateField.MINUTE, -20))) {
            throw new EmosException("距离会议开始不足20分钟，不能删除会议");
        }
        int row = meetingDao.deleteMeetingById(id);
        if (row != 1) {
            throw new EmosException("会议删除失败");
        }

        // // TODO: 2021/2/23 删除工作流

        approvalDao.deleteApprovalByUUID(uuid);
    }

    @Override
    public List<String> searchUserMeetingInMonth(HashMap param) {
        return meetingDao.searchUserMeetingInMonth(param);
    }

    @Override
    public ArrayList<HashMap> searchAlreadyApprovalMeeting(int userId) {
        //总经理的信息
        int gmId = userDao.searchGmId();
        HashMap gmInfo = userDao.searchUserInfo(gmId);
        ArrayList<HashMap> list;
        if (userId == gmId) {
            list = approvalDao.searchGMAlreadyApprovalMeeting();
        } else {
            list = approvalDao.searchMgrAlreadyApprovalMeeting(userId);
        }

        for (HashMap map : list) {

            long creator_id = (long) map.get("creator_id");
            long finish_id = (long) map.get("last_user");


            int status = (int) map.get("status");


            boolean meetingApprovalFinished = status != 1;

            JSONArray jsonMembers = JSONUtil.parseArray(map.get("members").toString());
            JSONArray jsonApprovals = JSONUtil.parseArray(map.get("approvals").toString());


            boolean isSingleDept = jsonMembers.size() == 1;

            map.put("sameDept", isSingleDept);
            map.put("processStatus", meetingApprovalFinished ? "已结束" : "未结束");

            if (isSingleDept) {
                if (!meetingApprovalFinished) {
                    throw new EmosException("会议审批状态错误");
                } else {

//                    HashMap lastUserInfo = userDao.searchUserInfo((int) finish_id);
//                    map.put("lastUserPhoto", lastUserInfo.get("photo"));
//                    map.put("lastUserName", lastUserInfo.get("name"));
                    if (status != 2) {
                        map.put("result_1", "同意");
                        map.put("result_2", "同意");
                    } else {
                        map.put("result_1", "不同意");
                        map.put("result_2", "不同意");
                    }
                }
            } else {
//                map.put("lastUserPhoto", gmInfo.get("photo"));
//                map.put("lastUserName", gmInfo.get("name"));
                if (meetingApprovalFinished) {
                    if (status != 2) {
                        map.put("result_1", "同意");
                        map.put("result_2", "同意");

                    } else {
                        if (finish_id == userId) {
                            map.put("result_1", "不同意");
                        } else {
                            map.put("result_1", "同意");
                        }
                        map.put("result_2", "不同意");

                    }
                } else {
                    map.put("result_1", "同意");
                }
            }

        }

        return list;
    }

    @Override
    public ArrayList<HashMap> searchNeedApprovalMeeting(int userId) {

        int gmId = userDao.searchGmId();
        if (userId == gmId) {
            return approvalDao.searchNeedGMApprovalMeeting();
        } else {
            return approvalDao.searchNeedMgrApprovalMeeting(userId);
        }
    }


    @Override
    public int approvalMeeting(HashMap hashMap) {

        int userId = (int) hashMap.get("userId");
        String uuid = hashMap.get("uuid").toString();
        int option = (int) hashMap.get("option");


        TbMeetingApproval approval = approvalDao.searchApprovalByUUID(uuid);
        JSONArray jsonMembers = JSONUtil.parseArray(approval.getMembers());
        JSONArray jsonApprovals;
        if (approval.getApprovals() == null || approval.getApprovals().toString().length() == 0) {
            jsonApprovals = new JSONArray();
        } else {
            jsonApprovals = JSONUtil.parseArray(approval.getApprovals());
        }

        boolean isSingleDept = jsonMembers.size() == 1;
        //非法审批
        if (isSingleDept) {
            if (!jsonMembers.contains(userId)) {
                return 0;
            }
        }
        //重复审批
        if (jsonApprovals.contains(userId)) {
            return -1;
        }

        Integer gmId = userDao.searchGmId();


        //单部门会议
        if (isSingleDept) {
            updateMeetingStatusByUUID(uuid, option);
            jsonApprovals.put(userId);
        } else {

            //多部门会议
            //总经理审批
            if (userId == gmId) {
                //就差总经理
                if (jsonApprovals.size() == jsonMembers.size()) {
                    updateMeetingStatusByUUID(uuid, option);
                } else {
                    return -2;
                }
            }else{
                jsonApprovals.put(userId);
            }
        }

        hashMap.put("approvals", jsonApprovals.toString());
        int result =  approvalDao.updateApprovals(hashMap);
        if(jsonApprovals.size() == jsonMembers.size() && option == 1 && result > 0){
            HashMap meetingInfo = meetingDao.searchMeetingByUUID(uuid);
            sendMeetingNeedApprovalMsg(gmId,meetingInfo.get("title").toString());
        }

        return result;

    }


    private void updateMeetingStatusByUUID(String uuid, int option) {
        HashMap param = new HashMap();
        param.put("uuid", uuid);
        param.put("status", option == 0 ? 2 : 3);
        meetingDao.updateMeetingStatusByUUID(param);
    }

    @Override
    public Integer searchMeetingStatus(String uuid) {
        return (Integer) meetingDao.searchMeetingStatus(uuid).get("status");
    }


    /**
     * @param uuid      视频会议室ID
     * @param creatorId
     * @param
     * @param
     */
    private void startMeetingWorkflow(long meetingId, String uuid, int creatorId,String title) {

//        // TODO: 2021/2/23 自己实现会议工作流

        TbMeetingApproval approval = new TbMeetingApproval();
        approval.setUuid(uuid);

        Integer managerId = userDao.searchDeptManagerId(creatorId);
        Integer gmId = userDao.searchGmId();

        JSONArray json_members = new JSONArray();
        JSONArray json_approvals = new JSONArray();
        if (creatorId == gmId) {
            //如果创建者是总经理
            json_members.put(creatorId);
            json_approvals.put(creatorId);
            approval.setLast_user((long) creatorId);

            HashMap map = new HashMap();
            map.put("id", meetingId);
            map.put("status", 3);
            meetingDao.updateMeetingStatus(map);

        } else {
            //查询所有部门
            ArrayList<Integer> list = meetingDao.searchMeetingDepts(uuid);
            for (Integer deptId : list) {
                Integer deptMgrId = userDao.searchDeptManagerIdByDeptId(deptId);
                if (deptMgrId != null) {
                    json_members.put(deptMgrId);
                    sendMeetingNeedApprovalMsg(deptId,title);
                }
            }
            //如果创建者是部门经理
            if (creatorId == managerId) {
                json_approvals.put(creatorId);
                approval.setLast_user((long) creatorId);
                if (list.size() == 1) {
                    HashMap map = new HashMap();
                    map.put("id", meetingId);
                    map.put("status", 3);
                    meetingDao.updateMeetingStatus(map);
                }
            } else {

            }
        }

        approval.setMembers(json_members.toString());
        approval.setApprovals(json_approvals.toString());
        approvalDao.insertApproval(approval);

    }

    private void sendMeetingNeedApprovalMsg(int toUserId,String meetingTitle){
        MessageEntity entity = new MessageEntity();
        entity.setSenderId(0);
        entity.setSenderName("会议审批");
        entity.setUuid(IdUtil.simpleUUID());
        entity.setMsg("您有一条会议("+meetingTitle+")待审批");
        entity.setSendTime(new Date());
        messageTask.sendAsync(toUserId+ "", entity);
    }
}
