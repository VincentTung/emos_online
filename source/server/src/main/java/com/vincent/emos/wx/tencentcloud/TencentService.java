package com.vincent.emos.wx.tencentcloud;

import org.springframework.beans.factory.annotation.Value;

/**
 *
 * 腾讯云服务配置
 *
 */
public class TencentService {

    @Value("${tencentcloud.secrect-id}")
    public String SECRET_ID = "";

    @Value("${tencentcloud.secrect-key}")
    public String SECRET_KEY = "";

    @Value("${tencentcloud.facelib-id}")
    public String FACE_LIBRARY_ID = "";

    public String DEFAULT_REGION = "ap-beijing";

    @Value("${tencentcloud.facelib-name}")
    public String FACE_LIBRARY_NAME = "";

    public String END_POINT = "iai.tencentcloudapi.com";
}
