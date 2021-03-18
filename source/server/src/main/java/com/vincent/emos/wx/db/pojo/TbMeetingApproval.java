package com.vincent.emos.wx.db.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * tb_meeting_approval
 * @author 
 */
@Data
public class TbMeetingApproval implements Serializable {
    private Long id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 最后审批人
     */
    private Long last_user;

    /**
     * 需要审批的人元列表
     */
    private Object members;

    /**
     * 已经审批的人员列表
     */
    private Object approvals;

    private static final long serialVersionUID = 1L;
}