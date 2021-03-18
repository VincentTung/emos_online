package com.vincent.emos.wx.service;

import com.vincent.emos.wx.db.pojo.MessageEntity;
import com.vincent.emos.wx.db.pojo.MessageRefEntity;

import java.util.HashMap;
import java.util.List;

/**
 *
 * 消息业务类
 *
 */
public interface MessageService {
     /**
      * 插入消息实体
      * @param entity
      * @return
      */
     String insertMessage(MessageEntity entity);

     /**
      * 插入消息状态
      * @param entity
      * @return
      */
     String insertRef(MessageRefEntity entity);

     /**
      * 查询未读消息个数
      * @param userId
      * @return
      */
     long searchUnreadCount(int userId);

     // TODO: 2021/2/1  ???
     long searchLastCount(int userId);

     /**
      * 分页查询消息
      * @param userId
      * @param start
      * @param length
      * @return
      */
     List<HashMap> searchMessageByPage(int userId,long start,int length);

     /**
      * 查询消息内容
      * @param id
      * @return
      */
     HashMap searchMessageById(String id);

     /**
      * 更新未读的消息
      * @param id
      * @return
      */
     long updateUnreadMessage(String id);

     /**
      * 删除消息状态
      * @param id
      * @return
      */
     long deleteMessageRefById(String id);

}
