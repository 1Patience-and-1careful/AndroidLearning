package com.hanlyjiang.learnandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 文件查看
 *<br/>Created by jianghanghang on 2017/3/13.
 */
public class FileViewer {

    /**
     * 查看文件
     * @param filePath
     */
    public static void viewFile(Context activity, String filePath){
        File viewFile = new File(filePath);
        Intent intent = new Intent();
        intent.setDataAndType(Uri.fromFile(viewFile),FileUtils.getMimeType(viewFile));
        activity.startActivity(intent);
    }


    public static Intent getExcelFileIntent( String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

}
