package com.hanly.bitmapdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hanly.bitmapdemo.paintpad.ui.ImageAdapter;
import com.hanly.bitmapdemo.ui.PaintView;

import java.util.HashMap;

import static com.hanly.bitmapdemo.paintpad.helper.BitMapHelper.decodeSampledBitmapFromResource;


public class BitmapDemoActivity extends ActionBarActivity {

    private PaintView mPaintPad = null;
    private PopupWindow mSetBgPopup;
    private View mRootView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = getLayoutInflater().inflate(R.layout.activity_bitmap,null);
        setContentView(R.layout.activity_bitmap);
        mPaintPad = (PaintView) findViewById(R.id.paint_pad);
        mSetBgPopup = initPopup();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bitmap, menu);
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
        if (id == R.id.action_set_background){
            if ( ! mSetBgPopup.isShowing() ){
                mSetBgPopup.showAtLocation(mRootView, Gravity.BOTTOM,0,0);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Init the popup window for setting paint background images
     *
     * @return
     */
    private PopupWindow initPopup() {
        PopupWindow popup = new PopupWindow();
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout popupView = (LinearLayout) inflater.inflate(R.layout.set_bg_popup_layout, null);
        GridView bgGridView = (GridView) popupView.findViewById(R.id.background_grid);

        bgGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item = (HashMap<String, Object>) parent.getAdapter().getItem(position);
                int bgResId = (Integer) item.get(ImageAdapter.KEY_IMAGE_RESOURCE);
                int width = getResources().getDisplayMetrics().widthPixels / 3;
                int height = (int) (1.45 * width);
                Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), bgResId, width, height);
                mPaintPad.setPaintBackGround(bitmap);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        });
        bgGridView.setAdapter(new ImageAdapter(getApplicationContext()));
        popup.setContentView(popupView);
        popup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popup.setOutsideTouchable(true);
        popup.setHeight(getResources().getDimensionPixelSize(R.dimen.popup_high));
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSetBgPopup.isShowing()) {
                    mSetBgPopup.dismiss();
                }
            }
        });
        return popup;
    }
}
