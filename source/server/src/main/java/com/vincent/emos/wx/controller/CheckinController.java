package com.vincent.emos.wx.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.vincent.emos.wx.SystemConstants;
import com.vincent.emos.wx.common.util.R;
import com.vincent.emos.wx.config.shiro.JwtUtil;
import com.vincent.emos.wx.controller.form.CheckinForm;
import com.vincent.emos.wx.controller.form.SearchMonthCheckinForm;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.CheckinService;
import com.vincent.emos.wx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/checkin")
@Api("签到模块web接口")
@Slf4j
public class CheckinController {

    private static final String JPG = ".jpg";

    @Autowired
    private SystemConstants systemConstants;
    @Autowired
    private CheckinService checkinService;
    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtil jwtUtil;

    @Value("${emos.image-folder}")
    private String imageFolder;

    @GetMapping("/validCanCheckin")
    @ApiOperation("查看用户今天是否可以签到")
    public R validCanCheckin(@RequestHeader("token") String token){
        int userId = jwtUtil.getUserId(token);
        String result = checkinService.validCanCheckin(userId, DateUtil.today());
        return R.ok(result);
    }

    @PostMapping("/checkin")
    @ApiOperation("签到")
    public R checkin(@Valid CheckinForm form, @RequestParam("photo")MultipartFile file,@RequestHeader("token") String token){
        int userId = jwtUtil.getUserId(token);
        String fileName = file.getOriginalFilename().toLowerCase();
        String path = imageFolder+ "/"+fileName;
        if(!fileName.endsWith(JPG)){
            FileUtil.del(path);
            return R.error("必须提交JPG格式图片");
        }else{

            try {
                file.transferTo(Paths.get(path));
                checkinService.checkin(form,userId,path);
                return R.ok("签到成功");
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new EmosException("保存图片错误");
            }finally {
                FileUtil.del(path);
            }


        }

    }

    @PostMapping("/createFaceModel")
    @ApiOperation("创建人脸模型")
    public R createFaceModel(@RequestParam("photo") MultipartFile file,@RequestHeader("token") String token){

        int userId = jwtUtil.getUserId(token);
        if(file == null){
            return R.error("没有上传文件");
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        String path = imageFolder+"/"+fileName;

        if(!fileName.endsWith(JPG)){
            FileUtil.del(path);
            return R.error("必须提交JPG格式图片");
        }else{
            try {
                file.transferTo(Paths.get(path));
                checkinService.createFaceModel(userId,path);
                return R.ok("人脸建模成功");
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new EmosException("保存图片错误");
            }finally {
                FileUtil.del(path);
            }

        }
    }

    /**
     *
     * @param token
     * @return
     */
    @GetMapping("/searchTodayCheckin")
    @ApiOperation("查询用户当日签到数据")

    public R searchTodayCheckin(@RequestHeader("token") String token){
        int userId = jwtUtil.getUserId(token);
        HashMap map = checkinService.searchTodayCheckin(userId);
        map.put("attendanceTime",systemConstants.attendanceTime);
        map.put("closingTime",systemConstants.closingTime);
        long days = checkinService.searchCheckingDays(userId);
        map.put("checkinDays",days);
        //判断日期是否在用户入职之前
        DateTime hireDate = DateUtil.parseDate(userService.searchUserHireDate(userId));
        DateTime startDate = DateUtil.beginOfWeek(DateUtil.date());
        if(startDate.isBefore(hireDate)){
            startDate = hireDate;
        }
        DateTime endDate  = DateUtil.endOfWeek(DateUtil.date());
        map.put("startDate",startDate.toString());
        map.put("endDate",endDate.toString());
        map.put("userId",userId);
        ArrayList<HashMap> list = checkinService.searchWeekCheckin(map);
        map.put("weekCheckin",list);
        return R.ok().put("result",map);
    }


    /**
     *
     * @param form
     * @param token
     * @return
     */
    @PostMapping("/searchMonthCheckin")
    @ApiOperation("查询用户某月签到数据")
    public R searchMonthCheckin(@Valid @RequestBody SearchMonthCheckinForm form,@RequestHeader("token") String token){

        int userId = jwtUtil.getUserId(token);
        //入职日期
        DateTime hireDate= DateUtil.parse(userService.searchUserHireDate(userId));
        //个位月份的话前面补0
        String month = form.getMonth()< 10 ?"0"+form.getMonth():""+form.getMonth();
        //查询的开始七日
        DateTime startDate = DateUtil.parseDate(form.getYear()+"-"+month+"-01");
        //比较查询日期和入职日期，如果查询入职日起之前的考勤就抛出异常
        if(startDate.isBefore(DateUtil.beginOfMonth(hireDate))){
            throw new EmosException("只能查询考勤之后的日期数据");
        }
        //如果查询日志是和入职日期是同月份 则更改开始日期为入职日期
        if(startDate.isBefore(hireDate)){
            startDate = hireDate;
        }
        //查询的结束日期
        DateTime endDate = DateUtil.endOfMonth(startDate);

        HashMap param = new HashMap();
        param.put("startDate",startDate.toString());
        param.put("endDate",endDate.toString());
        param.put("userId",userId);

        ArrayList<HashMap> list = checkinService.searchMonthChecking(param);
        int sum_1 = 0, sum_2 = 0, sum_3 = 0;
        for (HashMap<String,String> map :list){

            String type = map.get("type");
            String status = map.get("status");
            if("工作日".equals(type)){
                if("正常".equals(status)){
                    sum_1++;
                }else if("迟到".equals(status)){
                    sum_2++;
                }else if("缺勤".equals(status)){
                    sum_3++;
                }
            }
        }

        return R.ok().put("list",list).put("sum_1",sum_1).put("sum_2",sum_2).put("sum_3",sum_3);  

    }
}
