package com.geostar.solrtest.json;

import android.util.Log;

import com.geostar.solrtest.json.bean.Dataset;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author hanlyjiang on 2017/8/10-18:15.
 * @version 1.0
 */

public class DatasetTypeAdapter extends TypeAdapter<Dataset.Child> {

    @Override
    public void write(JsonWriter out, Dataset.Child value) throws IOException {
//        System.out.println("write called");
//        value.getMap().values();
//        out.beginObject();
//
//        out.endObject();

    }

    @Override
    public Dataset.Child read(JsonReader reader) throws IOException {
        Dataset.Child child = new Dataset.Child();
        JsonToken token = reader.peek();
        if (token.equals(JsonToken.BEGIN_OBJECT)) {
            reader.beginObject();
            String key = null;
            String value = null;
            while (!reader.peek().equals(JsonToken.END_OBJECT)) {
                if (reader.peek().equals(JsonToken.NAME)) {
                    if(reader.peek().equals(JsonToken.NULL)){
                        reader.skipValue();
                        continue;
                    }
                    key = reader.nextName();

                    value = reader.nextString();

                    System.out.println(key + ":" +value);
                    child.getMap().put(key,value);
                }
            }
            reader.endObject();
        }
        System.out.println("read called");
        return child;
    }
}
