package com.geostar.solrtest.solr;

import android.support.annotation.NonNull;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrClient;
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

    private SolrClient solrClient;
    private Class beamClasz;

    private String mServiceUrl;
    private QueryResponse solrResponse;

    /**
     * 构造函数
     * @param mServiceUrl 服务地址
     * @param beanClass Bean 类型
     */
    public SolrQueryService(String mServiceUrl, Class beanClass) {
        this.mServiceUrl = mServiceUrl;
        this.beamClasz = beanClass;
        solrClient = new HttpSolrClient(mServiceUrl,new DefaultHttpClient());
    }

    /**
     * 查询结果总数
     * @param queryParams
     * @return
     * @throws Exception
     */
    public long queryCount(String queryParams) throws Exception {
        QueryResponse response = querySolrResponse(makeSolrQuery(queryParams, 0, 10));
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
    public List query(String queryParams, int start, int rows) throws Exception {
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
    public QueryResponse queryResponse(String queryParams, int start, int rows) throws Exception {
        return querySolrResponse(makeSolrQuery(queryParams,start,rows));
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
        query.set("start",start);
        query.set("rows",rows);
        query.set("timeAllowed",30*1000);//设置timeout 毫秒
        return query;
    }

    private QueryResponse querySolrResponse(SolrQuery query) throws Exception {
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
}
