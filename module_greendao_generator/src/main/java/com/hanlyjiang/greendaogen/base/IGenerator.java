package com.hanlyjiang.greendaogen.base;

import de.greenrobot.daogenerator.Schema;

/**
 * @author hanlyjiang on 2017/8/24-15:21.
 * @version 1.0
 */

public interface IGenerator {

    /**
     * 定义包名
     *
     * @return
     */
    String _packageName();

    /**
     * 定义版本
     *
     * @return
     */
    int _version();

    /**
     * 生成的文件存放路径
     *
     * @return
     */
    String _genPath();

    /**
     * 添加实体
     *
     * @param schema
     */
    void addEntity(Schema schema);


}
