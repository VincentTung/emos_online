package com.vincent.emos.wx.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.vincent.emos.wx.SystemConstants;
import com.vincent.emos.wx.common.util.Imagebase64;
import com.vincent.emos.wx.controller.form.CheckinForm;
import com.vincent.emos.wx.db.dao.*;
import com.vincent.emos.wx.db.pojo.TbCheckin;
import com.vincent.emos.wx.db.pojo.TbUser;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.service.CheckinService;
import com.vincent.emos.wx.task.EmailTask;
import com.vincent.emos.wx.tencentcloud.face.FaceLibraryService;
import com.vincent.emos.wx.tencentcloud.face.FaceVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
@Scope("prototype")
@Slf4j
public class CheckinServiceImpl implements CheckinService {

    private static final String STRING_HOLIDAYS = "节假日";
    private static final String STRING_WORKDAY = "工作日";
    private static final String STRING_CAN_CHECKIN = "可以考勤";
    private static final String STRING_HAS_CHECKIN = "今日已经考勤，不用重复考勤";
    private static final String STRING_EARLY_CHECKIN = "没有到上班考勤开始时间";
    private static final String STRING_LATELY_CHECKIN = "超过了上班考勤结束时间";
    private static final String STRING_CAN_NOT_CHECK_AT_HOLIDAYS = "节假日不需要考勤";
    private static final String STRING_HIGH_RISK = "高风险";
    private static final String STRING_MIDDLE_RISK = "中风险";
    private static final String STRING_LOW_RISK = "低风险";
    // 风险等级
    private static final int RISK_LEVEL_HIGH = 3;
    private static final int RISK_LEVEL_MIDDLE = 2;
    private static final int RISK_LEVEL_LOW = 1;
    @Autowired
    private SystemConstants systemConstants;
    @Autowired
    private TbHolidaysDao tbHolidaysDao;
    @Autowired
    private TbWorkdayDao tbWorkdayDao;
    @Autowired
    private TbCheckinDao tbCheckinDao;
    @Autowired
    private TbUserDao tbUserDao;

    @Autowired
    private TbFaceModelDao tbFaceModelDao;

    @Autowired
    private TbCityDao tbCityDao;
    @Autowired
    private FaceLibraryService faceLibraryService;

    @Autowired
    private FaceVerifyService faceVerifyService;

    @Value("${emos.face.checkinUrl}")
    private String checkinUrl;

    @Value("${emos.face.createFaceModelUrl}")
    private String createFaceModelUrl;
    @Value("${emos.mail.hr}")
    private String hrEmail;
    @Autowired
    private EmailTask emailTask;

    @Override
    public String validCanCheckin(int userId, String date) {
        boolean bool1 = tbHolidaysDao.searchTodayIsHolidays() != null ? true : false;
        boolean bool2 = tbWorkdayDao.searchTodayIsWorkday() != null ? true : false;

        String type = STRING_WORKDAY;
        if (DateUtil.date().isWeekend()) {
            type = STRING_HOLIDAYS;
        }
        if (bool1) {
            type = STRING_HOLIDAYS;
        } else if (bool2) {
            type = STRING_HOLIDAYS;
        }
        if (type.equals(STRING_HOLIDAYS)) {
            return STRING_CAN_NOT_CHECK_AT_HOLIDAYS;
        } else {

            DateTime now = DateUtil.date();
            String start = DateUtil.today() + " " + systemConstants.attendanceStartTime;
            String end = DateUtil.today() + " " + systemConstants.attendanceEndTime;
            Date attendanceStart = DateUtil.parse(start);
            Date attendanceEnd = DateUtil.parse(end);

            if (now.before(attendanceStart)) {
                return STRING_EARLY_CHECKIN;
            } else if (now.before(attendanceEnd)) {
                return STRING_LATELY_CHECKIN;
            } else {
                HashMap map = new HashMap();
                map.put("userId", userId);
                map.put("date", date);
                map.put("start", start);
                map.put("end", end);
                boolean hasCheckin = tbCheckinDao.haveCheckin(map) != null ? true : false;

                return hasCheckin ? STRING_HAS_CHECKIN : STRING_CAN_CHECKIN;


            }

        }
    }

