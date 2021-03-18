package com.vincent.emos.wx.db.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ContactList {

    private int dept_id;
    private String dept_name;
    private  List<TbUser> users;

    public int getDept_id() {
        return dept_id;
    }

    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public List<TbUser> getUsers() {
        return users;
    }

    public void setUsers(List<TbUser> users) {
        this.users = users;
    }
}
