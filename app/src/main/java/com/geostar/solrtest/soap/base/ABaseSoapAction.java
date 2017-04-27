package com.geostar.solrtest.soap.base;


/**
 *  请求参数及结果解析，请求过程在{@link SoapReqManager SoapReqManager}
 * <br/>Created by hanlyjiang on 2017/4/18.
 */

/**
 *
 * @param <R> 响应结果解析后的java对象类型
 */
public abstract class ABaseSoapAction<R> implements ISoapReqeustAction<R> {

    @Override
    public String getSoapAction() {
        return "";
    }

}
