package com.hanlyjiang.learnandroid.soap.login;

import com.hanlyjiang.learnandroid.soap.SoapException;
import com.hanlyjiang.learnandroid.soap.base.ABaseSoapAction;

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
        return "getSupportCity";
    }

    @Override
    public String getSoapAction() {
        return "http://WebXml.com.cn/getSupportCity";
    }

    @Override
    public String parseResult(Object obj) throws SoapException {
        return obj.toString();
    }

    /*
    <v:Envelope
	xmlns:i="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:d="http://www.w3.org/2001/XMLSchema"
	xmlns:c="http://www.w3.org/2003/05/soap-encoding"
	xmlns:v="http://www.w3.org/2003/05/soap-envelope">
	<v:Header />
	<v:Body>
		<getSupportCity xmlns="http://WebXml.com.cn/" id="o0" c:root="1">
			<byProvinceName>ALL</byProvinceName>
		</getSupportCity>
	</v:Body>
    </v:Envelope>
     */

    /*

    WSDL
    http://www.webxml.com.cn/Webservices/WeatherWebService.asmx?WSDL

    接口说明：
    http://www.webxml.com.cn/Webservices/WeatherWebService.asmx?op=getSupportCity
     */
}
