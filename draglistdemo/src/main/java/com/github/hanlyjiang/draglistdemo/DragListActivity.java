package com.github.hanlyjiang.draglistdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.List;

public class DragListActivity extends AppCompatActivity {

    private DragListView mDragListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        DragListView listview;
        mDragListView = (DragListView) findViewById(R.id.draglistview);
        mDragListView.setVerticalScrollBarEnabled(true);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Log.d("Debug", "OnDragStart");
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                Log.d("Debug", "OnItemDragEnd");

            }
        });
        mDragListView.setLayoutManager(new LinearLayoutManager(this));
        List<DragData> data = new ArrayList<DragData>();
        for(int i=0; i<  20; i++){
            data.add(new DragData("--------- "+i +"---------",i ));
        }
        ItemAdapter mAdaper = new ItemAdapter(data,R.layout.list_item,R.id.imageView2,true);
        mDragListView.setAdapter(mAdaper,false);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(new MyDragItem(getApplicationContext(),R.layout.list_item));


    }

    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            TextView text = (TextView) clickedView.findViewById(R.id.textView);
            String content = text.getText().toString();
            ((TextView)dragView.findViewById(R.id.textView)).setText(content);
            dragView.setBackgroundColor(0x0f000000);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drag_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
