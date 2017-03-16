package com.geostar.solrtest.ftp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.geostar.solrtest.R;
import com.geostar.solrtest.utils.FileViewer;
import com.geostar.solrtest.utils.RunnableUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FTPDownloadActivity extends AppCompatActivity implements View.OnClickListener, FTPDownUtils.FTPDownloadCallback {


    private static final String TAG = "FTP";

    private FTPClient ftp;
    ProgressDialog mDownProgress;

    private Future mDownTask;
    private FTPDownUtils ftpDownUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpdownload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.btn_connect_ftp).setOnClickListener(this);
        findViewById(R.id.btn_login_ftp).setOnClickListener(this);
        findViewById(R.id.btn_download_file).setOnClickListener(this);

        mDownProgress = createProgresDialog();
        ftpDownUtils = new FTPDownUtils();
    }

    private ProgressDialog createProgresDialog() {
        ProgressDialog downProgress = new ProgressDialog(this);
        downProgress.setTitle("下载中");
        downProgress.setMessage("....");
        downProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downProgress.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelDownTask();
            }
        });
        downProgress.setCancelable(false);
        downProgress.setCanceledOnTouchOutside(false);
        return downProgress;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_connect_ftp){
            RunnableUtils.executeOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    connectFTP();
                }
            });
        }else  if(v.getId() == R.id.btn_login_ftp){
            RunnableUtils.executeOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    loginToFTP();
                }
            });
        }else  if(v.getId() == R.id.btn_download_file){
            RunnableUtils.executeOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    String fileName = "apache-solr-ref-guide-5.3.pdf";
                    final String filePath = "ROOT/" + fileName;
                    final String localFilePath = "/sdcard/GeoMap/ftp_" + fileName;
//                    String filePath = "ROOT/196766172363653868.jpg",localFilePath = "/sdcard/ftp_196766172363653868.jpg";
//                    cancelDownTask();
                    mDownTask = RunnableUtils.submit(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            ftpDownUtils.downloadFile(filePath,localFilePath,FTPDownloadActivity.this);
                            return null;
                        }
                    });
                }
            });
        }else{

        }
    }

    private void cancelDownTask() {
        if(mDownTask != null && !mDownTask.isDone()){
            boolean cancelResult = mDownTask.cancel(true);
            Log.d(TAG,"任务取消结果：" + cancelResult );
        }
    }

    private void downloadFile(String filePath, String localFilePath) throws IOException {
        FTPClient ftpClient = getFTPClicent();
        FTPFile[] files = null;
        try {
            files = ftpClient.listFiles(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(files == null || files.length == 0){
            unify_outPutResult("沒有文件:" + filePath);
            return;
        }
        File localFile = new File(localFilePath);
        long startOffset = 0;
        if(localFile.exists()){
            if( localFile.length() > files[0].getSize() ){
                localFile.delete();
            }else{
                startOffset = localFile.length();
            }
        }
        ftpClient.setRestartOffset(startOffset);
        ftpClient.setCopyStreamListener(new CopyStreamListener() {
            long traned = 0;
            @Override
            public void bytesTransferred(CopyStreamEvent event) {
            }

            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                // 先看是否下載完畢
                if(totalBytesTransferred == streamSize){
                    unify_outPutResult(String.format("下載中：totalBytesTransferred:%d, bytesTransferred:%d , streamSize:%d",totalBytesTransferred,
                            bytesTransferred,streamSize));
                }
                // 每下載1% 更新進度
                traned+=bytesTransferred;
                if(traned/(float)streamSize >= 0.01f){
                    unify_outPutResult(String.format("下載中：totalBytesTransferred:%d, bytesTransferred:%d , streamSize:%d",totalBytesTransferred,
                            bytesTransferred,streamSize));
                    traned = 0;
                }
            }
        });
        boolean downloadResult = ftpClient.retrieveFile(filePath,new FileOutputStream(localFile));
        if(downloadResult){
            unify_outPutResult("下載成功");
        }else{
            unify_outPutResult("下載失敗");
        }
    }

    private void loginToFTP() {
        if( !getFTPClicent().isConnected() ){
            unify_outPutResult("没有连接");
            return;
        }
        boolean loginResult = false;
        try {
            loginResult = getFTPClicent().login("file","file");
        } catch (IOException e) {
            e.printStackTrace();
            unify_outPutResult("登录异常");
        }
        if(loginResult){
            unify_outPutResult("登录成功");
        }else{
            unify_outPutResult("登录失败");
        }
    }

    private void connectFTP() {
        if( getFTPClicent().isConnected() ){
            unify_outPutResult("已经连接");
            return;
        }
        try {
            getFTPClicent().connect(FTP_SERVER);
        } catch (IOException e) {
            e.printStackTrace();
            unify_outPutResult("连接异常");
        }
        if(getFTPClicent().isConnected()) {
            unify_outPutResult("连接成功");
        }
    }

    private void unify_outPutResult(String s) {
        Log.d(TAG,"----> " + s);
    }


    public static final String FTP_SERVER = "192.168.100.118";
    private FTPClient getFTPClicent(){
        if(ftp == null){
            ftp = new FTPClient();
            setFTPClient();
            FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_NT);
            config.setDefaultDateFormatStr("yyyy-MM-dd");
            config.setServerLanguageCode("zh");
            ftp.configure(config);
        }
        return ftp;
    }

    private void setFTPClient() {
        ftp.setConnectTimeout(2000);
        ftp.enterLocalPassiveMode();//
        ftp.setControlEncoding("UTF-8"); // 编码一定要正确，不然无法定位到中文文件名
        try {
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * FTP 下载更新
     * @param totalSize
     * @param startSize
     */
    @Override
    public void onDownloadStart(long totalSize, long startSize) {
        unify_outPutResult("onDownloadStart " + startSize + " / " + totalSize );
        updateProgress((int) totalSize, (int) startSize);
    }

    private void updateProgress(int totalSize, int startSize) {
        if(!mDownProgress.isShowing()){
            mDownProgress.show();
        }
        mDownProgress.setMax(totalSize);
        mDownProgress.setProgress(startSize);
    }

    @Override
    public void onDownloading(long fileTotalSize, long downedSize) {
        updateProgress((int) fileTotalSize,(int) downedSize);
        unify_outPutResult("onDownloading " + downedSize + " / " + fileTotalSize );
    }

    @Override
    public void onDownloadFinished(String filePath) {
        unify_outPutResult("onDownloadFinished");
        FileViewer.viewFile(this,filePath);
        mDownProgress.dismiss();
    }

    @Override
    public void onDownloadError(int errorCode) {
        unify_outPutResult("onDownloadError:" + FTPDownUtils.parseErrorCode(errorCode));
        Toast.makeText(this,"下载错误:" + FTPDownUtils.parseErrorCode(errorCode),Toast.LENGTH_SHORT).show();
        mDownProgress.dismiss();
    }
}
