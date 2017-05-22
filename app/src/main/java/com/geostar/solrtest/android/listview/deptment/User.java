package com.geostar.solrtest.android.listview.deptment;

import java.io.Serializable;

/**
 * author:wangenzhao
 * date:2017/5/11 14:21
 */

public class User implements Serializable {
    private static final long serialVersionUID = 46516656512L;

    private String telphone;
    private String deptid;
    private String userid;
    private String username;

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
