package com.hanlyjiang.learnandroid.json.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 组织结构实体
 *
 * @author hanlyjiang on 2017/8/15-17:38.
 * @version 1.0
 */

public class OrgDept extends OrgUnit {

    String orgid;
    String orgcode;
    String orgname;

    int orglevel;

    @SerializedName("treeNodes")
    List<OrgUnit> orgUnits;

    public static class Ref {
        int orgid;
        int orglevel;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public int getOrglevel() {
        return orglevel;
    }

    public void setOrglevel(int orglevel) {
        this.orglevel = orglevel;
    }

    public List<OrgUnit> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrgUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }
}
