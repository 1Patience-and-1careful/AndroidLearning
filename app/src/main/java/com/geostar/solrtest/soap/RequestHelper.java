package com.geostar.solrtest.soap;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hanlyjiang on 2017/4/18.
 */

public class RequestHelper {
    private static final String TAG = "RequestHelper";
    private static final boolean DEBUG = true;


    /*
    定义地址： http://192.168.100.115:7001/webservice/ws/queryuser?wsdl


     */
    public static final String URL = "http://192.168.100.115:7001/webservice/ws/queryuser";
    public static final String NAMESPACE = "http://default.org/";
    public static final String METHOD = "UploadData";
    public static final String SOAP_ACTION = "http://default.org/UploadData";

    public static void testConnection() {
    }

    public static void checkLoginName(String userName, String password) {
        String method = "checkLoginName";
        String soapAction = "checkLoginName";

        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("loginName", "test");
        request.addProperty("password", "123456");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER12);
        envelope.implicitTypes = true;
        envelope.bodyOut = request;// 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;// 由于是.net开发的webservice，所以这里要设置为true

        log(TAG, request.toString());

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        Object reslt = null;
        httpTransportSE.debug = true;
        try {
            httpTransportSE.call(soapAction, envelope);// 调用
            log(TAG, "请求ml：\n" + httpTransportSE.requestDump);
            reslt = envelope.getResponse();
            log(TAG, "响应xml：\n" + httpTransportSE.responseDump);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {

        }
    }


    private static void log(String tag, String log) {
        if (DEBUG) {
            Log.v(tag, log);
        }
    }
}
