package com.hanlyjiang.learnandroid.android.view;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanlyjiang.learnandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 测试滚动视图中定位滚动到某个视图
 */
public class ScrollViewLocateActivity extends AppCompatActivity {

    public static final int MAX = 20;
    @BindView(R.id.llc_content_view)
    protected LinearLayout containerLayout;

    @BindView(R.id.nsl_root_view)
    protected NestedScrollView nestedScrollView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_locate);
        unbinder = ButterKnife.bind(this);
        loadTestViews();
    }


    private void loadTestViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ViewGroup viewParent = null;
        for (int i = 1; i <= MAX; i++) {
            viewParent = (ViewGroup) layoutInflater.inflate(android.R.layout.simple_list_item_2, containerLayout, false);
            ((TextView) viewParent.findViewById(android.R.id.text1)).setText(i + " -------->" + "据平安武汉 7月27日9时2分，我局接群众报警称，在东西湖六顺路中百物流综合楼内，该公司员工王祥（男，40岁）因工作矛盾，持菜刀砍伤3人后逃走，随后开车至107国道六顺路路口时撞伤4名路人。民警及时出警赶到现场，果断开枪将王击伤并控制。\n" +
                    "\n" +
                    "截止目前，3名群众经抢救无效死亡，其余伤者正在医院救治中。案件正在进一步调查侦办。请广大网友不要传播血腥图片视频，不信谣不传谣。" + "");
            ((TextView) viewParent.findViewById(android.R.id.text2)).setText(i + " -------->" + "请广大网友不要传播血腥图片视频，不信谣不传谣。" + "");
            containerLayout.addView(viewParent);
        }
    }


    @OnClick(R.id.btn_locate_item)
    void locateAItem() {
        int id = 10;
        int[] location = new int[2];
//        for (int i = 0; i < MAX; i++) {
//            id = i;
//            View targetView = containerLayout.getChildAt(id);
//            targetView.getLocationOnScreen(location);
//            System.out.println(String.format("第 %d 个 --> X: %d  Y: %d", id + 1, location[0], location[1]));
//        }
        View targetView = containerLayout.getChildAt(id);
        location[0] = 0;
        location[1] = targetView.getTop() + containerLayout.getTop();
        nestedScrollView.smoothScrollTo(location[0], location[1]);
        System.out.println(String.format("第 %d 个 --> X: %d  Y: %d", id + 1, location[0], location[1]));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
