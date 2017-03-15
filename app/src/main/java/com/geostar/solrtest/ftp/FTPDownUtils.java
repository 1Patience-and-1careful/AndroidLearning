package com.geostar.solrtest.ftp;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FTP 下载工具
 * <br/>Created by jianghanghang on 2017/3/14.
 */

public class FTPDownUtils {

    public static final int ERR_FILE_NOT_FOUND_ON_SERVER = 0x01;
    /**
     * 登录失败
     */
    public static final int ERR_FILE_LOGIN_FAILED = 0x02;
    private static final int ERR_FTP_SERVER_REFUSED = 0x03;
    private static final int ERR_FTP_CANT_CONNECT_SERVER = 0x04;
    private static final int ERR_DOWNFILE_FAILED = 0x05;

    private static final String TAG = "FTPDownUtils";
    private FTPClient ftp;

    private volatile boolean stop;


    public interface FTPDownloadCallback{

        void onDownloadStart(long totalSize,long startSize);

        void onDownloading(long fileTotalSize, long downedSize);

        void onDownloadFinished(String filePath);

        void onDownloadError(int errorCode);

    }

    private void notifyStart(FTPDownloadCallback callback,long totalSize,long startSize){
        if(callback != null){
            callback.onDownloadStart(totalSize,startSize);
        }
    }

    private void notifyError(FTPDownloadCallback callback,int errorCode){
        if(callback != null){
            callback.onDownloadError(errorCode);
        }
    }

    private void notifyUpdating(FTPDownloadCallback callback,long totalSize,long downloadSize){
        if(callback != null){
            callback.onDownloading(totalSize,downloadSize);
        }
    }

    private void notifyFinish(FTPDownloadCallback callback,String filePath){
        if(callback != null){
            callback.onDownloadFinished(filePath);
        }
    }

    public void downloadFile(String filePath, String localFilePath, final FTPDownloadCallback callback) throws IOException {
        final Thread downTask = Thread.currentThread();
        connectFTP();
        loginToFTP();
        final FTPClient ftpClient = getFTPClicent();
        FTPFile[] files = null;
        try {
            files = ftpClient.listFiles(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(files == null || files.length == 0){
            unify_outPutResult("沒有文件:" + filePath);
            notifyError(callback,ERR_FILE_NOT_FOUND_ON_SERVER);
            return;
        }
        final long ftpFileSize = files[0].getSize();
        File localFile = new File(localFilePath);
        long startOffset = 0;
        long localFileSize = localFile.length();
//        long localFileSize = Utils.getFileSize(localFile.getAbsolutePath());
        Log.d(TAG,"本地文件大小：" + localFileSize + "; FTP文件大小：" + ftpFileSize);
        int dx = (int) Math.abs(localFileSize-ftpFileSize);
        if(localFile.isFile()){
            if( dx <= 2 ){
                notifyFinish(callback,localFilePath);
                return;
            }/*else if(localFileSize > ftpFileSize){
                localFile.delete();
            }*/else{
                boolean deleteResult = localFile.delete();
                if(deleteResult) {
                    localFile.createNewFile();
                }
                Log.d(TAG,"删除文件结果：" + deleteResult + "; localFilePath：" + localFilePath);
//                startOffset = localFileSize;
                startOffset = 0;
            }
        }
        ftpClient.setRestartOffset(startOffset);
        judgeStopDownTask(downTask, ftpClient);
        notifyStart(callback,ftpFileSize,startOffset);
        ftpClient.setCopyStreamListener(new CopyStreamListener() {
            long traned = 0;
            long pre;
            @Override
            public void bytesTransferred(CopyStreamEvent event) {
            }

            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) { //StreamSize 总是-1
                judgeStopDownTask(downTask, ftpClient);
                // 先看是否下載完畢
//                if(totalBytesTransferred >= streamSize){
//                    unify_outPutResult(String.format("下載完成：totalBytesTransferred:%d, bytesTransferred:%d , streamSize:%d",totalBytesTransferred,
//                            bytesTransferred,streamSize));
//                    notifyUpdating(callback,ftpFileSize,totalBytesTransferred);
//                    return;
//                }

                // 每下載1% 更新進度
                traned+=bytesTransferred;
                if(traned/(float)streamSize >= 0.01f){
                    unify_outPutResult(String.format("下載中：totalBytesTransferred:%d, bytesTransferred:%d , streamSize:%d",totalBytesTransferred,
                            bytesTransferred,streamSize));
                    traned = 0;
                    notifyUpdating(callback,ftpFileSize,totalBytesTransferred);
                }
            }
        });
        boolean downloadResult = ftpClient.retrieveFile(filePath,new FileOutputStream(localFile));
        if(stop){
            stop = false;
            return;
        }
        if(downloadResult){ // 如果下载成功，并且没有取消，则调用finish
            unify_outPutResult("下載成功");
            notifyUpdating(callback,ftpFileSize,ftpFileSize);
            notifyFinish(callback,localFilePath);
        }else{
            unify_outPutResult("下載失敗");
            notifyError(callback, ERR_DOWNFILE_FAILED);
        }
    }

    private void judgeStopDownTask(Thread downTask, FTPClient ftpClient) {
        if(downTask != null && downTask.isInterrupted()){
            stop = true;
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(stop){
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            setFTPClient(ftp);
            // 如果系统设置错误也可能导致错误，如此处如果设置成NT ,则无法获取文件大小
            FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            config.setDefaultDateFormatStr("yyyy-MM-dd");
            config.setServerLanguageCode("zh");
            ftp.configure(config);
        }
        return ftp;
    }

    private void setFTPClient(FTPClient ftp) {
        ftp.setConnectTimeout(2000);
        ftp.enterLocalPassiveMode();//
        // 保证数据传输期间连接不被关闭 http://commons.apache.org/proper/commons-net/javadocs/api-3.6/index.html
        ftp.setControlKeepAliveTimeout(300); //set timeout to 5 minutes
        ftp.setSendDataSocketBufferSize(1024);
        ftp.setControlEncoding("UTF-8"); // 编码一定要正确，不然无法定位到中文文件名
        try {
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(boolean stop) {
        this.stop = stop;
    }

}
