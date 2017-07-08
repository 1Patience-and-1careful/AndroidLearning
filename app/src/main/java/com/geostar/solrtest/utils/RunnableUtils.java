package com.geostar.solrtest.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 替代之前封装在MapActivity中的 任务执行函数
 * <br/>
 * Created by jianghanghang on 2016/12/5.
 */
public class RunnableUtils {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(6);
    public static final int UI_MSG = 0x1024;
    private static UIHandler sUIHandler = new UIHandler(Looper.getMainLooper());

    /**
     * 在UI线程中执行一个Runnable
     * @param task 要执行的任务
     */
    public static void executeOnMainThread(Runnable task){
//        AndroidSchedulers.mainThread().scheduleDirect(task);
        postToMainThread(task,-1);  // 不使用RXJava 的实现
    }

    /**
     * 在UI线程中执行一个Runnable
     * @param task 要执行的任务
     * @param delay  延迟（MILLISECONDS） 1000ms =  1s
     */
    public static void executeOnMainThread(Runnable task, int delay){
//        AndroidSchedulers.mainThread().scheduleDirect(task,delay, TimeUnit.MILLISECONDS);
         postToMainThread(task,delay);    //不使用RXJava 的实现
    }

    /**
     * 在IO 线程中执行一个Runnable
     * @param task 要执行的任务
     */
    public static void executeOnWorkThread(Runnable task){
//        Schedulers.from(EXECUTOR).scheduleDirect(task);
        EXECUTOR.execute(task);
    }

    /**
     * 在IO 线程中执行一个Runnable
     * @param task 要执行的任务
     * @param delay  延迟（MILLISECONDS） 1000ms =  1s
     */
    public static void executeOnWorkThread(final Runnable task, final int delay){
//        Schedulers.from(EXECUTOR).scheduleDirect(task,delay, TimeUnit.MILLISECONDS);
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
            }
        });
    }

    public static void cancelUITask(){
        sUIHandler.removeMessages(UI_MSG);
    }


    /**
     * 提交执行任务
     * @param callable
     * @return
     */
    public static Future submit(Callable callable){
        return EXECUTOR.submit(callable);
    }

    public static void postUI(Runnable runnable) {
        postToMainThread(runnable,0);
    }

    private static class UIHandler extends Handler {

        UIHandler(Looper looper) {
            super(looper);
        }

//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.obj != null){
//                if( msg.arg1 > 0 ){
//                    postDelayed((Runnable) msg.obj,msg.arg1);
//                }else {
//                    post((Runnable) msg.obj);
//                }
//            }
//        }
    }

    private static void postToMainThread(Runnable runnable, int delay){
//        Message msg = sUIHandler.obtainMessage();
//        msg.what = UI_MSG;
//        msg.obj = runnable;
//        msg.arg1 = delay;
        if(sUIHandler == null){
            sUIHandler = new UIHandler(Looper.getMainLooper());
        }
//        sUIHandler.sendMessage(msg);
        if(delay > 0){
            sUIHandler.postDelayed(runnable,delay);
        }else{
            sUIHandler.post(runnable);
        }
    }
}
