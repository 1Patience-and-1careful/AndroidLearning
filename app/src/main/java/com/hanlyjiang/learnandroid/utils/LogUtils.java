package com.hanlyjiang.learnandroid.utils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 通用的Android Log 工具接口
 * <br/> 目前使用 <a href="https://github.com/orhanobut/logger">Logger工具包，可以使用Logger类进行初始化</a>
 * <br/>Created by jianghanghang on 2016/7/22.
 */
public class LogUtils {

    private static long startTime = 0;

    public static void d(String msg){
        Logger.d( msg );
    }

    public static void d(Object obj){
        Logger.d(obj);
    }

    public static void d(String tag,String msg){
        Logger.t(tag).d(msg);
    }

    public static void e(String msg){
        Logger.e(msg);
    }

    public static void e(String tag,String msg){
        Logger.t(tag).e(msg);
    }

    public static void w(String msg){
        Logger.w(msg);
    }

    public static void json(String jsonStr){
        Logger.json(jsonStr);
    }

    public static void xml(String xmlStr){
        Logger.xml(xmlStr);
    }


    public static void timeInit(){
        startTime = System.nanoTime();
    }

    public static void timeCost(String tag){
        long endTime = System.nanoTime() - startTime;
        d(tag + " :" + " 纳秒=" + endTime + " , 微秒=" + TimeUnit.NANOSECONDS.toMicros(endTime)  + " , 毫秒= " +
                TimeUnit.NANOSECONDS.toMillis(endTime) + " , 秒=" + TimeUnit.NANOSECONDS.toSeconds(endTime));
        startTime = System.nanoTime();
    }


    /**
     * 输出消息到文件，并且附加当前时间与方法线程方法调用堆栈信息。需要文件读写权限
     * <br/> 建议保存为txt文件，便于移动端查看
     * @param file
     * @param msg
     * @param appendStackInfo 是否附加调用信息
     */
    public static void file(String file, String msg,boolean appendStackInfo) {
        if(!new File(file).isFile()){
            try {
                new File(file).getParentFile().mkdirs();
                new File(file).createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("日志文件创建失败");
            }
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            if(appendStackInfo) {
                writer.append(String.format("%s: %s \n %s \n",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())),
                        getStackStraceInfo(Thread.currentThread().getStackTrace()), msg)
                );
            }else{
                writer.append(String.format("%s: %s \n",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))
                        , msg));
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("写入日志文件失败");
        }
        finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输出异常信息到文件 需要文件读写权限
     * <br/> 建议保存为txt文件，便于移动端查看
     */
    public static void file(String filePath , Exception exception,String msg) {
        if(!new File(filePath).isFile()){
            try {
                new File(filePath).getParentFile().mkdirs();
                new File(filePath).createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("日志文件创建失败");
            }
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.append(String.format("%s: %s \n %s \n %s",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())),
                    getStackStraceInfo(exception.getStackTrace())
                    ,exception.getMessage(),msg)
            );
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("写入日志文件失败");
        }
        finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getStackStraceInfo(StackTraceElement[] stackTrace) {
        StringBuffer buffer = new StringBuffer();
        for(StackTraceElement el:stackTrace){
            buffer.append(el.toString() + "\n");
        }
        return buffer.toString();
    }

    /**
     * 对于有用的错误消息，可以保存到文件，以便远程定位问题（一般情况不便于）;
     * <br/>
     * 参考： {@link ##file(String, String, boolean)}  ,设置了默认文件输出路径为 ： /sdcard/crash/runtime_log.txt
     * @param msg - 要输出的消息内容
     */
    public static void file( String msg) {
        file("/sdcard/crash/runtime_log.txt",msg,true);
    }
}
