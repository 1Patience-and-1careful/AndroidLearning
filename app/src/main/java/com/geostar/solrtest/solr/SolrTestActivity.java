package com.geostar.solrtest.solr;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.geostar.solrtest.R;
import com.geostar.solrtest.solr.fragment.ItemFragment;
import com.geostar.solrtest.solr.fragment.ResultItem;
import com.geostar.solrtest.utils.RunnableUtils;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


/**
 * Solr 搜索测试
 */
public class SolrTestActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener,
        View.OnClickListener,Callable {

    public static final String SOLR_BASE_URL = "http://192.168.42.107:8080/solr/core0";
    private static final String TAG = "SolrTest";
    Future currQueryTask = null;
    private HttpSolrClient httpSolrClient;
    private View mFragmentStub;
    ProgressDialog progressDialog;
    private EditText queryText;

    private SolrQueryService mQueryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solr_test);
        findViewById(R.id.btn_search_solr).setOnClickListener(this);
        mFragmentStub = findViewById(R.id.fl_fragment_stub);
        progressDialog = new ProgressDialog(this,0);
        progressDialog.setTitle("正在查询...");
        queryText = (EditText) findViewById(R.id.et_input_keyword);
        queryText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    startQuery();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        startQuery();
    }

    private void startQuery() {
        if(currQueryTask != null && !currQueryTask.isDone()){
            currQueryTask.cancel(true);
        }
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
        currQueryTask = RunnableUtils.submit(this);
    }

    /**
     * 后台线程查询
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        if(mQueryService == null) {
            mQueryService = new SolrQueryService(SOLR_BASE_URL,ResultItem.class);
        }
        QueryResponse resp = null;
        try {
            resp = mQueryService.query(queryText.getText().toString());
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        if(resp != null){
            Log.d(TAG,"Get Solr Test Response");
            final ArrayList<ResultItem> resultItems = (ArrayList<ResultItem>) resp.getBeans(ResultItem.class);
            Log.d(TAG,String.format("起始数：%d,总数：%d ; 返回结果数：%d",resp.getResults().getStart(),resp.getResults().getNumFound(),
                    resp.getResults().size()));
            RunnableUtils.executeOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if(getFragmentManager().findFragmentById(R.id.fl_fragment_stub) != null){
                        ItemFragment fragment = (ItemFragment) getFragmentManager().findFragmentById(R.id.fl_fragment_stub);
                        fragment.updateData(resultItems);
                    }else{
                        ItemFragment fragment = ItemFragment.newInstance(1,resultItems);
                        getFragmentManager()
                                .beginTransaction()
                                .add(R.id.fl_fragment_stub,fragment)
                                .commit();
                    }
                    progressDialog.dismiss();
                }
            });
        }else{
            Log.d(TAG,"Get Response Failed");
            RunnableUtils.executeOnMainThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }
        return null;
    }

    @Override
    public void onListFragmentInteraction(ResultItem item) {
        Toast.makeText(this, "Click " + item.getTableName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
