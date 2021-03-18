package com.vincent.emos.wx.db.dao;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.vincent.emos.wx.db.pojo.MessageEntity;
import com.vincent.emos.wx.db.pojo.MessageRefEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class MessageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public String insert(MessageEntity entity){
        Date sendTime = entity.getSendTime();
        sendTime = DateUtil.date().offset(DateField.HOUR,8);
        entity.setSendTime(sendTime);
        entity = mongoTemplate.save(entity);
        return entity.get_id();
    }


    /**
     * 分页查询消息列表
     * @param userId 用户id
     * @param start
     * @param length
     * @return
     */
    public List<HashMap> searchMessageByPage(int userId, long start, int length){

        JSONObject json = new JSONObject();
        json.set("$toString","$_id");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.addFields().addField("id").withValueOf(json).build(),
                Aggregation.lookup("message_ref","id","messageId","ref"),
                Aggregation.match(Criteria.where("ref.receiverId").is(userId)),
                Aggregation.sort(Sort.by(Sort.Direction.DESC,"sendTime")),
                Aggregation.skip(start),
                Aggregation.limit(length)

        );

        AggregationResults<HashMap> results = mongoTemplate.aggregate(aggregation,"message",HashMap.class);
        List<HashMap> list = results.getMappedResults();
        list.forEach(map->{
            List<MessageRefEntity> refList = (List<MessageRefEntity>)map.get("ref");
            MessageRefEntity entity  = refList.get(0);
            boolean readFlag = entity.getReadFlag();
            String refId = entity.get_id();
            map.remove("ref");
            map.put("readFlag",readFlag);
            map.put("refId",refId);
            map.remove("_id");

            Date sendTime = (Date) map.get("sendTime");
            sendTime = DateUtil.date(sendTime).offset(DateField.HOUR,-8);
            String today = DateUtil.today();
            if(today.equals(DateUtil.date(sendTime).toDateStr())){
                map.put("sendTime",DateUtil.format(sendTime,"HH:mm"));
            }else{
                map.put("sendTime",DateUtil.format(sendTime,"yyyy/MM/dd"));
            }
        });

        return list;

    }

    public HashMap searchMessageById(String id){
        HashMap map = mongoTemplate.findById(id,HashMap.class,"message");
        Date sendTime = (Date) map.get("sendTime");
        sendTime = DateUtil.date(sendTime).offset(DateField.HOUR,-8);
        map.replace("sendTime",DateUtil.format(sendTime,"yyyy-MM-dd HH:mm"));
        return map;
    }
}
