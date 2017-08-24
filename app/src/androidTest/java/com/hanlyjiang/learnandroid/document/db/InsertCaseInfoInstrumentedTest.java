package com.hanlyjiang.learnandroid.document.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.hanlyjiang.learnandroid.document.FileDataSource;
import com.hanlyjiang.learnandroid.utils.MD5;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InsertCaseInfoInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hanlyjiang.learnandorid", appContext.getPackageName());
    }


    private FileDataSource mFileDataSource;

    private DaoMaster.DevOpenHelper mDevOpenHelper;

    @Test
    public void makeTestCaseFileData() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hanlyjiang.learnandorid", appContext.getPackageName());
        mDevOpenHelper = new DaoMaster.DevOpenHelper(appContext, "file_db.db", null);
        DaoMaster daoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mFileDataSource = new FileDataSource(daoMaster.newSession().getCaseFileDao());

        mFileDataSource.getRawDao().deleteAll();

        addAData();

        List<CaseFile> caseFileList = mFileDataSource.queryAll();
        Assert.assertNotNull(caseFileList);
        Assert.assertEquals(caseFileList.size(),1);
        System.out.println(caseFileList.get(0).getFileName());
    }

    private void addAData() {
        int count = 0;
        CaseFile caseFile = new CaseFile();
        caseFile.setFileId(count++ + "");
        caseFile.setDownloadDate(new Date());
        caseFile.setUpdateDate(new Date());
        caseFile.setFileName("TestDoc.doc");
        caseFile.setFileLocalPath("/sdcard/GuangZhouOA/TestDoc.doc");
        caseFile.setFileUrl("/sdcard/GuangZhouOA/TestDoc.doc");
        caseFile.setMd5(MD5.getFileMD5(new File("/sdcard/GuangZhouOA/TestDoc.doc")));

        mFileDataSource.insert(caseFile);
    }
}
