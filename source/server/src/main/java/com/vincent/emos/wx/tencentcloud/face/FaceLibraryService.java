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
import org.springframework.stereotype.Service;;

/**
 * 腾讯云 人员库管理类
 */
@Service
@Slf4j
@Scope("prototype")
public class FaceLibraryService extends TencentService {

    /**
     * 创建人员库
     */
    public void createFaceLibrary() {
        try {
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(END_POINT);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred, DEFAULT_REGION, clientProfile);

            CreateGroupRequest req = new CreateGroupRequest();
            req.setGroupName(FACE_LIBRARY_NAME);
            req.setGroupId(FACE_LIBRARY_ID);

            CreateGroupResponse resp = client.CreateGroup(req);

            System.out.println(CreateGroupResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }


    /**
     * 查询 人员库是否有userId的脸
     *
     * @param userId
     * @return
     */
    public boolean checkUserFaceExistById(int userId) {

        try {
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(END_POINT);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred, DEFAULT_REGION, clientProfile);

            GetPersonGroupInfoRequest req = new GetPersonGroupInfoRequest();
            req.setPersonId(String.valueOf(userId));

            GetPersonGroupInfoResponse resp = client.GetPersonGroupInfo(req);
            System.out.println(GetPersonGroupInfoResponse.toJsonString(resp));
            if (resp.getPersonGroupInfos() != null) {
                //成功
                return true;
            } else {
                //不存在 新建
                return false;
            }

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * 插入人脸
     *
     * @param userId
     * @param userName
     * @param image
     */
    public String createFaceModel(int userId, String userName, String image) {
        try {

            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(END_POINT);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred, DEFAULT_REGION, clientProfile);
            CreatePersonRequest req = new CreatePersonRequest();
            req.setGroupId(FACE_LIBRARY_ID);
            req.setPersonName(userName);
            req.setPersonId(String.valueOf(userId));
            req.setImage(image);

            CreatePersonResponse resp = client.CreatePerson(req);
            System.out.println(CreatePersonResponse.toJsonString(resp));
            return resp.getFaceId();


        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            throw new EmosException(e.getMessage());
        }
    }
}
