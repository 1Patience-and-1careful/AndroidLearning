package com.hanlyjiang.android.widget;


public class Popup1 {

	public Popup1() {
		// TODO Auto-generated constructor stub
	}
	
	/* *
	 * 
	 * Popup window 点击返回键消失
	 * 
	 * 
	 */

	
//	private void initUi() {
//		View contentView = View.inflate(mapActivity,
//				R.layout.route_start_or_end_select_popup, null);
//		contentView.setOnKeyListener(new OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
//		            PoiSelectPopUp.this.dismiss();
//				return true;
//			}
//		});
//		mCancelBtn = (Button) contentView
//				.findViewById(R.id.btn_route_startend_select_popup_cancel_btn);
//		mTitleText = (TextView) contentView
//				.findViewById(R.id.tv_route_startend_select_popup_title);
//		mPageIndicatorText = (TextView) contentView.findViewById(R.id.tv_route_startend_select_page_indicator);
//		mCancelBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				PoiSelectPopUp.this.dismiss();
//			}
//		});
//		mPoiXListView = (XListView) contentView
//				.findViewById(R.id.xlist_route_startend_select);
//
//		mAdapter = new PoiSugListAdapter(mapActivity, mData);
//		mPoiXListView.setAdapter(mAdapter);
//
//		mPoiXListView.setPullLoadEnable(true);
//		mPoiXListView.setPullRefreshEnable(true);
//
//		mPoiXListView.setOnItemClickListener(mOnPoiSelect);
//		mPoiXListView.setXListViewListener(mXListLoadDataListener);
//		this.setContentView(contentView);
//		
//		updateFooterAndHeader();
//		
//		this.setOutsideTouchable(false);
//		// 设置此参数获得焦点，否则无法点击
//		this.setFocusable(true);
//
//		// popupWindow.setBackgroundDrawable(new BitmapDrawable()); //comment by
//		// danielinbiti,如果添加了这行，那么标注1和标注2那两行不用加，加上这行的效果是点popupwindow的外边也能关闭
//		contentView.setFocusable(true);// comment by
//										// danielinbiti,设置view能够接听事件，标注1
//		contentView.setFocusableInTouchMode(true); //comment by danielinbiti,设置view能够接听事件 标注2 
//	}
}
