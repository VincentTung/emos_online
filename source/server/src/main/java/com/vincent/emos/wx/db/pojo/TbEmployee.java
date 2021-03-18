package com.vincent.emos.wx.db.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tb_employee
 * @author 
 */
@Data
public class TbEmployee implements Serializable {
    /**
     * 注册码
     */
    private Integer code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 入职日期
     */
    private Date hiredate;

    /**
     * 角色
     */
    private Object role;

    /**
     * 部门id
     */
    private Integer deptId;

    private String deptName;

    /**
     * 激活 状态
     */
    private int state;
    /**
     * 性别
     */
    private Object sex;
    private static final long serialVersionUID = 1L;
}