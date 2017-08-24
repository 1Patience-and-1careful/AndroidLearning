package com.hanlyjiang.learnandroid;

import com.hanlyjiang.learnandroid.json.DatasetTypeAdapter;
import com.hanlyjiang.learnandroid.json.bean.Dataset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hanlyjiang on 2017/8/10-18:17.
 * @version 1.0
 */

public class GsonTypeAdapterTest {


    String datasetJson = "{\n" +
            "  \"title\":\"这是标题\",\n" +
            "  \"child\":{\n" +
            "    \"name\":\"小宝宝\",\n" +
            "    \"age\":\"1\",\n" +
            "    \"gender\":null\n" +
            "  }\n" +
            "}";

    String datasetJson2 = "{\n" +
            "  \"title\":\"这是标题\",\n" +
            "  \"child\":null\n" +
            "}";

    @Test
    public void test_TypeAdapter() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Dataset.Child.class, new DatasetTypeAdapter()).create();
        Dataset dataset = gson.fromJson(datasetJson,Dataset.class);
        Assert.assertEquals("这是标题",dataset.getTitle());
        Assert.assertEquals(3,dataset.getChild().getMap().size());
        Assert.assertEquals("小宝宝",dataset.getChild().getMap().get("name"));
        Assert.assertEquals("1",dataset.getChild().getMap().get("age"));
        Assert.assertEquals("无",dataset.getChild().getMap().get("gender"));
        System.out.println(dataset.getTitle());
    }

    @Test
    public void test_TypeAdapterNull() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Dataset.Child.class, new DatasetTypeAdapter()).create();
        Dataset dataset = gson.fromJson(datasetJson2,Dataset.class);
        Assert.assertEquals("这是标题",dataset.getTitle());
        Assert.assertEquals(null,dataset.getChild());
    }

    @Test
    public void test_TypeAdapterToJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Dataset.Child.class, new DatasetTypeAdapter()).create();
        Dataset dataset = gson.fromJson(datasetJson,Dataset.class);
        Assert.assertEquals("这是标题",dataset.getTitle());
        Assert.assertEquals(3,dataset.getChild().getMap().size());
        Assert.assertEquals("小宝宝",dataset.getChild().getMap().get("name"));
        Assert.assertEquals("1",dataset.getChild().getMap().get("age"));
        Assert.assertEquals("女",dataset.getChild().getMap().get("gender"));
        System.out.println(dataset.getTitle());

        String json = gson.toJson(dataset);
        System.out.println(json);
    }
}
