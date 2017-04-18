package com.geostar.solrtest.soap.base;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by jianghanghang on 2017/4/18.
 */

public interface ISoapReqeustAction<T> extends ISoapResponseHandler<T> {

    /**
     * 构造请求内容对象,往soap 对象中注入内容
     * @return
     * @param soapObject
     */
    void addDataToSoapObject(SoapObject soapObject);

    /**
     * 请求方法名
     * @return
     */
    String getMethodName();

    /**
     * 获取Action
     * @return
     */
    String getSoapAction();


}
