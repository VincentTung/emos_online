package com.vincent.emos.wx;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 系统配置类
 *
 * 数据来db
 */
@Data
@Component
public class SystemConstants {
    //    上班考勤开始时间
    public String attendanceStartTime;
    //    上班时间
    public String attendanceTime;
    //    上班考勤结束时间
    public String attendanceEndTime;
    //    下班考勤开始时间
    public String closingStartTime;
    //    下班时间
    public String closingTime;
    //    下班考勤结束时间
    public String closingEndTime;
}
