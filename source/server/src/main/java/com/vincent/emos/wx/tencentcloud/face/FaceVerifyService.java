package com.vincent.emos.wx.tencentcloud.face;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.*;
import com.vincent.emos.wx.exception.EmosException;
import com.vincent.emos.wx.tencentcloud.TencentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 *
 * 腾讯云 人脸验证类
 *
 */

@Service
@Slf4j
@Scope("prototype")
public class FaceVerifyService extends TencentService {

    public boolean verify(int userId, String image) {
        try {
            Credential cred = new Credential(SECRET_ID,SECRET_KEY);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(END_POINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            IaiClient client = new IaiClient(cred, DEFAULT_REGION, clientProfile);
            VerifyPersonRequest req = new VerifyPersonRequest();
            req.setPersonId(String.valueOf(userId));
            req.setImage(image);

            VerifyPersonResponse resp = client.VerifyPerson(req);
            System.out.println(VerifyPersonResponse.toJsonString(resp));
            return resp.getIsMatch();
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            throw new EmosException(e.getMessage());
        }
    }
}
