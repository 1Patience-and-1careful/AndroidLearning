package com.geostar.solrtest.solr.request;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.geostar.solrtest.R;
import com.geostar.solrtest.ftp.UIHandler;
import com.geostar.solrtest.utils.FileViewer;
import com.geostar.solrtest.utils.RunnableUtils;
import com.geostar.solrtest.utils.SimpleHTTPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TwoFuncTestActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String SERVICE_ADMIN_ADDR = "http://192.168.31.54/MainService.svc/GetRegionModelJsonByCoordinate";
    public static final int MSG_ADMIN_BACK = 0x0098;
    private static final String SERVICE_EXCEL_FILE_ADDR = "http://192.168.31.54/MainService.svc/GetStatExcelPathByTableInfo" ;
    private static final String SERVICE_EXCEL_FILE_BASE_URl = "http://192.168.31.54/" ;
    private static final int MSG_EXCEL_BACK = 0x00100;
    private TextView displayBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_func_test);
        displayBoard = (TextView) findViewById(R.id.tv_display_board);
        findViewById(R.id.btn_get_admin_info).setOnClickListener(this);
        findViewById(R.id.btn_get_excel_file_url).setOnClickListener(this);
    }


    private final Handler mHandler = new UIHandler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_ADMIN_BACK || msg.what == MSG_EXCEL_BACK){
                displayBoard.setText(msg.obj==null?"null":msg.obj.toString());
            }
            if(msg.what == MSG_EXCEL_BACK && msg.obj != null){
//                String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".xlsx");
                FileViewer.viewFile(TwoFuncTestActivity.this,msg.obj.toString());
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(msg.obj.toString())),mime);
//                TwoFuncTestActivity.this.startActivity(intent);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_admin_info:
                RunnableUtils.executeOnWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject result = null;
                        try {
                            result = HTTPReqHelper.requestAdminByCoord(SERVICE_ADMIN_ADDR,
                                    112.838f,24.32f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage(MSG_ADMIN_BACK);
                        msg.obj = result;
                        mHandler.removeMessages(MSG_ADMIN_BACK);
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            case R.id.btn_get_excel_file_url:
                RunnableUtils.executeOnWorkThread(new Runnable() {
                    public static final String TAG = "Down file Thread";

                    @Override
                    public void run() {
                        String result = null;
                        Message msg = mHandler.obtainMessage(MSG_EXCEL_BACK);
                        try {
                            result = HTTPReqHelper.requestExcelFileUri(SERVICE_EXCEL_FILE_ADDR,SERVICE_EXCEL_FILE_BASE_URl,
                                    "T_Statistics_Template","%E7%BB%9F%E8%AE%A1%E8%A1%A8",
                                    new String[]{
                                        "50","51","100"
                                    });
                        }  catch (Exception e) {
                            e.printStackTrace();
                            msg.obj = "无法连接到服务器";
                        }
                        String localFileName = "temp.xlsx";
                        String[] split = result.split("/");
                        if(split != null && split.length > 1){
                            localFileName = split[split.length-1];
                        }
                        String localTempFile = new File(TwoFuncTestActivity.this.getExternalCacheDir().getAbsolutePath(),localFileName).getAbsolutePath();
                        File lFile = new File(localTempFile);
                        if(lFile.isFile()){
                            if( lFile.delete() ){
                                Log.e(TAG,"文件：" + localTempFile + " 失败");
                            }
                        }
                        try {
                            SimpleHTTPUtils.downFile(result,localTempFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.obj = "文件下載失敗";
                        }
                        msg.obj = localTempFile;
                        mHandler.removeMessages(MSG_EXCEL_BACK);
                        mHandler.sendMessage(msg);
                    }
                });
                break;
        }
    }


    private void updateDPBoard(String info){
        displayBoard.setText(info);
    }
}
