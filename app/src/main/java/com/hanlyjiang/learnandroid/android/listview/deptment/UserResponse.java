package com.hanlyjiang.learnandroid.android.listview.deptment;

/**
 * author:wangenzhao
 * date:2017/5/15 10:07
 */

public class UserResponse {

    private String Status;
    private String Message;
    private UserList data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public UserList getData() {
        return data;
    }

    public void setData(UserList data) {
        this.data = data;
    }
}
