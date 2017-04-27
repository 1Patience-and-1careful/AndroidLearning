package com.geostar.solrtest.soap.base;


import com.geostar.solrtest.soap.SoapException;

/**
 * 封装Soap 各个接口的结果解析
 * <br/>Created by hanlyjiang on 2017/4/18.
 */

public interface ISoapResponseHandler<T> {

    T parseResult(Object obj) throws SoapException;

}
