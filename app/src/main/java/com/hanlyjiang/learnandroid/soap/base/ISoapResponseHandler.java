package com.hanlyjiang.learnandroid.soap.base;


import com.hanlyjiang.learnandroid.soap.SoapException;

/**
 * 封装Soap 各个接口的结果解析
 * <br/>Created by hanlyjiang on 2017/4/18.
 */

public interface ISoapResponseHandler<T> {

    T parseResult(Object obj) throws SoapException;

}
