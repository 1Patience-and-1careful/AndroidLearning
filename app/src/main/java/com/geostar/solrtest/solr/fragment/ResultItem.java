
package com.geostar.solrtest.solr.fragment;


import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * Solr 返回结果 Bean
 */
public class ResultItem implements Serializable{

    @Field("Content")
    private String mContent;
    @Field("DataBase")
    private String mDataBase;
    @Field("id")
    private String mId;
    @Field("TableAliasName")
    private String mTableAliasName;
    @Field("TableName")
    private String mTableName;
    @Field("_version_")
    private Long m_version;

    public String getContent() {
        return mContent;
    }

    public String getDataBase() {
        return mDataBase;
    }

    public String getId() {
        return mId;
    }

    public String getTableAliasName() {
        return mTableAliasName;
    }

    public String getTableName() {
        return mTableName;
    }

    public Long get_version() {
        return m_version;
    }

    @Override
    public String toString() {
        return String.format("id:%s\n" +
                "Content:%s\n" +
                "TableAliasName:%s\n" +
                "TableName:%s\n" +
                "DataBase:%s\n" +
                "_version_:%d",
                mId,mContent,mTableAliasName,mTableName,mDataBase,m_version);
    }

    public static class Builder {

        private String mContent;
        private String mDataBase;
        private String mId;
        private String mTableAliasName;
        private String mTableName;
        private Long m_version;

        public ResultItem.Builder withContent(String Content) {
            mContent = Content;
            return this;
        }

        public ResultItem.Builder withDataBase(String DataBase) {
            mDataBase = DataBase;
            return this;
        }

        public ResultItem.Builder withId(String id) {
            mId = id;
            return this;
        }

        public ResultItem.Builder withTableAliasName(String TableAliasName) {
            mTableAliasName = TableAliasName;
            return this;
        }

        public ResultItem.Builder withTableName(String TableName) {
            mTableName = TableName;
            return this;
        }

        public ResultItem.Builder with_version(Long _version) {
            m_version = _version;
            return this;
        }

        public ResultItem build() {
            ResultItem ResultItem = new ResultItem();
            ResultItem.mContent = mContent;
            ResultItem.mDataBase = mDataBase;
            ResultItem.mId = mId;
            ResultItem.mTableAliasName = mTableAliasName;
            ResultItem.mTableName = mTableName;
            ResultItem.m_version = m_version;
            return ResultItem;
        }

    }

}
