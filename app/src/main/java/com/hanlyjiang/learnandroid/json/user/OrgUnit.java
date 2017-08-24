package com.hanlyjiang.learnandroid.json.user;

/**
 * 组织结构中的单元实体
 *
 * @author hanlyjiang on 2017/8/15-17:35.
 * @version 1.0
 */

public class OrgUnit {

    String nodeId;
    String nodeType;
    String nodeName;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
