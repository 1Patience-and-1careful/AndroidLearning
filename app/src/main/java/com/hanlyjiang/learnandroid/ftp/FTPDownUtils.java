package com.hanlyjiang.learnandroid.ftp;

import android.util.Log;

import com.hanlyjiang.learnandroid.utils.RunnableUtils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

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
    private FileOutputStream fileOutputStream;


    public interface FTPDownloadCallback{

        void onDownloadStart(long totalSize,long startSize);

        void onDownloading(long fileTotalSize, long downedSize);

        void onDownloadFinished(String filePath);

        void onDownloadError(int errorCode);

    }

    private void notifyStart(final FTPDownloadCallback callback,final long totalSize,final long startSize){
        if(callback != null){
            RunnableUtils.postUI(new Runnable() {
                @Override
                public void run() {
                    callback.onDownloadStart(totalSize,startSize);
                }
            });
        }
    }

    private void notifyError(final  FTPDownloadCallback callback,final int errorCode){
        if(callback != null){
            RunnableUtils.postUI(new Runnable() {
                @Override
                public void run() {
                    callback.onDownloadError(errorCode);
                }
            });
        }
    }

    private void notifyUpdating(final FTPDownloadCallback callback, final long totalSize, final long downloadSize){
        if(callback != null){
            RunnableUtils.postUI(new Runnable() {
                @Override
                public void run() {
                    callback.onDownloading(totalSize,downloadSize);
                }
            });
        }
    }

    private void notifyFinish(final FTPDownloadCallback callback, final String filePath){
        if(callback != null){
            RunnableUtils.postUI(new Runnable() {
                @Override
                public void run() {
                    callback.onDownloadFinished(filePath);
                }
            });
        }
    }

    /**
     * FTP 下载
     * @param filePath FTP 文件路径
     * @param localFilePath 本地存储路径
     * @param callback 回调
     * @throws IOException
     */
    public void downloadFile(String filePath, String localFilePath, final FTPDownloadCallback callback) throws IOException {
        getFTPClicent(true);
        try {
            doRealDown(filePath, localFilePath, callback);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally {
            try {
                getFTPClicent().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stop = false;
        }
    }

    private void doRealDown(String filePath, String localFilePath, final FTPDownloadCallback callback) throws IOException {
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
        RandomAccessFile randomAccessFile = new RandomAccessFile(localFile,"rws");
        if(localFile.isFile()){
            if( localFileSize == ftpFileSize ){ // 文件存在
                notifyFinish(callback,localFilePath);
                return;
            }else if(localFileSize > ftpFileSize){ // 文件比FTP上大
                localFile.delete();
                boolean deleteResult = localFile.delete();
                if(deleteResult) {
                    localFile.createNewFile();
                }
                Log.d(TAG,"删除文件结果：" + deleteResult + "; localFilePath：" + localFilePath);
            }else{ // 文件比服务器小
                startOffset = localFileSize;
                Log.d(TAG,"文件存在：开始下载大小" + localFileSize);
            }
        }
        randomAccessFile.seek(startOffset);
        ftpClient.setRestartOffset(startOffset);
        ftpClient.setBufferSize(calcBufferSize(ftpFileSize));
        // 解决文件大小不一致问题
        if( !ftpClient.setFileType(FTP.BINARY_FILE_TYPE)){
            unify_outPutResult("Set BINARY_FILE_TYPE Failed!!!");
        }
        judgeStopDownTask(downTask, ftpClient);
        notifyStart(callback,ftpFileSize,startOffset);
        final long startByteSize = startOffset;
        ftpClient.setCopyStreamListener(new CopyStreamListener() {
            @Override
            public void bytesTransferred(CopyStreamEvent event) {}

            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) { //StreamSize 总是-1
                try {
                    if( judgeStopDownTask( downTask, ftpClient) ){
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    unify_outPutResult("停止服务连接失败");
                }
                if(stop){
                    RunnableUtils.cancelUITask();
                }
                if(!stop) {
                    notifyUpdating(callback, ftpFileSize, totalBytesTransferred+startByteSize);
                }
            }
        });
        fileOutputStream = new FileOutputStream(localFile,startOffset  > 0); // 使用append 方式写入
        boolean downloadResult = ftpClient.retrieveFile(filePath, fileOutputStream);
        if(downloadResult){ // 如果下载成功，并且没有取消，则调用finish
            unify_outPutResult("下載成功");
            notifyUpdating(callback,ftpFileSize,ftpFileSize);
            notifyFinish(callback,localFilePath);
        }else{
            unify_outPutResult("下載失敗");
            notifyError(callback, ERR_DOWNFILE_FAILED);
        }
        getFTPClicent().logout();
    }

    private int calcBufferSize(long ftpFileSize) {
        if(ftpFileSize > (1<<30)){// > 1GB
            return 20 * (1<<10);
        }else if(ftpFileSize > (1<<20)){ // > 1M
            return 10 * (1<<10);
        }else if(ftpFileSize > (1<<10)){ // > 1KB
            return 512;
        }else if(ftpFileSize > (512)){
            return 2<<5;
        }
        return 2;
    }

    private boolean judgeStopDownTask(Thread downTask, FTPClient ftpClient) throws IOException {
        if(downTask != null && downTask.isInterrupted()){
            stop = true;
            tryToStopFileDown(ftpClient);
        }
        if(stop){
            tryToStopFileDown(ftpClient);
        }
        return stop;
    }

    private void tryToStopFileDown(FTPClient ftpClient) throws IOException {
        try {
            if(fileOutputStream != null ){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ftpClient.disconnect();
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
        return getFTPClicent(false);
    }

    private FTPClient getFTPClicent(boolean create){
        if(create){
            ftp = new FTPClient();
            setFTPClient(ftp);
            // 如果系统设置错误也可能导致错误，如此处如果设置成NT ,则无法获取文件大小
            FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            config.setDefaultDateFormatStr("yyyy-MM-dd");
            config.setServerLanguageCode("zh");
            ftp.configure(config);
            return ftp;
        }
        if(ftp == null){
            return getFTPClicent(true);
        }
        return ftp;
    }

    private void setFTPClient(FTPClient ftp) {
        ftp.setConnectTimeout(2000);
        ftp.enterLocalPassiveMode();//
        // 保证数据传输期间连接不被关闭 http://commons.apache.org/proper/commons-net/javadocs/api-3.6/index.html
        ftp.setControlKeepAliveTimeout(300); //set timeout to 5 minutes
        ftp.setBufferSize(4096);
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

    public static final String parseErrorCode(int code) {
        if (code == ERR_DOWNFILE_FAILED) {
            return "下载文件过程中失败";
        } else if (code == ERR_FILE_LOGIN_FAILED) {
            return "无法登录到FTP服务器";
        } else if (code == ERR_FILE_NOT_FOUND_ON_SERVER) {
            return "档案文件不存在";
        } else if (code == ERR_FTP_CANT_CONNECT_SERVER) {
            return "无法连接到FTP服务器";
        } else if (code == ERR_FTP_SERVER_REFUSED) {
            return "服务器拒绝连接";
        } else {
            return "";
        }
    }

}
