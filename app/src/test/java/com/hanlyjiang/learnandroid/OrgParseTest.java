package com.hanlyjiang.learnandroid;


import com.hanlyjiang.learnandroid.json.user.OrgUnitDeserializer;
import com.hanlyjiang.learnandroid.json.user.UserResp;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author hanlyjiang on 2017/8/15-17:54.
 * @version 1.0
 */

public class OrgParseTest {

    @Test
    public void test_Parse() throws FileNotFoundException {
//        Gson gson = new GsonBuilder().registerTypeAdapter(OrgUnit.class, new OrgUnitDeserializer()).create();
        File jsonFile = new File("./app/src/main/assets/contacts.json");
        UserResp userResp = OrgUnitDeserializer.gson.fromJson(new FileReader(jsonFile), UserResp.class);
        Assert.assertNotNull(userResp);
    }
}
