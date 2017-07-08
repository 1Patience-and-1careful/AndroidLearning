package com.geostar.solrtest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geostar.solrtest.R;


/**
 * 朋友圈评论输入框
 *
 * @author hanlyjiang on 2017/6/27-14:50.
 * @version 1.0
 */

public class CircleCommentPopup implements View.OnClickListener, TextView.OnEditorActionListener {

    private Context context;

    //    private PopupWindow popupWindow;
    private Dialog dialog;

    private Listener mListener;

    private EditText etInutComment;
    private TextView btnSubmitComment;

    public CircleCommentPopup(Context context) {
        this.context = context;
        initPopup();
    }

    /**
     * init
     */
    private void initPopup() {
        this.dialog = new Dialog(context, R.style.TransDialog);
        View rootView = LayoutInflater.from(context).inflate(R.layout.pop_circle_comment_input_layout, null);
        etInutComment = (EditText) rootView.findViewById(R.id.et_input_comment);
        etInutComment.setOnEditorActionListener(this);
        btnSubmitComment = (TextView) rootView.findViewById(R.id.btn_submit_comment);
        btnSubmitComment.setOnClickListener(this);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(rootView);

        dialog.getWindow().setGravity(Gravity.BOTTOM);

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (WindowManager.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.5
        p.width = width; // 宽度设置为屏幕宽
        dialog.getWindow().setAttributes(p);
    }


    /**
     * 设置外部代码钩子
     *
     * @param listener
     */
    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        doSubmit();
    }

    private void doSubmit() {
        if (mListener != null) {
            if (TextUtils.isEmpty(etInutComment.getText())) {
                return;
            }
            mListener.onSubmit(etInutComment.getText().toString());
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEND) {
//            doSubmit();
//            return true;
//        }
        return false;
    }

    public interface Listener {

        /**
         * 点击了评论提交按钮
         *
         * @param commentContent 评论内容
         */
        void onSubmit(String commentContent);

        /**
         * 取消评论
         */
        void onCancel();
    }

    /**
     * 在下方显示
     */
    public void show(View parent) {

        etInutComment.requestFocus();
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod();
            }
        },100);
    }

    private void showInputMethod() {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 消失
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