    @Override
    public void checkin(CheckinForm form, int userId, String path) {
        Date d1 = DateUtil.date();
        //上班时间 6点
        Date d2 = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceTime);
        //上班考勤结束时间 9点半
        Date d3 = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime);

        int status = 1;
        if (d1.compareTo(d2) < 0) {
            //小于6点正常签到
            status = 1;
        } else if (d1.compareTo(d2) > 0 && d1.compareTo(d3) < 0) {
            //迟到
            status = 2;
        }
        boolean isFaceExist = faceLibraryService.checkUserFaceExistById(userId);
        if (!isFaceExist) {
            throw new EmosException("不存在人脸模型");
        } else {
            // 腾讯云人脸识别
            boolean isVerify = faceVerifyService.verify(userId,Imagebase64.byteConverterBASE64(FileUtil.file(path)));
            if(isVerify)
            {
                // 获取签到地区新冠疫情风险等级
                int risk = RISK_LEVEL_LOW;

                if (!StrUtil.isBlank(form.getCity()) && !StrUtil.isBlank(form.getDistrict())) {

                    String cityCode = tbCityDao.searchCode(form.getCity());

                    try {
                        String url = "http://m." + cityCode + ".bendibao.com/news/yqdengji/?qu=" + form.getDistrict();
                        Document document = Jsoup.connect(url).get();
                        Elements elements = document.getElementsByClass("list-detail");
                        for (Element element : elements) {
                            String result = element.text().split(" ")[1];
                            if (STRING_HIGH_RISK.equals(result)) {
                                risk = RISK_LEVEL_HIGH;
                                //发送邮件警告
                                HashMap<String, String> map = tbUserDao.searchNameAndDept(userId);
                                //员工名字
                                String name = map.get("name");
                                //部门名字
                                String deptName = map.get("dept_name");
                                deptName = deptName != null ? deptName : "";

                                SimpleMailMessage message = new SimpleMailMessage();
                                message.setTo(hrEmail);
                                message.setSubject("员工" + name + "身处高风险疫情地区警告");
                                message.setText(deptName + "员工" + name + "," + DateUtil.format(new Date(), "yyyy年MM月dd日") + "处于" + form.getAddress() + "，属于疫情高风险地区，请及时与该员工联系，核实情况！");
                                emailTask.sendAsync(message);

                                break;
                            } else if (STRING_MIDDLE_RISK.equals(result)) {
                                risk = risk < RISK_LEVEL_MIDDLE ? RISK_LEVEL_MIDDLE : risk;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        log.error("执行异常");
                        throw new EmosException("获取风险等级失败");
                    }

                    // 保存签到记录
                    TbCheckin checkin = new TbCheckin();
                    checkin.setUserId(userId);
                    checkin.setAddress(form.getAddress());
                    checkin.setCountry(form.getCountry());
                    checkin.setProvince(form.getProvince());
                    checkin.setCity(form.getCity());
                    checkin.setDistrict(form.getDistrict());
                    checkin.setRisk(risk);
                    checkin.setStatus((byte) status);
                    checkin.setDate(new Date());
                    checkin.setCreateTime(d1);
                    tbCheckinDao.insert(checkin);
                }
            }
        }
    }

    @Override
    public void createFaceModel(int userId, String path) {



        TbUser user =  tbUserDao.searchById(userId);
        String image = Imagebase64.byteConverterBASE64(FileUtil.file(path));
        faceLibraryService.createFaceModel(userId,user.getNickname(),image);

//        HttpRequest request = HttpUtil.createPost(createFaceModelUrl);
//        request.form("photo", FileUtil.file(path));
//        HttpResponse response = request.execute();
//        String body = response.body();
//        if ("无法识别出人脸".equals(body) || "照片中存在多张人脸".equals(body)) {
//            throw new EmosException("body");
//        } else {
//
//            TbFaceModel tbFaceModel = new TbFaceModel();
//            tbFaceModel.setUserId(userId);
//            tbFaceModel.setFaceModel(body);
//            tbFaceModelDao.insert(tbFaceModel);
//        }

    }

    @Override
    public HashMap searchTodayCheckin(int userId) {
        return tbCheckinDao.searchTodayCheckin(userId);
    }

    @Override
    public long searchCheckingDays(int userId) {
        return tbCheckinDao.searchCheckinDays(userId);
    }

    @Override
    public ArrayList<HashMap> searchWeekCheckin(HashMap param) {

        ArrayList<HashMap> checkinList = tbCheckinDao.searchWeekCheckin(param);
        ArrayList<String> holidaysList = tbHolidaysDao.searchHolidaysInRange(param);
        ArrayList<String> workdayList = tbWorkdayDao.searchWorkDayInRange(param);

        DateTime startDate = DateUtil.parseDate(param.get("startDate").toString());
        DateTime endDate = DateUtil.parseDate(param.get("endDate").toString());
        DateRange range = DateUtil.range(startDate, endDate, DateField.DAY_OF_MONTH);

        ArrayList list = new ArrayList();
        range.forEach(day -> {


            String date = day.toString("yyyy-MM-dd");
            String type = "工作日";
            if (day.isWeekend()) {
                type = "节假日";
            }
            if (holidaysList != null && holidaysList.contains(date)) {

                type = "节假日";
            } else if (workdayList != null && workdayList.contains(date)) {
                type = "工作日";
            }

            String status = "";
            if (type.equals("工作日") && DateUtil.compare(day, DateUtil.date()) <= 0) {
                status = "缺勤";

                // TODO: 2021/2/4  缺勤逻辑问题
                boolean flag = false;
                for (HashMap<String, String> map : checkinList) {
                    if(map.get("date").equals(date)) {
                        status = map.get("status");
                        flag = true;
                        break;
                    }
                }
                DateTime endTime = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime);
                String today = DateUtil.today();
                if (date.equals(today) && DateUtil.date().isBefore(endTime) && flag == false) {
                    status = "";
                }
            }

            HashMap map = new HashMap();
            map.put("date", date);
            map.put("status", status);
            map.put("type", type);
            map.put("day", day.dayOfWeekEnum().toChinese("周"));
            list.add(map);

        });
        return list;
    }

    @Override
    public ArrayList<HashMap> searchMonthChecking(HashMap param) {
        return this.searchWeekCheckin(param);
    }

}
