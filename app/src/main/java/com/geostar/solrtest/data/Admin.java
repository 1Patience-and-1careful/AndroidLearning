package com.geostar.solrtest.data;

/**
 * @author hanlyjiang on 2017/5/26-下午11:04.
 * @version 1.0
 */

public class Admin {

    private String name;

    private int code;

    public Admin(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
