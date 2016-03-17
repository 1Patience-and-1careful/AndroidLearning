package com.github.hanlyjiang.draglistdemo;

/**
 * Created by HanlyJiang on 2016/3/17.
 */
public class DragData {

    private String name;
    private int postion;

    public DragData(String name, int postion) {
        this.name = name;
        this.postion = postion;
    }

    public int getPos() {
        return postion;
    }

    public void setAge(int postion) {
        this.postion = postion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
