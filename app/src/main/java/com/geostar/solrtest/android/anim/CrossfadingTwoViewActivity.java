package com.geostar.solrtest.android.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.geostar.solrtest.R;

public class CrossfadingTwoViewActivity extends AppCompatActivity implements View.OnClickListener {

    private int mShortAnimationDuration = 0;

    private View mContentView;
    private View noContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfading_two_view);

        findViewById(R.id.btn_do_action).setOnClickListener(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mContentView = findViewById(R.id.tv_content);
        noContent = findViewById(R.id.ll_no_content);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mContentView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        noContent.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        noContent.setVisibility(View.GONE);
                        ((TextView)mContentView).setTextColor(Color.BLACK);
                    }
                });
    }
}
