package com.vincent.emos.wx.controller;

import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.config.shiro.JwtUtil;
import com.vincent.emos.wx.controller.form.ApprovalMeetingForm;
import com.vincent.emos.wx.controller.form.SearchMyMeetingListByPageForm;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/meetingApproval")
@Api("会议审批")
@Slf4j
public class MeetingApprovalController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MeetingService meetingService;


    @PostMapping("/searchNeedApproval")
    @ApiOperation("查询需要审批的会议")
    public R searchNeedApproval( @RequestHeader("token")String token){
        int userId  = jwtUtil.getUserId(token);


        ArrayList list = meetingService.searchNeedApprovalMeeting(userId);
        return R.ok().put("result",list);
    }


    @PostMapping("/searchAlreadyApproval")
    @ApiOperation("查询需要审批的会议")
    public R searchAlreadyApproval( @RequestHeader("token")String token){
        int userId  = jwtUtil.getUserId(token);
        ArrayList list = meetingService.searchAlreadyApprovalMeeting(userId);
        return R.ok().put("result",list);
    }

    @PostMapping("/approvalMeeting")
    @ApiOperation("审批会议")
    public R approvalMeeting(@RequestHeader("token")String token  ,@Valid @RequestBody ApprovalMeetingForm form){

        Integer meetingStatus  = meetingService.searchMeetingStatus(form.getUuid());
        if(meetingStatus == null){
            throw new EmosException("操作的会议不存在了");
        }
        //不是待审批状态
        if(meetingStatus != 1){
            throw new EmosException("该会议已经被别人审批了");
        }

        // TODO: 2021/2/25 写入members
        HashMap hashMap = new HashMap();
        hashMap.put("userId",jwtUtil.getUserId(token));
        hashMap.put("uuid",form.getUuid());
        hashMap.put("option",form.getState());
        int result =  meetingService.approvalMeeting(hashMap);

        if(result >0){
            return  R.ok().put("result","success");
        }else{
            throw new EmosException("会议审批操作异常:code"+result);
        }

    }
}
