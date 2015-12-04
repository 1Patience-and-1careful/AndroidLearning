package com.hanlyjiang.android.input;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * http://www.cnblogs.com/zhwl/archive/2012/07/06/2579073.html
 * 
 * @author jianghanghang 软键盘显示与隐藏： <li>1.
 * 
 *         <pre>
 * Activity中设置：
 *           Android:windowSoftInputMode
 *           ="stateUnspecified",
 *           默认设置：软键盘的状态(隐藏或可见)没有被指定。
 *          系统将选择一个合适的状态或依赖于主题的设置。
 *          "stateUnchanged", 软键盘被保持上次的状态。
 *           "stateHidden", 当用户选择该Activity时，软键盘被隐藏。
 *          "stateAlwaysHidden", 软键盘总是被隐藏的。
 *           "stateVisible",. 软键盘是可见的。
 *          "stateAlwaysVisible", 当用户选择这个Activity时，软键盘是可见的。
 *          "adjustUnspecified", . 它不被指定是否该Activity主窗口调整大小以便留出软键盘的空间， 或是否窗口上的内容得到屏幕上当前的焦点是可见的。系统将自动选择这些模式中一种主要依赖于是否窗口的内容有任何布局视图能够滚动他们的内容。
 *           
 *          如果有这样的一个视图，这个窗口将调整大小，这样的假设可以使滚动窗口的内容在一个较小的区域中可见的。这个是主窗口默认的行为设置。也就是说，
 *         系统自动决定是采用平移模式还是压缩模式，决定因素在于内容是否可以滚动。
 *           "adjustResize", （压缩模式）
 *          当软键盘弹出时，要对主窗口调整屏幕的大小以便留出软键盘的空间。
 *           "adjustPan" ,（平移模式：当输入框不会被遮挡时，该模式没有对布局进行调整，然而当输入框将要被遮挡时，
 *          窗口就会进行平移。也就是说，该模式始终是保持输入框为可见
 * </pre>
 * 
 *         </li> <li>2.
 * 
 *         <pre>
 * EditText默认不弹出软件键盘
 * 方法一：
 * 在AndroidMainfest.xml中选择哪个activity，设置windowSoftInputMode属性为adjustUnspecified|stateHidden
 * 例如：<activity Android:name=".Main"
 *                   Android:label="@string/app_name"
 *                   Android:windowSoftInputMode="adjustUnspecified|stateHidden"
 *                   Android:configChanges="orientation|keyboardHidden">
 *             <intent-filter>
 *                 <action Android:name="android.intent.action.MAIN" />
 *                 <category Android:name="android.intent.category.LAUNCHER" />
 *             </intent-filter>
 *         </activity>
 * 方法二：
 * 让EditText失去焦点，使用EditText的clearFocus方法
 * 例如：EditText edit=(EditText)findViewById(R.id.edit);
 *            edit.clearFocus();
 * 方法三：
 * 强制隐藏Android输入法窗口
 * 例如：EditText edit=(EditText)findViewById(R.id.edit);  
 *            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
 *            imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
 * 5.EditText始终不弹出软件键盘
 * 例：EditText edit=(EditText)findViewById(R.id.edit);
 *        edit.setInputType(InputType.TYPE_NULL);
 * </pre>
 * 
 *         </li>
 *
 */
public class UtilsInputMethod {

	private Activity mActivity;

	public UtilsInputMethod() {
	}

	public UtilsInputMethod(Activity a) {
		mActivity = a;
	}

	private Activity getActivity() {
		return mActivity;
	}

	/**
	 * 显示软键盘
	 * 
	 * @param view
	 */
	public void showKeyboard(View view) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1
				&& view.hasFocus()) {
			view.clearFocus();
		}
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}

	public void showSoftKeyboard(View view) {
		if (view.requestFocus()) {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param v
	 */
	public void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}


	private void setEditTextInputKeyboardListener(EditText edit) {
		edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showKeyboard(v);
				} else {
					hideSoftKeyboard(v);
				}
			}
		});
	}

}
