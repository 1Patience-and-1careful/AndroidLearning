package com.hanly.alarmdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


public class AlarmMainActivity extends ActionBarActivity implements View.OnClickListener {

    private Spinner mAlarmTypeChooser = null;
    private TimePicker mTimePicker = null;
    private String mAlarmType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        mAlarmTypeChooser = (Spinner)findViewById(R.id.type_chooser);
        mTimePicker = (TimePicker)findViewById(R.id.time_picker);
        String[] strArray = getResources().getStringArray(R.array.Alarm_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strArray);
        mAlarmTypeChooser.setAdapter(adapter);
        mAlarmTypeChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   mAlarmType = (String)parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        switch(vId){
            case R.id.set_alarm_btn:
                Toast.makeText(getApplicationContext(),"Set Alarm:" + mAlarmType,Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }

    }
}
