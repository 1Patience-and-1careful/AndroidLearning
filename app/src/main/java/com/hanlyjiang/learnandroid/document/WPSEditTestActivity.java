package com.hanlyjiang.learnandroid.document;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanlyjiang.learnandroid.R;
import com.hanlyjiang.learnandroid.document.db.CaseFile;
import com.hanlyjiang.learnandroid.document.db.DaoMaster;
import com.hanlyjiang.learnandroid.utils.MD5;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * 测试 WPS 编辑文档后获取文档编辑结果
 */
public class WPSEditTestActivity extends AppCompatActivity {

    private static final String TAG = "WPSEditTestActivity";
    public static final int REQUEST_CODE = 0x09;
    @BindView(R.id.tv_logs)
    TextView operateLogsTv;


    @BindView(R.id.listview_doclist)
    ListView docListView;

    private FileDataSource mFileDataSource;

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private CaseFileAdapter caseFileAdapter;

    private List<CaseFile> caseFileList = new ArrayList<>();

    private CaseFile mCurrentEditFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wpsedit_test);
        ButterKnife.bind(this);

        initDataSource();
        initView();
    }


    private void initDataSource() {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, "file_db.db", null);
        DaoMaster daoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mFileDataSource = new FileDataSource(daoMaster.newSession().getCaseFileDao());
//        addAData();
        caseFileList = mFileDataSource.queryAll();
    }

    private void addAData() {
//        mFileDataSource.getRawDao().deleteAll();
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

        caseFile = new CaseFile();
        caseFile.setFileId(count++ + "");
        caseFile.setDownloadDate(new Date());
        caseFile.setUpdateDate(new Date());
        caseFile.setFileName("测试文档.doc");
        caseFile.setFileLocalPath("/sdcard/GuangZhouOA/测试文档.doc");
        caseFile.setFileUrl("/sdcard/GuangZhouOA/测试文档.doc");
        caseFile.setMd5(MD5.getFileMD5(new File("/sdcard/GuangZhouOA/测试文档.doc")));

        mFileDataSource.insert(caseFile);


        caseFile = new CaseFile();
        caseFile.setFileId(count++ + "");
        caseFile.setDownloadDate(new Date());
        caseFile.setUpdateDate(new Date());
        caseFile.setFileName("TestDoc-1.doc");
        caseFile.setFileLocalPath("/sdcard/GuangZhouOA/TestDoc-1.doc");
        caseFile.setFileUrl("/sdcard/GuangZhouOA/TestDoc-1.doc");
        caseFile.setMd5(MD5.getFileMD5(new File("/sdcard/GuangZhouOA/TestDoc-1.doc")));

        mFileDataSource.insert(caseFile);
    }

    private void initView() {
        caseFileAdapter = new CaseFileAdapter();
        docListView.setAdapter(caseFileAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDevOpenHelper != null) {
            mDevOpenHelper.close();
        }
    }

//    /**
//     * 调用WPS 打开文档并编辑
//     */
//    @OnClick(R.id.btn_open_doc)
//    void openDoc() {
//        openCaseDoc("/sdcard/GuangZhouOA/TestDoc.doc");
//    }

    private void openCaseDoc(String pathname) {
        showToast("打开文件编辑");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        Uri data = Uri.fromFile(new File(pathname));
        intent.setDataAndType(data, "application/ms-word");
        startActivityForResult(intent, REQUEST_CODE);
    }


    /**
     * 点击item 事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @OnItemClick(R.id.listview_doclist)
    void onDocListItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentEditFile = caseFileAdapter.getItem(position);
        openCaseDoc(mCurrentEditFile.getFileLocalPath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showToast("文件已编辑");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showToast("取消编辑");
                // TODO: 文件更新操作
                updateCurrentFile();
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                showToast("RESULT_FIRST_USER");
            }
        }

    }


    private void updateCurrentFile() {
        if (mCurrentEditFile == null) {
            return;
        }
        String newMd5 = MD5.getFileMD5(new File(mCurrentEditFile.getFileLocalPath()));
        String oldMd5 = mCurrentEditFile.getMd5();
        Date oldTime = mCurrentEditFile.getUpdateDate();
        if (!newMd5.equals(oldMd5)) {
            showToast("文件已更新");
            mCurrentEditFile.setMd5(newMd5);
            mCurrentEditFile.setUpdateDate(new Date());
            mFileDataSource.update(mCurrentEditFile);
            caseFileAdapter.notifyDataSetChanged();
            updateLogs(mCurrentEditFile, oldMd5, oldTime);
        } else {
            showToast("文件没有变化");
            updateLogsNoUpdate(mCurrentEditFile);
        }
    }

    private void updateLogsNoUpdate(CaseFile mCurrentEditFile) {
        operateLogsTv.append("\n--> 文件名： " + mCurrentEditFile.getFileName() + " 文件未更新");
    }

    private void updateLogs(CaseFile mCurrentEditFile, String oldMd5, Date oldTime) {
        String mesg = String.format("\n -->文件名：%s \n\t 最后更新时间： %s -> %s \n\t md5: %s -> %s \n\n", mCurrentEditFile.getFileName()
                , getTimeStr(oldTime), getTimeStr(mCurrentEditFile.getUpdateDate()),
                oldMd5, mCurrentEditFile.getMd5());
        operateLogsTv.append(mesg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private class CaseFileAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return caseFileList.size();
        }

        @Override
        public CaseFile getItem(int position) {
            return caseFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CaseFile caseFile = getItem(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_casefile, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(caseFile.getFileName());
            viewHolder.md5.setText(caseFile.getMd5());
            viewHolder.time.setText(getTimeStr(caseFile.getUpdateDate()));
            return convertView;
        }
    }

    private String getTimeStr(Date updateDate) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateDate);
    }

    class ViewHolder {
        View itemView;
        TextView name, md5, time;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            name = (TextView) itemView.findViewById(R.id.tv_fileName);
            md5 = (TextView) itemView.findViewById(R.id.tv_md5);
            time = (TextView) itemView.findViewById(R.id.tv_updateDate);
        }
    }
}
