package com.geostar.solrtest.solr;

import android.support.annotation.NonNull;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

/**
 * Solr 查询服务
 * <br/>Created by jianghanghang on 2017/3/9.
 */
public class SolrQueryService {

    //    public static final String SOLR_BASE_URL = "http://192.168.42.107:8080/solr/core0";
    public static final String SOLR_BASE_URL = "http://192.168.31.46:8080/solr/core0";
    private HttpSolrClient solrClient;
    private Class beamClasz;

    private QueryResponse solrResponse;
    private String mServiceUrl;

    /**
     * 构造函数
     * @param mServiceUrl 服务地址
     * @param beanClass Bean 类型
     */
    public SolrQueryService(String mServiceUrl, Class beanClass) {
        this.mServiceUrl = mServiceUrl;
        this.beamClasz = beanClass;
        HttpClient httpClient = new DefaultHttpClient();
        // 设置连接timeout
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
        // 设置获取数据timeout
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
        solrClient = new HttpSolrClient(mServiceUrl, httpClient);

        // SolrClient 自带的timeout设置方法无效，会引发异常
//        solrClient.setConnectionTimeout(2000);
//        solrClient.setSoTimeout(2000);
    }

    /**
     * 查询结果总数
     * @param queryParams
     * @return
     * @throws Exception
     */
    public long queryCount(String queryParams) throws Exception {
        QueryResponse response = querySolrResponse(makeSolrQuery(queryParams, 0, 1));
        if(response.getResults() == null){
            throw new Exception("查询失败");
        }
        return response.getResults().getNumFound();
    }

    /**
     * 查询结果
     * @param queryParams 查询参数
     * @param start 起始结果
     * @param rows 结果数
     * @return 查询结果列表 List<BeanClass>
     */
    public List queryBeans(String queryParams, int start, int rows) throws Exception {
        QueryResponse response = querySolrResponse(makeSolrQuery(queryParams,start,rows));
        return response.getBeans(beamClasz);
    }

    /**
     * 查询结果
     * @param queryParams 查询参数
     * @param start 起始结果
     * @param rows 结果数
     * @return 查询结果列表 List<BeanClass>
     */
    public QueryResponse query(String queryParams, int start, int rows) throws Exception {
        return querySolrResponse(makeSolrQuery(queryParams,start,rows));
    }

    /**
     * 查询结果
     * @param queryParams 查询参数
     * @return 查询结果列表 List<BeanClass>  结果数按服务器默认配置
     */
    public QueryResponse query(String queryParams) throws Exception {
        return querySolrResponse(makeSolrQuery(queryParams,-1,-1));
    }

    /**
     * 直接根据 SolrQuery 查询
     * @param query throws Exception
     * @return 查询结果列表 List<BeanClass>  结果数按服务器默认配置
     * @throws Exception 查询失败
     */
    public QueryResponse query(SolrQuery query)throws Exception {
        return querySolrResponse(query);
    }

    /**
     * 关闭连接
     */
    public  void close(){
        try {
            solrClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private SolrQuery makeSolrQuery(String queryParams, int start, int rows) {
        SolrQuery query = new SolrQuery(queryParams);
        if(start >= 0){
//            query.set("start",start);
            query.setStart(start);
        }
        if(rows > 0) {
//            query.set("rows", rows);
            query.setRows(rows);
        }
//        query.set("timeAllowed",5*1000);//设置timeout 毫秒  如果时间过了还没有查询完毕，则先返回部分结果
        query.setTimeAllowed(5000);
        query.setHighlight(true).setHighlightSnippets(1); // 开启高亮，结果会在 highlighting返回
        // TODO 移出去
        query.setParam("hl.fl", QueryParamCreator.CONTENT); // 设置高亮字段
        return query;
    }

    /**
     * 统一的查询入口
     * @param query
     * @return
     * @throws Exception
     */
    private QueryResponse querySolrResponse(SolrQuery query) throws Exception {
        if(enableDebug){
            System.out.println("Solr:" + mServiceUrl + "  q=" + query.getQuery() + " start:" + query.get("start") + "; rows:" + query.get("rows"));
        }
        try {
            solrResponse = solrClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        if(solrResponse == null){
            throw new Exception("查询失败");
        }
        return solrResponse;
    }
    private boolean enableDebug = true;
}
