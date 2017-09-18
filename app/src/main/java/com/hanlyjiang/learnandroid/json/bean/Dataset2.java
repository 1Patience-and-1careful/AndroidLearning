package com.hanlyjiang.learnandroid.json.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hanlyjiang on 2017/8/10-18:13.
 * @version 1.0
 */

public class Dataset2 {

    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private Map<String, String> child = new LinkedHashMap<>();

    public Map<String, String> getChild() {
        return child;
    }

    public void setChild(Map<String, String> child) {
        this.child = child;
    }
}
