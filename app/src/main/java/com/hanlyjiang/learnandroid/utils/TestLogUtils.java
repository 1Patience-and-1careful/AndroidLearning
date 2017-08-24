package com.hanlyjiang.learnandroid.utils;

/**
 * Logger 输出 ，使用system.out.print / system.err.print
 * Created by jianghanghang on 2016/10/17.
 */

public class TestLogUtils {

    private static String TAG = "TestLogUtils";

    public static void init(String tag) {
        if (tag != null) {
            TAG = tag;
        }
    }

    /**
     * 附加线程信息
     * @param msg
     */
    public static void d(String msg) {
        d(msg,true);
    }

    /**
     * 附加线程信息和调用方法名称
     * @param msg
     * @param showCallMethodName
     */
    public static void d(String msg, boolean showCallMethodName) {
        String threadMsg = Thread.currentThread().getName();
        if(showCallMethodName) {
            System.out.println(String.format("tag@ %s --> thread@ %s --> method@ %s --> %s", TAG, threadMsg,
                    Thread.currentThread().getStackTrace()[2].getMethodName(), msg));
        }else{
            System.out.println(String.format("tag@ %s --> thread@ %s --> %s", TAG, threadMsg, msg));
        }
    }

}
