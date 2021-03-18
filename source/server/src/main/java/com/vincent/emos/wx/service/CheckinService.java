package com.vincent.emos.wx.service;

import com.vincent.emos.wx.controller.form.CheckinForm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 签到业务类
 */
public interface CheckinService {
    /**
     * 验证是否可以签到
     *
     * @param userId
     * @param date
     * @return
     */
    String validCanCheckin(int userId, String date);

    /**
     * 进行签到
     *
     * @param form
     * @param userId
     * @param path
     */
    void checkin(CheckinForm form, int userId, String path);

    /**
     * 创建脸部模型
     *
     * @param userId
     * @param path
     */
    void createFaceModel(int userId, String path);

    /**
     * 查询今天的签到状态
     *
     * @param userId
     * @return
     */
    HashMap searchTodayCheckin(int userId);

    /**
     * 查询总的签到天数
     *
     * @param userId
     * @return
     */
    long searchCheckingDays(int userId);

    // TODO: 2021/2/1 ??
    ArrayList<HashMap> searchWeekCheckin(HashMap param);

    // TODO: 2021/2/1 ??
    ArrayList<HashMap> searchMonthChecking(HashMap param);

}
