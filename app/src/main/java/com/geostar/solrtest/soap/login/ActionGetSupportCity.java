package com.geostar.solrtest.soap.login;

import com.geostar.solrtest.soap.SoapException;
import com.geostar.solrtest.soap.base.ABaseSoapAction;

import org.ksoap2.serialization.SoapObject;

/**
 * http://www.webxml.com.cn/zh_cn/weather_icon.aspx
 * Created by hanlyjiang on 2017/4/27.
 */
public class ActionGetSupportCity extends ABaseSoapAction<String> {

    private String byProvinceName;

    public ActionGetSupportCity(String byProvinceName) {
        this.byProvinceName = byProvinceName;
    }

    @Override
    public void addDataToSoapObject(SoapObject soapObject) {
        soapObject.addProperty("byProvinceName",byProvinceName);
    }

    @Override
    public String getMethodName() {
        return "http://WebXml.com.cn/getSupportCity";
    }

    @Override
    public String parseResult(Object obj) throws SoapException {
        return obj.toString();
    }
}
