package com.geostar.solrtest.android.listview.deptment;

import java.util.ArrayList;
import java.util.List;

/**
 * author:wangenzhao
 * date:2017/5/11 14:25
 */

public class UserList {

    private String Status;
    private String Message;
    private boolean isOk;
    private List<User> rows;

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

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public List<User> getRows() {
        if (rows == null) rows = new ArrayList<>();
        return rows;
    }

    public void setRows(List<User> rows) {
        this.rows = rows;
    }
}
