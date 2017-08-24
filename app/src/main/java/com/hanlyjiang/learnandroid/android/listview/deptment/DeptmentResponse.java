package com.hanlyjiang.learnandroid.android.listview.deptment;

/**
 * author:wangenzhao
 * date:2017/5/15 9:59
 */

public class DeptmentResponse {
    private String Status;
    private String Message;
    private DeptmentList data;

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

    public DeptmentList getData() {
        return data;
    }

    public void setData(DeptmentList data) {
        this.data = data;
    }
}
