package com.geostar.solrtest.soap.base;

/**
 * Created by jianghanghang on 2017/4/18.
 */

public abstract class ABaseSoapAction<L> implements ISoapReqeustAction<L> {

    @Override
    public String getSoapAction() {
        return "";
    }

}
