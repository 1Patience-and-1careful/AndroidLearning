package com.hanlyjiang.learnandroid.document;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.hanlyjiang.learnandroid.utils.MD5;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
 * @author hanlyjiang on 2017/8/24-22:10.
 * @version 1.0
 */

public class CaseFileListFragment extends Fragment {
    public static final int REQUEST_CODE = 0x09;
    @BindView(R.id.tv_logs)
    TextView operateLogsTv;


    @BindView(R.id.listview_doclist)
    ListView docListView;
    private Unbinder unbinder;

    private CaseFileAdapter caseFileAdapter;

    private List<CaseFile> caseFileList = new ArrayList<>();

    private CaseFile mCurrentEditFile;

    private OnFileStatusUpdate onFileStatusUpdate;

    private FileDataSource mFileDataSource;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(! (activity instanceof OnFileStatusUpdate)){
            throw new RuntimeException("请实现OnFileStatusUpdate");
        }
        mFileDataSource = ((OnFileStatusUpdate)activity).getFileDataSource();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_case_file_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        caseFileList = mFileDataSource.queryAll();
    }

    private void initView() {
        caseFileAdapter = new CaseFileAdapter();
        docListView.setAdapter(caseFileAdapter);
    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public interface OnFileStatusUpdate {

        void onFileStateUpdate(CaseFile caseFile);

        FileDataSource getFileDataSource();

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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
