package com.geostar.solrtest.android.anim;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.geostar.solrtest.R;

/**
 * View 动画设置示例
 */
public class AnimTestActivity extends AppCompatActivity {

    ImageView imageView;
    LinearLayout rootLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_test);
        imageView = (ImageView) findViewById(R.id.imageView);
        rootLinearLayout = (LinearLayout) findViewById(R.id.ll_root_layout);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.startAnimation(createHideAnimation());
//
                } else {
                    imageView.startAnimation(createShowAnimation());

                }

            }
        });

//        Animation layoutAnimation = AnimationUtils.makeInAnimation(AnimTestActivity.this,true);
//        AnimationSet animationSet ;
//        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(layoutAnimation);
//        rootLinearLayout.startLayoutAnimation();
//        rootLinearLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//            }
//        });

        rootLinearLayout.setLayoutTransition(createLayoutTransition());
    }

    private LayoutTransition createLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
//        Animator animator = null;
//        animator = ObjectAnimator.ofInt(imageView.getLayoutParams(),
//                "height", imageView.getHeight(), 0);
//        animator.setTarget(imageView);
//        layoutTransition.setAnimator(LayoutTransition.APPEARING, animator);
        return layoutTransition;

    }

    private Animation createHideAnimation() {
        Animation animation = AnimationUtils.loadAnimation(AnimTestActivity.this, R.anim.anim_01);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
//                boolean show = imageView.getVisibility() == View.VISIBLE?false:true;
            }
        });
        return animation;
    }

    private Animation createShowAnimation() {
        Animation animation = AnimationUtils.loadAnimation(AnimTestActivity.this, R.anim.anim_02);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
//                boolean show = imageView.getVisibility() == View.VISIBLE?false:true;
//                imageView.setVisibility(View.VISIBLE);
            }
        });
        return animation;
    }
}
