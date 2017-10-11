package com.hanlyjiang.learnandroid.android.process;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.hanlyjiang.learnandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class KillProcessActivity extends AppCompatActivity {

    @BindView(R.id.btn_kill_process)
    Button btnKillProcess;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_process);
        unbinder = ButterKnife.bind(this);


    }


    @OnClick(R.id.btn_kill_process)
    public void killProcess() {
        Toast.makeText(this, " 开始杀进程", Toast.LENGTH_SHORT).show();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses("com.geostar.guangzhou.oa");
    }
}
