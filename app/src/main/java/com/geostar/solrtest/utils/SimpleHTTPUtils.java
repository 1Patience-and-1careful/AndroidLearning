package com.geostar.solrtest.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一个简单的HTTP 请求工具，包含POST 和 GET 请求方法，最终将结果转化为String
 * <br/>Created by jianghanghang on 2017/3/20.
 */
public class SimpleHTTPUtils {

    private static final String TAG = "SimpleHTTPUtils";
    private static final int TIME_OUT_5000 = 2000;
    private static  boolean DEBUG = true;

    /**
     * 做Get 请求，并且获取返回json 字符串
     * @param url
     * @return 返回内容
     * @throws IOException
     */
    public static String doGet(URL url) throws IOException {
        if(DEBUG){
            log(url.toString(),"GET" ,"");
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(TIME_OUT_5000);
        urlConnection.setReadTimeout(TIME_OUT_5000);
        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
        InputStreamReader reader = new InputStreamReader(in);
        try {
            if(DEBUG){
                String info = readStream(reader);
                log(url.toString(),"Response" ,info);
                return info;
            }else{
                return readStream(reader);
            }
        } finally {
            reader.close();
            in.close();
            urlConnection.disconnect();
        }
    }


    /**
     * 流转string
     * @param reader  InputStreamReader
     * @return  内容 string
     * @throws IOException
     */
    private static String readStream(InputStreamReader reader) throws IOException {
        char[] buffer = new char[4096];
        StringBuffer result = new StringBuffer();
        int index = 0;
        int size = 0;
        while( (size = reader.read(buffer, index, 4096)) != -1 ){
            result.append(buffer, index, size);
        }
        return result.toString();
    }

    /**
     * 做post请求，结果转String
     * @param url
     * @param postData
     * @return
     * @throws IOException
     */
    public static String doPost(URL url, String postData) throws IOException{
        String dateStampe = new SimpleDateFormat("yyMMdd-HHmmss-SSSS").format(new Date(System.currentTimeMillis()));
        if(DEBUG){
            log(url.toString(),dateStampe + " POST" ,postData);
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setConnectTimeout(TIME_OUT_5000);
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(urlConnection.getOutputStream())
        );
        out.write(postData);
        out.flush();
        out.close();
        if( urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK ){
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader reader = new InputStreamReader(in);
            try {
                if(DEBUG){
                    String info = readStream(reader);
                    log(url.toString(),dateStampe + " Response" ,info);
                    return info;
                }else{
                    return readStream(reader);
                }
            } finally {
                reader.close();
                in.close();
                urlConnection.disconnect();
            }
        }else{
//			urlConnection.getErrorStream();
            throw new IOException("请求出错");
        }

    }


    /**
     * setDebug为true后，会输出http请求和返回的内容
     * @param debug
     */
    public static void setDebug(boolean debug){
        DEBUG = debug;
    }

    private static void log(String url, String action, String info){
        System.out.println(String.format("SimpleHTTPRequest: %s ; %s;  %s ",
                url,action,info));
    }


    public static boolean downFile(String url,String filePath) throws IOException {
        URL fileUrl = new URL(url); //MalformedURLException
        URLConnection connection = fileUrl.openConnection();
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(5000);
        File localFile = new File(filePath);
        if( localFile.isFile() ){
            if(!localFile.delete()){
                Log.e(TAG,"文件：" + filePath + "删除失败");
            }
        }
        BufferedInputStream bif = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            bif = new BufferedInputStream(connection.getInputStream());
            int bufferSize = calcBufferSize(bif.available());
            byte[] buffer = new byte[10];
            int readcount = -1;
            while ( ( readcount = bif.read(buffer,0,bufferSize) ) != -1){
                fileOutputStream.write(buffer,0,readcount);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(bif != null){
                bif.close();
            }
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }
        return true;
    }

    private static int calcBufferSize(long ftpFileSize) {
        if (ftpFileSize > (1 << 30)) {// > 1GB
            return 20 * (1 << 10);
        } else if (ftpFileSize > (1 << 20)) { // > 1M
            return 10 * (1 << 10);
        } else if (ftpFileSize > (1 << 10)) { // > 1KB
            return 512;
        } else if (ftpFileSize > (512)) {
            return 2 << 5;
        }
        return 2;
    }

}
