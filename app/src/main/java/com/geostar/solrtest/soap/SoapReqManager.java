package com.geostar.solrtest.soap;


import com.geostar.solrtest.soap.base.ISoapReqeustAction;
import com.geostar.solrtest.utils.RunnableUtils;
import com.geostar.solrtest.utils.TestLogUtils;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Soap 请求过程封装，通过{@link #doRequest(ISoapReqeustAction, RequestCallBack) doRequest(action)} 传入action 来请求不同的接口及对相应的数据进行解析
 * <br/>Created by hanlyjiang on 2017/4/18.
 */

public class SoapReqManager {

    private static final boolean DEBUG = true;
    private static final String TAG = "SoapReqManager";

    public final String url;
    public final String namespace;
    private boolean isDebugEnable = true;
    private int version = SoapSerializationEnvelope.VER11;
    private boolean dotNet;

    /**
     * 服务地址
     * @param url 服务url地址 查看Wsdl 的 wsdl:service 标签段
     * @param ver 请求使用的 soap 版本，有的服务器支持多个版本  查看wsdl 开始标签中 xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
     * @param namespace 命名空间 必须正确设置  wsdl 中的 wsdl 开始标签中  targetNamespace="http://WebXml.com.cn/
     */
    public SoapReqManager(String url, int ver, String namespace) {
        this.url = url;
        this.namespace = namespace;
        this.version = ver;
    }

    public void setDoNet(boolean doNet) {
        this.dotNet = doNet;
    }


    /**
     * 请求，在UI线程中回调callback方法
     *
     * @param action
     * @param callBack
     */
    public void doRequest(ISoapReqeustAction action, RequestCallBack callBack) {
        doRequest(action, callBack, true);
    }


    /**
     * 请求Soap 服务接口
     *
     * @param action
     * @param callBack
     * @param notifyOnUI 时候在UI 线程中回调 callback 参数的方法
     */
    public void doRequest(ISoapReqeustAction action, RequestCallBack callBack, boolean notifyOnUI) {
        Object object = null;
        try {
            object = doRealRequest(action);
        } catch (SoapException e) {
            e.printStackTrace();
            notifyonError(callBack, e, notifyOnUI);
            return;
        }
        Object obj = null;
        try {
            obj = action.parseResult(object);
        } catch (SoapException e) {
            e.printStackTrace();
            notifyonError(callBack, new SoapException("转换错误", SoapException.E_SOAP_TRANS_ERROR), notifyOnUI);
        }
        if (obj == null) {
            notifyonError(callBack, new SoapException("转换错误", SoapException.E_SOAP_TRANS_ERROR), notifyOnUI);
            return;
        }
        notifyOnSuccess(callBack, obj, notifyOnUI);
        return;
    }


    protected Object doRealRequest(ISoapReqeustAction action) throws SoapException {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                version);// 注意服务版本
        envelope.implicitTypes = true;

        SoapObject soapObject = new SoapObject(namespace, action.getMethodName());
        action.addDataToSoapObject(soapObject);
        envelope.bodyOut = soapObject;// 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = dotNet;// 是否 .Net-Services webservice，如果是这里要设置为true  否则得设置为false 如果设置不正确可能无法正确请求及解析

        log(TAG, envelope.bodyOut.toString());

        HttpTransportSE httpTransportSE = new HttpTransportSE(getUrl());

        Object reslt = null;
        httpTransportSE.debug = isDebugEnable;
        try {
            httpTransportSE.call(action.getSoapAction(), envelope);// 调用
            reslt = envelope.getResponse();
            log(TAG, "请求xml：\n" + httpTransportSE.requestDump);
            log(TAG, "响应xml：\n" + httpTransportSE.responseDump);
        } catch (SoapFault soapFault) {
            throw new SoapException("发生错误:" + soapFault.getMessage(), soapFault, SoapException.E_SERVER_ERROR);
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
        return reslt;
    }

    private static void notifyOnSuccess(final RequestCallBack callBack, final Object object, boolean notifyOnUI) {
        if (callBack != null) {
            if (notifyOnUI) {
                RunnableUtils.executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(object);
                    }
                });
            } else {
                callBack.onSuccess(object);
            }
        }
    }


    private static void notifyOnFailed(final RequestCallBack callBack, boolean notifyOnUI) {
        if (callBack != null) {
            if (notifyOnUI) {
                RunnableUtils.executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed();
                    }
                });
            } else {
                callBack.onFailed();
            }
        }
    }

    private static void notifyonError(final RequestCallBack callBack, final Exception e, boolean notifyOnUI) {
        if (callBack != null) {
            if (notifyOnUI) {
                RunnableUtils.executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            } else {
                callBack.onError(e);
            }
        }
    }

    public String getUrl() {
        return url;
    }


    public interface RequestCallBack {

        void onSuccess(Object result);

        void onFailed();

        void onError(Exception e);

    }


    private static void log(String tag, String log) {
        if (DEBUG) {
            TestLogUtils.d(log);
        }
    }


    // 在登录时会因为方法<element> 中添加了一个namespace 而无法正确获取请求数据

        /*
        <v:Envelope xmlns:c="http://schemas.xmlsoap.org/soap/encoding/"
            xmlns:d="http://www.w3.org/2001/XMLSchema" xmlns:i="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:v="http://schemas.xmlsoap.org/soap/envelope/">
            <v:Header />
            <v:Body>
            <v:checkLoginName id="o0" c:root="1">
            <loginName>xxxx</loginName>
            <password>xxxx</password>
            </v:checkLoginName >
            </v:Body>
            </v:Envelope>
         */

}
