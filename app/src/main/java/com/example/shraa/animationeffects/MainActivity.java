package com.example.shraa.animationeffects;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    ProgressBar progress_bar;
    TextView text;
    FrameLayout button;
    View reveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress_bar = findViewById(R.id.progress_bar);
        text = findViewById(R.id.text);
        button = findViewById(R.id.button);
        reveal = findViewById(R.id.reveal);

    }
//
//    public void clickOn(View view) {
//    }

    public void load(View view) {

        animateButtonWidth();

        fadeOutTextAndShowProgressDialog();

        nextAction();
    }

    private void nextAction() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                revealButton();

                fadeOutProgressDialog();

                delayedStartNextActivity();
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void revealButton() {
        button.setElevation(0f);
        reveal.setVisibility(VISIBLE);
        int cx = reveal.getWidth();
        int cy = reveal.getHeight();

        int x = (int) (getFabWidth() / 2 + button.getX());
        int y = (int) (getFabWidth() / 2 + button.getY());
        float finalRadius = Math.max(cx, cy) * 1.2f;
        final Animator revealNew = ViewAnimationUtils
                .createCircularReveal(reveal, x, y, getFabWidth(), finalRadius);

        revealNew.setDuration(350);
        revealNew.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                reset(animation);
//                finish();
            }

            private void reset(Animator animation) {
                super.onAnimationEnd(animation);
                reveal.setVisibility(INVISIBLE);
                text.setVisibility(VISIBLE);
                text.setAlpha(1f);
                button.setElevation(4f);
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.width = (int) (getResources().getDisplayMetrics().density * 300);
               button.requestLayout();
            }
        });

        revealNew.start();
        
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    private void fadeOutTextAndShowProgressDialog() {

        text.animate().alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showProgressDialog();
                    }
                })
                .start();

    }

    private void showProgressDialog() {

        progress_bar.setAlpha(1f);
        progress_bar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        progress_bar.setVisibility(VISIBLE);
    }

    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(button.getMeasuredWidth(), getFabWidth());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.width = val;
                button.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void fadeOutProgressDialog() {
        progress_bar.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        }).start();
    }
    private void delayedStartNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));

            }
        }, 100);
    }


}
