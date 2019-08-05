package com.example.chenzeyuan.zhihu1.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.chenzeyuan.zhihu1.R;

public class SplashActivity extends AppCompatActivity {
    private View mIvBackground;
    private AlphaAnimation mAlphaAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        // 初始化动画
        initAnimation();
        initListener();
    }
    private void initListener() {
        mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }
            @Override
            public void onAnimationEnd(Animation arg0) {

                Intent intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initAnimation() {
        mAlphaAnimation = new AlphaAnimation(0f, 1f);
        mAlphaAnimation.setDuration(1500);
        mAlphaAnimation.setFillAfter(true);
        //animationSet.addAnimation(alphaAnimation);

        mIvBackground.setAnimation(mAlphaAnimation);
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        mIvBackground = findViewById(R.id.iv_spash_backgroud);

    }
}
