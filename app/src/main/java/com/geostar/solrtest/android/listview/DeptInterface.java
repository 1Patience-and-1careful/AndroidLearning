package com.geostar.solrtest.android.listview;

import com.geostar.solrtest.android.listview.deptment.Deptment;
import com.geostar.solrtest.android.listview.deptment.User;

/**
 * @author hanlyjiang on 2017/5/22-下午6:24.
 * @version 1.0
 */

public class DeptInterface {


    interface OnUserClickListener {
        void onUserClick(User user);
    }

    interface OnDeptClickListener {
        void onDeptClick(Deptment dept);
    }
}
