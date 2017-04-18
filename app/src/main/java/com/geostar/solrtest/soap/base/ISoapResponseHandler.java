package com.geostar.solrtest.soap.base;

import com.geostar.solrtest.soap.SoapException;

/**
 * Created by jianghanghang on 2017/4/18.
 */

public interface ISoapResponseHandler<T> {

    T parseResult(Object obj) throws SoapException;

}
