package com.geostar.solrtest.android.listview;

import com.geostar.solrtest.android.listview.deptment.Deptment;
import com.geostar.solrtest.android.listview.deptment.User;

/**
 * 统一数据结构Wrapper
 *
 * @author hanlyjiang on 2017/5/22-下午5:21.
 * @version 1.0
 */

public class DeptUnqData {

    public static final int TYPE_DEPT = 0;
    public static final int TYPE_USER = 1;

    private Object data;

    private int type;

    public DeptUnqData(Deptment data) {
        this.data = data;
        type = TYPE_DEPT;
    }

    public DeptUnqData(User data) {
        this.data = data;
        type = TYPE_USER;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String getName() {
        if (data instanceof Deptment) {
            return ((Deptment) data).getDeptname();
        } else if (data instanceof User) {
            return ((User) data).getUsername();
        }
        return null;
    }
}
