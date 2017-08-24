package com.hanlyjiang.learnandroid.json.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取组织结构数据的响应
 *
 * @author hanlyjiang on 2017/8/15-22:34.
 * @version 1.0
 */

public class UserResp {

    @SerializedName("treeNodes")
    List<OrgUnit> nodes;

    public List<OrgUnit> getNodes() {
        return nodes;
    }

    public void setNodes(List<OrgUnit> nodes) {
        this.nodes = nodes;
    }
}
