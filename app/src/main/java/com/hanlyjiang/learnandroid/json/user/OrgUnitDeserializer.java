package com.hanlyjiang.learnandroid.json.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * 用于组织结构数据解析
 *
 * @author hanlyjiang on 2017/8/15-23:10.
 * @version 1.0
 */

public class OrgUnitDeserializer implements JsonDeserializer<OrgUnit> {

    /**
     * 代表是组织结构
     */
    public static final String ORG_ORGANIZATION = "OrgOrganization";
    /**
     * 代表是用户
     */
    public static final String ORG_EMPLOYEE = "OrgEmployee";
    /**
     * 节点数据类型 组织结构/用户
     */
    public static final String NODE_TYPE = "nodeType";

    public static Gson gson = new GsonBuilder().registerTypeAdapter(OrgUnit.class, new OrgUnitDeserializer()).create();

    @Override
    public OrgUnit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Class childType = null;
        if (json.isJsonObject()) {
            JsonObject jsonObject = (JsonObject) json;
            String nodeType = jsonObject.get(NODE_TYPE).getAsString();
            if (ORG_ORGANIZATION.equalsIgnoreCase(nodeType)) {
                childType = OrgDept.class;
            } else if (ORG_EMPLOYEE.equalsIgnoreCase(nodeType)) {
                childType = OrgUser.class;
            } else {
                childType = OrgUnit.class;
            }
        }
        return (OrgUnit) gson.fromJson(json, childType);
    }
}
