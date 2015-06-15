package com.hanly.alarmdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;


public class AlarmMainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final  String TAG = "AlarmMainActivity";
    private int mPickHour = 0;
    private int mPickMinute = 0;
    private TimePicker mTimePicker = null;
    private String mAlarmType = null;
    private AlarmManager mAlarmMgr = null;
    private PendingIntent mAlarmIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.v(TAG, "hourOfDay : " + hourOfDay + ", minute:" + minute);
                mPickHour = hourOfDay;
                mPickMinute = minute;
            }
        });

        mAlarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_main, menu);
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

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.set_alarm_btn:

                Toast.makeText(getApplicationContext(), "Set Alarm:" + mAlarmType, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                mAlarmIntent = intent.
                break;
            default:
                break;
        }

    }
}
