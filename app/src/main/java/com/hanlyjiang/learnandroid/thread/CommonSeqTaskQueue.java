package com.hanlyjiang.learnandroid.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hanlyjiang on 2017/10/16-14:51.
 * @version 1.0
 */

public class CommonSeqTaskQueue {

    private static List<Runnable> runnableList = Collections.synchronizedList(new ArrayList<Runnable>());
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void addTask(Runnable runnable) {
        runnableList.add(runnable);
        executor.execute(runnable);
    }

}
