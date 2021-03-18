package com.vincent.emos.wx;

import cn.hutool.core.util.StrUtil;
import com.vincent.emos.wx.db.dao.SysConfigDao;
import com.vincent.emos.wx.db.pojo.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;


@SpringBootApplication
@ServletComponentScan
@Slf4j
@EnableAsync
public class EmosServerApplication {

    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private SystemConstants sysConstants;

    @Value("${emos.image-folder}")
    private String imageFolder;

    public static void main(String[] args) {
        SpringApplication.run(EmosServerApplication.class, args);
    }


    @PostConstruct
    public void init(){

        // 1.读取数据库中的配置到变量中
        List<SysConfig> list = sysConfigDao.selectAllParams();
        list.forEach(sysConfig -> {

            String key = sysConfig.getParamKey();
            key = StrUtil.toCamelCase(key);
            String value = sysConfig.getParamValue();
            Field field = null;
            try {
                field = sysConstants.getClass().getDeclaredField(key);
                field.set(sysConstants,value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                log.error("执行出错",e);
            }

        });
        log.info(sysConstants.attendanceEndTime);
        // 2. 创建缓存图片用的文件夹
        new File(imageFolder).mkdirs();

    }
}
