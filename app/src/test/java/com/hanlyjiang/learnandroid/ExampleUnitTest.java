package com.hanlyjiang.learnandroid;

import com.hanlyjiang.learnandroid.soap.SoapReqManager;
import com.hanlyjiang.learnandroid.soap.login.ActionGetSupportCity;
import com.hanlyjiang.learnandroid.utils.TestLogUtils;

import org.junit.Test;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void addition_StringReplace() throws Exception {
        String test = "temp\\\\hello.text";
        System.out.println(test + "\n" + test.replace("\\\\", "/"));
    }


    String url = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
    @Test
    public void login2Soap() {
        final SoapReqManager reqManager = new SoapReqManager(url, SoapSerializationEnvelope.VER12,"http://WebXml.com.cn/");
        reqManager.setDoNet(true);
        reqManager.doRequest(new ActionGetSupportCity("ALL"), new SoapReqManager.RequestCallBack() {
            @Override
            public void onSuccess(Object result) {
                TestLogUtils.d(result.toString());
            }

            @Override
            public void onFailed() {
                TestLogUtils.d("onFailed");
            }

            @Override
            public void onError(Exception e) {
                TestLogUtils.d("onError");
            }
        }, false);
    }


}