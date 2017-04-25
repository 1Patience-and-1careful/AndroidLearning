package com.geostar.solrtest.soap;

import com.geostar.solrtest.utils.TestLogUtils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by HanlyJiang on 2017/4/18.
 */

public class SoapRequest {
    private static final String TAG = "SoapRequest";
    private static final boolean DEBUG = true;

    /*
    定义地址： http://192.168.100.115:7001/webservice/ws/queryuser?wsdl


     */
    public static final String URL = "http://192.168.100.115:7001/webservice/ws/queryuser";
    public static final String NAMESPACE = "http://default.org/";
    public static final String METHOD = "UploadData";

    public static void testConnection() {
    }

    public static void checkLoginName(String userName, String password) {
        String method = "checkLoginName";
        String soapAction = "checkLoginName";

        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("loginName", userName);
        request.addProperty("password", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.bodyOut = request;// 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;// 由于是.net开发的webservice，所以这里要设置为true

        log(TAG, request.toString());

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        Object reslt = null;
        httpTransportSE.debug = true;
        try {
            httpTransportSE.call("", envelope);// 调用
            log(TAG, "请求xml：\n" + httpTransportSE.requestDump);
            reslt = envelope.getResponse();
            log(TAG, "响应xml：\n" + httpTransportSE.responseDump);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            try {
                httpTransportSE.getServiceConnection().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void log(String tag, String log) {
        if (DEBUG) {
            TestLogUtils.d(log);
        }
    }

}
