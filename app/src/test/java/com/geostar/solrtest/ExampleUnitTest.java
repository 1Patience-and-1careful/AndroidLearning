package com.geostar.solrtest;

import com.geostar.solrtest.soap.SoapRequest;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

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
        System.out.println(test + "\n" + test.replace("\\\\","/"));
        JSONObject jsonObject = new JSONObject("{\"RegionCode\":\"441800000000\",\"RegionName\":\"清远市\"}");
//        System.out.println(jsonObject.getString("RegionCode"));
    }


    @Test
    public void login2Soap(){
        SoapRequest.checkLoginName("zhiqunshen","111111");
    }


}