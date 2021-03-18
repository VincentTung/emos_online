package com.vincent.emos.wx.tencentcloud.storage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.vincent.emos.wx.db.pojo.DocumentFile;
import com.vincent.emos.wx.tencentcloud.TencentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Scope("prototype")
public class StorageService extends TencentService {

    private static final String BUCK_REGION = "ap-beijing";
    private static final String BUCK_NAME = "emos-1304799125";

    private COSClient cosClient;

    public StorageService() {
    }


    private void initClient() {
        if (cosClient == null) {

            COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
            Region region = new Region(BUCK_REGION);
            ClientConfig clientConfig = new ClientConfig(region);
// 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);

        }
    }

    public List<DocumentFile> listFiles() {

        initClient();
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
// 设置bucket名称
        listObjectsRequest.setBucketName(BUCK_NAME);
// prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix("doc/");
// deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
// 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;

        List<DocumentFile> documentFiles = new ArrayList<>();
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return documentFiles;
            } catch (CosClientException e) {
                e.printStackTrace();
                return documentFiles;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();
            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                if (key.endsWith("/")) {
                    continue;
                }
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();


                DocumentFile documentFile = new DocumentFile();
                documentFile.setEtag(etag);
                documentFile.setKey(key);
                documentFile.setFileSize(fileSize);
                documentFile.setStorageClasses(storageClasses);
                documentFiles.add(documentFile);

            }
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);


        } while (objectListing.isTruncated());

        return documentFiles;

    }

    public void downloadFile(String key) {

        initClient();
        try {
// 方法1 获取下载输入流
            GetObjectRequest getObjectRequest = new GetObjectRequest(BUCK_NAME, key);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
// 下载对象的 CRC64
            String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();
// 关闭输入流
            cosObjectInput.close();
// 方法2 下载文件到本地
            String outputFilePath = "exampleobject";
            File downFile = new File(outputFilePath);
            getObjectRequest = new GetObjectRequest(BUCK_NAME, key);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
