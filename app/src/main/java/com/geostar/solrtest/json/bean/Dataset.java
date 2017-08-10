package com.geostar.solrtest.json.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hanlyjiang on 2017/8/10-18:13.
 * @version 1.0
 */

public class Dataset {

    String title;
    Child child;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public static class Child {

        private Map<String,String> map = new LinkedHashMap<>();

        public Map<String, String> getMap() {
            return map;
        }

    }

}
