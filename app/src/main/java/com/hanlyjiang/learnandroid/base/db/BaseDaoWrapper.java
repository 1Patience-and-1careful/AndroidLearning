package com.hanlyjiang.learnandroid.base.db;

/**
 * @author hanlyjiang on 2017/8/24-15:49.
 * @version 1.0
 */

import de.greenrobot.dao.AbstractDao;

/**
 * 具体AbstractDao 的包装器，保证修改表结构时不用修改或者方便的修改查询代码
 * <br/> 使用GreenDao的一般流程：
 * <li>1. 使用<a href="https://code.geostar.com.cn/svn/AndroidSDK/project baseline/trunk/relyon_projects/tools/GreenDaoGenerator">GreenDaoGenerator</a>
 * 定义并生成实体类和DaoMaster与DaoSession类</li>
 * <li>2. 继承BaseDaoWrapper 并实现需要的数据库操作方法，实际使用时使用Wrapper即可</li>
 * <br/>
 * <br/>
 * Created by jianghanghang on 2016/11/2.
 */

public abstract class BaseDaoWrapper<T extends AbstractDao> {

    private T mDao;

    public T getRawDao() {
        return mDao;
    }

    public BaseDaoWrapper(T dao) {
        this.mDao = dao;
    }
}