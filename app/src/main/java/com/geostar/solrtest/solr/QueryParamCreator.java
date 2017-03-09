package com.geostar.solrtest.solr;

/**
 * 便于构造查询关键词
 * <br/>http://192.168.42.107:8080/solr/core0/select?q=*%3A*&wt=json&indent=true
 * <br/>Created by jianghanghang on 2017/3/9.
 */
public class QueryParamCreator {
    // 表名
    private static final String TABLE_NAME = "TableName";
    //
    /**
     * 内容字段
     * <br/>"Content":"{\"FILENAME\":\"091230QS2QF关于广东省第二次全国土地调查省级总结报告编写工作实施方案的请示 001.jpg\",\"FILEFULLPATH\":\"ROOT//成果数据//调查室//二次调查//二调报告//091230QS2QF关于广东省第二次全国土地调查省级总结报告编写工作实施方案的请示//091230QS2QF关于广东省第二次全国土地调查省级总结报告编写工作实施方案的请示 001.jpg\"}"
     */
    private static final String CONTENT = "Content";
    /**
     * 存储档案数据的表
     */
    private static final String ARCHIVE_TABLE = "A00GIS_FTPFILE";

    /**
     * 构建档案数据查询Solr查询关键词（q）
     * @param keyword 内容关键词
     * @return 可以供 {@link SolrQueryService#query(String, int, int) SolrQueryService.query方法} 使用的查询参数
     */
    public static String createArchiveQuery(String keyword){
        return String.format("%s:%s AND %s:%s",CONTENT,keyword,TABLE_NAME,ARCHIVE_TABLE);
    }


    /**
     * 构建统计数据查询Solr查询关键词（q）
     * @param keyword 内容关键词
     * @return 可以供 {@link SolrQueryService#query(String, int, int) SolrQueryService.query方法} 使用的查询参数
     */
    public static String createStatisticQuery(String keyword){
        return String.format("%s:%s NOT %s:%s",CONTENT,keyword,TABLE_NAME,ARCHIVE_TABLE);
    }

}
