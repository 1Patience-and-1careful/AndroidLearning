package com.hanlyjiang.learnandroid.document;

import com.hanlyjiang.learnandroid.base.db.BaseDaoWrapper;
import com.hanlyjiang.learnandroid.document.db.CaseFile;
import com.hanlyjiang.learnandroid.document.db.CaseFileDao;

import java.util.List;

/**
 * @author hanlyjiang on 2017/8/24-15:53.
 * @version 1.0
 */

public class FileDataSource extends BaseDaoWrapper<CaseFileDao> {

    public FileDataSource(CaseFileDao dao) {
        super(dao);
    }

    public List<CaseFile> queryAll() {
        return getRawDao().loadAll();
    }

    public void insert(CaseFile caseFile) {
        getRawDao().insert(caseFile);
    }

    public void update(CaseFile caseFile) {
        getRawDao().update(caseFile);
    }

}
