package com.hanlyjiang.learnandroid.android.listview.deptment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author:wangenzhao
 * date:2017/5/11 11:35
 */

public class Deptment implements Serializable{

    private int lv;
    private String deptid;
    private String parentid;
    private String deptname;
    private List<Deptment> children;
    private List<User> users;

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public List<Deptment> getChildren() {
        if (children == null) children = new ArrayList<>();
        return children;
    }

    public void setChildren(List<Deptment> children) {
        this.children = children;
    }

    public List<User> getUsers() {
        if (users == null) users = new ArrayList<>();
        return users;
    }

    public void setUser(User user) {
        if (users == null) users = new ArrayList<>();
        this.users.add(user);
    }
}
