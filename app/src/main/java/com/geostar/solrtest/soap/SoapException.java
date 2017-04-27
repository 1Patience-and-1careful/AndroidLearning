package com.geostar.solrtest.soap;

/**
 * Created by hanlyjiang on 2017/4/18.
 */
public class SoapException extends Exception {

    private int errorCode;
    public static final int E_NETWORK_ERROR = 101;
    public static final int E_XML_PARSE_ERROR = 102;
    public static final int E_SERVER_ERROR = 103;
    /**
     *  对象转换错误
     */
    public static final int E_SOAP_TRANS_ERROR = 103;

    public SoapException(String detailMessage, int errorCode) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public SoapException(String detailMessage, Throwable throwable, int errorCode) {
        super(detailMessage, throwable);
        this.errorCode = errorCode;
    }

    public SoapException(Throwable throwable, int errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
