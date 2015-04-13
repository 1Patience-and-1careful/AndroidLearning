package com.hanly.uiadapter01;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class UIAdapterActivity01 extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uiadapter_activity01);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uiadapter_activity01, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private View rootView = null;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (rootView == null) {
				rootView = inflater.inflate(
					R.layout.fragment_uiadapter_activity01, container, false);
			}
			return rootView;
		}
		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			if( rootView != null ){
				
				// Get the simple TextView
				TextView text = (TextView) rootView.findViewById(R.id.text);
				// Get the layouParams
			    LayoutParams lp = (LayoutParams) text.getLayoutParams();
			    // Get the resources after config changed
			    Resources res = getResources();
			    
			    // Apply new values to textView - text,so it takes affects
			    lp.topMargin = (int) res.getDimension(
			    		R.dimen.text_margin_top);
			    String textStr = res.getString(R.string.hello_world);
			    text.setText(textStr);
			    text.setLayoutParams(lp);
			    
			}
			super.onConfigurationChanged(newConfig);
		}
	}

}
