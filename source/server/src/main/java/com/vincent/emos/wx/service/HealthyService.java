package com.vincent.emos.wx.service;

import com.vincent.emos.wx.controller.form.CheckinForm;


public interface HealthyService {
    int getRiskAtLocation( CheckinForm form);

    int searchHighRiskCheckin(int userId);
    int searchMiddleRiskCheckin(int userId);

    int searchRiskCheckinCount(int userId,int risk);
}
