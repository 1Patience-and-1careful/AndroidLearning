package com.hanlyjiang.learnandroid.solr.request;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.hanlyjiang.learnandroid.utils.SimpleHTTPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 完成指定接口请求功能
 * <br/>Created by jianghanghang on 2017/3/20.
 */

public class HTTPReqHelper {

    public static final String TAG = "HTTPReqHelper";

    /**
     * 通过点查询行政区划信息
     * @return 行政区划
     */
    public static JSONObject requestAdminByCoord(String serviceUrl, float x, float y) throws Exception{
        // Test Address  --> http://192.168.31.54/MainService.svc/GetRegionModelJsonByCoordinate?x=112.838&y=24.32
        /*
         * 返回： JSON
         * <pre>
         * "{\"RegionCode\":\"441800000000\",\"RegionName\":\"清远市\"}"
         * </pre>
         */
        String result = null;
        try {
            result = SimpleHTTPUtils.doGet(new URL(String.format("%s?x=%f&y=%f",
                    serviceUrl,x,y)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result == null){
            return null;
        }
        JSONObject jsonObject = null;
        try {
            String cutString = result.trim().replace("\\",""); // "{\"RegionCode\":\"441800000000\",\"RegionName\":\"清远市\"}"
            jsonObject = new JSONObject( cutString.substring(1,cutString.length()-1) );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,"获取行政区失败");
        }
        return jsonObject;
    }


    /**
     * 请求Excel 文件下载路径
     * @param baseUrl 基础路径
     * @param objIDs 高亮ITEM 的ObjectID
     * @return
     */
    public static String requestExcelFileUri(String baseUrl, String fileBaseUrl, String tableName ,String tableAlias, String[] objIDs)
            throws Exception {
        //http://192.168.31.54/MainService.svc/GetStatExcelPathByTableInfo?tablename=T_Statistics_Template&Tablealias=%E7%BB%9F%E8%AE%A1%E8%A1%A8&objectIds=50|51|100
        /* 返回 ： "Temp\\T_Statistics_Template_1489992207.xlsx"
         *
         */
        Uri.Builder builder = new Uri.Builder();
        builder.encodedPath(baseUrl);
        builder.appendQueryParameter("tablename",tableName);
        builder.appendQueryParameter("Tablealias",tableAlias);
        StringBuffer buffer = new StringBuffer();
        for(String id : objIDs){
            buffer.append(id + "|");
        }
        buffer.deleteCharAt(buffer.length()-1);
        builder.appendQueryParameter("objectIds",buffer.toString());
        Uri uri = builder.build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(url == null){
            Log.e(TAG,"URL 构造失败");
            return null;
        }

        String result = null;
        try {
            result = SimpleHTTPUtils.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"无法连接到服务器");
            throw e;
        }
        if(!TextUtils.isEmpty(result)){
            String cutStr = result.trim().substring(1,result.length()-1);
            return fileBaseUrl + "/" + cutStr.replace("\\\\","/");
        }else{
            Log.e(TAG,"没有查询到");
        }
        return null;
    }

}
