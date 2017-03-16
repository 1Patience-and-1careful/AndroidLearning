package com.geostar.solrtest.ftp;

import android.os.Handler;
import android.os.Looper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jianghanghang on 2017/3/16.
 */

public class UIHandler extends Handler {

    private List<Runnable> tasks = new LinkedList<>();
    private List<Runnable> workTask = Collections.synchronizedList(new LinkedList<Runnable>());

    public UIHandler() {
        super(Looper.getMainLooper());
    }

    public void postUITask(Runnable runnable,int delay){
        if(!tasks.contains(runnable)){
            tasks.add(runnable);
        }
        if( delay > 0 ){
            postDelayed(runnable,delay);
        }else {
            post(runnable);
        }
    }

}
