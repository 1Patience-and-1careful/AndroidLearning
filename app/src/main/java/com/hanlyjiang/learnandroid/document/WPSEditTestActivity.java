package com.hanlyjiang.learnandroid.document;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hanlyjiang.learnandroid.R;
import com.hanlyjiang.learnandroid.document.db.CaseFile;
import com.hanlyjiang.learnandroid.document.db.DaoMaster;
import com.hanlyjiang.learnandroid.utils.FileUtil;
import com.hanlyjiang.learnandroid.utils.MD5;

import java.io.File;
import java.io.IOException;
import java.util.Date;


/**
 * 测试 WPS 编辑文档后获取文档编辑结果
 */
public class WPSEditTestActivity extends AppCompatActivity implements CaseFileListFragment.OnFileStatusUpdate {

    private static final String TAG = "WPSEditTestActivity";


    private FileDataSource mFileDataSource;

    private DaoMaster.DevOpenHelper mDevOpenHelper;

    private String pathDoc_01 = "插件使用文档01.doc";
    private String pathDoc_02 = "插件使用文档02.doc";
    private String pathDoc_03 = "插件使用文档03.doc";
    private String basedir = "/sdcard/doc/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wpsedit_test);
        initDataSource();
        cloneFile();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_stub,
                new CaseFileListFragment()).commit();
    }

    private void cloneFile() {
        try {
            FileUtil.copyFile(getAssets().open("插件使用文档.doc"), basedir + pathDoc_01);
            FileUtil.copyFile(getAssets().open("插件使用文档.doc"), basedir + pathDoc_02);
            FileUtil.copyFile(getAssets().open("插件使用文档.doc"), basedir + pathDoc_03);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDataSource() {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, "file_db.db", null);
        DaoMaster daoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mFileDataSource = new FileDataSource(daoMaster.newSession().getCaseFileDao());
//        addAData();
    }

    private void addAData() {
        mFileDataSource.getRawDao().deleteAll();
        int count = 0;
        CaseFile caseFile = insertFile(++count, pathDoc_01);
        mFileDataSource.insert(caseFile);

        caseFile = insertFile(++count, pathDoc_02);
        mFileDataSource.insert(caseFile);

        caseFile = insertFile(++count, pathDoc_03);
        mFileDataSource.insert(caseFile);

    }

    private CaseFile insertFile(int count, String fileName) {
        CaseFile caseFile = new CaseFile();
        caseFile.setFileId(count + "");
        caseFile.setDownloadDate(new Date());
        caseFile.setUpdateDate(new Date());
        caseFile.setFileName(fileName);
        String filePath = basedir + fileName;
        caseFile.setFileLocalPath(filePath);
        caseFile.setFileUrl(filePath);
        caseFile.setMd5(MD5.getFileMD5(new File(filePath)));
        return caseFile;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDevOpenHelper != null) {
            mDevOpenHelper.close();
        }
    }

    @Override
    public void onFileStateUpdate(CaseFile caseFile) {

    }

    @Override
    public FileDataSource getFileDataSource() {
        return mFileDataSource;
    }
}
