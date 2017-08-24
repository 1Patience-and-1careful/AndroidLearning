package com.hanlyjiang.learnandroid.soap.base;

import org.ksoap2.serialization.SoapObject;

/**
 * 封装Soap 各个接口的请求参数
 * <br/>Created by hanlyjiang on 2017/4/18.
 */

public interface ISoapReqeustAction<T> extends ISoapResponseHandler<T> {

    /**
     * 构造请求内容对象,往soap 对象中注入内容
     * @return
     * @param soapObject
     */
    void addDataToSoapObject(SoapObject soapObject);

    /**
     * 请求方法名  wsdl:binding 中Operation 名称
     * @return 返回 operation 标签的 Name
     */
    String getMethodName();

    /**
     * 获取Action  wsdl:binding 中Operation 下 soapAction 的值
     * @return
     */
    String getSoapAction();


}
