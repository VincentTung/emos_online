package com.vincent.emos.wx.common.util;

import cn.hutool.core.codec.Base64Encoder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class Imagebase64 {
    public static String byteConverterBASE64(File file){
        long size = file.length();
        byte[] imageByte = new byte[(int)size];
        FileInputStream fs = null;
        BufferedInputStream bis = null;
        try {
            fs = new FileInputStream(file);
            bis = new BufferedInputStream(fs);
            bis.read(imageByte);
        } catch (FileNotFoundException e) {
            log.error("文件"+file.getName()+"不能被找到："+e.getMessage());
        } catch (IOException e) {
            log.error("byte转换BASE64出错："+e.getMessage());
        } finally{
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("关闭输入流出错："+e.getMessage());
                }
            }
            if(fs != null){
                try {
                    fs.close();
                } catch (IOException e) {
                    log.error("关闭输入流出错："+e.getMessage());
                }
            }
        }
        return Base64Encoder.encode(imageByte);
    }
}