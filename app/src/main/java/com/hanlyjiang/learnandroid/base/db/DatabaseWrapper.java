package com.hanlyjiang.learnandroid.base.db;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author hanlyjiang on 2017/8/24-15:52.
 * @version 1.0
 */

public class DatabaseWrapper {
    /**
     * 使用GreenDao时数据库使用示例
     * Created by jianghanghang on 2016/11/30.
     */
    private SQLiteOpenHelper mDevOpenHelper;
    private BaseDaoWrapper daoWrapper1;

    /**
     * 关闭数据库
     */
    public void destory() {
        if (mDevOpenHelper != null) {
            mDevOpenHelper.close();
        }
    }
}
