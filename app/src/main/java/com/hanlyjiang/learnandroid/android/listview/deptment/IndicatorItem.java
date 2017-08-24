package com.hanlyjiang.learnandroid.android.listview.deptment;

import java.io.Serializable;

/**
 * @author hanlyjiang on 2017/7/9-18:46.
 * @version 1.0
 */

public class IndicatorItem<T> implements Serializable {
    String name;
    T realData;

    public IndicatorItem(String name, T realData) {
        this.name = name;
        this.realData = realData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getRealData() {
        return realData;
    }

    public void setRealData(T realData) {
        this.realData = realData;
    }
}