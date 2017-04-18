package com.geostar.solrtest.soap;

import com.geostar.solrtest.soap.base.ISoapReqeustAction;
import com.geostar.solrtest.utils.TestLogUtils;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jianghanghang on 2017/4/18.
 */

public class SoapReqManager {

    private static final boolean DEBUG = true;
    private static final String TAG = "SoapReqManager";

    public final String url;
    public final String namespace;
    private boolean isDebugEnable = true;

    public SoapReqManager() {
        url = "http://192.168.100.115:7001/webservice/ws/queryuser";
        namespace = "http://default.org/";
    }

    /**
     * 请求
     * @param action
     * @param callBack
     */
    public void doRequest(ISoapReqeustAction action, RequestCallBack callBack) {
        Object object = null;
        try {
            object = doRealRequest(action);
        } catch (SoapException e) {
            e.printStackTrace();
            notifyonError(callBack, e);
            return;
        }
        Object obj = null;
        try {
            obj = action.parseResult(object);
            notifyOnSuccess(callBack, obj);
            if(obj == null){
                notifyonError(callBack,new SoapException("转换错误",SoapException.E_SOAP_TRANS_ERROR));
            }
            return;
        } catch (SoapException e) {
            e.printStackTrace();
            notifyonError(callBack,e);
            return;
        }
    }


    public Object doRealRequest(ISoapReqeustAction action) throws SoapException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER11);
        envelope.implicitTypes = true;
        SoapObject soapObject = new SoapObject(namespace,action.getMethodName());
        action.addDataToSoapObject(soapObject);
        envelope.bodyOut = soapObject;// 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;// 由于是.net开发的webservice，所以这里要设置为true

        log(TAG, envelope.bodyOut.toString());

        HttpTransportSE httpTransportSE = new HttpTransportSE(getUrl());

        Object reslt = null;
        httpTransportSE.debug = isDebugEnable;
        try {
            httpTransportSE.call(action.getSoapAction(), envelope);// 调用
            reslt = envelope.getResponse();
        } catch (SoapFault soapFault) {
            throw new SoapException("发生错误:" + soapFault.getMessage(), soapFault, Integer.parseInt(soapFault.faultcode));
        } catch (IOException e) {
            throw new SoapException("发生错误:" + e.getMessage(), e, SoapException.E_NETWORK_ERROR);
        } catch (XmlPullParserException e) {
            throw new SoapException("发生错误:" + e.getMessage(), e, SoapException.E_XML_PARSE_ERROR);
        } finally {
            try {
                httpTransportSE.getServiceConnection().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log(TAG, "请求xml：\n" + httpTransportSE.requestDump);
        log(TAG, "响应xml：\n" + httpTransportSE.responseDump);
        return reslt;
    }

    private static void notifyOnSuccess(RequestCallBack callBack, Object object) {
        if (callBack != null) {
            callBack.onSuccess(object);
        }
    }


    private static void notifyOnFailed(RequestCallBack callBack) {
        if (callBack != null) {
            callBack.onFailed();
        }
    }

    private static void notifyonError(RequestCallBack callBack, Exception e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }

    public String getUrl() {
        return url;
    }


    interface RequestCallBack {

        void onSuccess(Object result);

        void onFailed();

        void onError(Exception e);

    }


    private static void log(String tag, String log) {
        if (DEBUG) {
            TestLogUtils.d(log);
        }
    }


}
