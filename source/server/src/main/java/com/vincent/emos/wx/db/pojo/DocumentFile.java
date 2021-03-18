package com.vincent.emos.wx.db.pojo;

import lombok.Data;

@Data
public class DocumentFile {

    String key;
    // 文件的etag
    String etag;
    // 文件的长度
    long fileSize;
    // 文件的存储类型
    String storageClasses ;
}
