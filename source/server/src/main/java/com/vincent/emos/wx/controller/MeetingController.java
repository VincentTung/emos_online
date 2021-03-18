package com.vincent.emos.wx.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.config.shiro.JwtUtil;
import com.vincent.emos.wx.controller.form.*;
import com.vincent.emos.wx.db.pojo.TbMeeting;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/meeting")
@Api("会议接口")
public class MeetingController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/searchMeetingListByPage")
    @ApiOperation("查询会议列表分页数据")
    public R  searchMeetingListByPage(@Valid @RequestBody SearchMyMeetingListByPageForm form, @RequestHeader("token")String token){
        int userId  = jwtUtil.getUserId(token);
        int page = form.getPage();
        int length  =form.getLength();
        long start = (page -1)*length;
        HashMap map = new HashMap();
        map.put("userId",userId);
        map.put("start",start);
        map.put("length",length);
        ArrayList list = meetingService.searchMeetingListByPage(map);
        return R.ok().put("result",list);

    }

    @PostMapping("/insertMeeting")
    @ApiOperation("新建会议")
    @RequiresPermissions(value = {"ROOT","MEETING:INSERT"},logical = Logical.OR)
    public R insertMeeting(@Valid @RequestBody InsertMeetingForm form,@RequestHeader("token")String token){

        if(form.getType() == 2&&(form.getPlace()==null||form.getPlace().length()==0)){

            throw new EmosException("线下会议地点不能为空");
        }
        DateTime d1 = DateUtil.parse(form.getDate()+" "+form.getStart()+":00");
        DateTime d2 = DateUtil.parse(form.getDate()+" "+form.getEnd()+":00");
        if(d2.isBefore(d1)){
            throw new EmosException("会议结束时间必须大于开始时间");
        }
        if(!JSONUtil.isJsonArray(form.getMembers())){
            throw new EmosException("members不是JSON数组");
        }
        TbMeeting meeting = new TbMeeting();
        meeting.setUuid(UUID.randomUUID().toString(true));
        meeting.setCreatorId((long) jwtUtil.getUserId(token));
        meeting.setTitle(form.getTitle());
        meeting.setDate(form.getDate());
        meeting.setPlace(form.getPlace());
        meeting.setStart(form.getStart()+":00");
        meeting.setEnd(form.getEnd()+":00");
        meeting.setType((short)form.getType());
        meeting.setMembers(form.getMembers());
        meeting.setDesc(form.getDesc());
        meeting.setStatus((short)1);

        meetingService.insertMeeting(meeting);
        return R.ok().put("result","success");
    }

    @PostMapping("/searchMeetingById")
    @ApiOperation("根据ID查询会议")
    @RequiresPermissions(value = {"ROOT", "MEETING:SELECT"}, logical = Logical.OR)
    public R searchMeetingById(@Valid @RequestBody SearchMeetingByIdForm form) {
        HashMap map = meetingService.searchMeetingById(form.getId());
        return R.ok().put("result", map);
    }



    @PostMapping("/updateMeetingInfo")
    @ApiOperation("更新会议内容")
    @RequiresPermissions(value = {"ROOT", "MEETING:UPDATE"}, logical = Logical.OR)
    public R updateMeetingInfo(@Valid @RequestBody UpdateMeetingInfoForm form){
        if(form.getType() == 2 && (form.getPlace()==null ||form.getPlace().length()==0)){
            throw  new EmosException("线下会议地点不能为空");
        }
        DateTime d1 = DateUtil.parse(form.getDate() + " " + form.getStart() + ":00");
        DateTime d2 = DateUtil.parse(form.getDate() + " " + form.getEnd() + ":00");
        if (d2.isBeforeOrEquals(d1)) {
            throw new EmosException("结束时间必须大于开始时间");
        }
        if (!JSONUtil.isJsonArray(form.getMembers())) {
            throw new EmosException("members不是JSON数组");
        }
        HashMap param = new HashMap();
        param.put("title", form.getTitle());
        param.put("date", form.getDate());
        param.put("place", form.getPlace());
        param.put("start", form.getStart() + ":00");
        param.put("end", form.getEnd() + ":00");
        param.put("type", form.getType());
        param.put("members", form.getMembers());
        param.put("desc", form.getDesc());
        param.put("id", form.getId());
        param.put("instanceId", form.getInstanceId());
        param.put("status", 1);
        meetingService.updateMeetingInfo(param);
        return R.ok().put("result", "success");
    }


    @PostMapping("/deleteMeetingById")
    @ApiOperation("根据ID删除会议")
    @RequiresPermissions(value = {"ROOT", "MEETING:DELETE"}, logical = Logical.OR)
    public R deleteMeetingById(@Valid @RequestBody DeleteMeetingByIdForm form) {
         meetingService.deleteMeetingById(form.getId());
        return R.ok().put("result", "success");
    }


    @PostMapping("/searchUserMeetingInMonth")
    @ApiOperation("查询某月用户的会议日期列表")
    public R searchUserMeetingInMonth(@Valid @RequestBody SearchUserMeetingInMonthForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap param = new HashMap();
        param.put("userId", userId);
        param.put("express", form.getYear()+"/"+form.getMonth());
        List<String> list = meetingService.searchUserMeetingInMonth(param);
        return R.ok().put("result", list);
    }

}
