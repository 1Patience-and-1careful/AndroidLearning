package com.hanlyjiang.greendaogen.document;

import com.hanlyjiang.greendaogen.base.IGenerator;
import com.hanlyjiang.greendaogen.utils.DaoUtils;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * @author hanlyjiang on 2017/8/24-15:25.
 * @version 1.0
 */

public class DocumentGen implements IGenerator {

    @Override
    public String _packageName() {
        return "com.hanlyjiang.learnandroid.document.db";
    }

    @Override
    public int _version() {
        return 1000;
    }

    @Override
    public String _genPath() {
        return "app/src/main/java/";
    }

    @Override
    public void addEntity(Schema schema) {
        add(schema);
    }

    private static void add(Schema schema) {
        Entity note = schema.addEntity("CaseFile");
        note.addIdProperty();
        note.addStringProperty("fileId").notNull();
        note.addStringProperty("fileName").notNull();
        note.addStringProperty("fileUrl").codeBeforeField(DaoUtils.createComment("文件下载路径"));
        note.addStringProperty("fileLocalPath").codeBeforeField(DaoUtils.createComment("文件本地路径"));
        note.addDateProperty("downloadDate").codeBeforeField(DaoUtils.createComment("下载时间"));
        note.addDateProperty("updateDate").codeBeforeField(DaoUtils.createComment("最后更新事件"));
        note.addStringProperty("md5").codeBeforeField(DaoUtils.createComment("最后更新md5"));
        note.addStringProperty("downloadState").codeBeforeField(DaoUtils.createComment("下载状态"));
    }

}
