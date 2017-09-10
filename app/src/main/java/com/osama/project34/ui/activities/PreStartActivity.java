package com.osama.project34.ui.activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.osama.project34.R;
import com.osama.project34.ui.adapters.PagerAdapter;
import com.osama.project34.utils.ConfigManager;

import me.relex.circleindicator.CircleIndicator;

public class PreStartActivity extends AppCompatActivity {
    private RelativeLayout preStartLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if not first run
        if (!ConfigManager.isFirstRun()) {
            startSplash();
            return;
        }
        ConfigManager.voidFirstRun();
        setContentView(R.layout.activity_pre_start);

        preStartLayout = (RelativeLayout) findViewById(R.id.pre_start);
        setColors(ContextCompat.getColor(this, R.color.colorAccent));

        //set ViewPager adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //set pager indicator
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
        changePage(0);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changePage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.pre_start_skip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSkipButtonClick(v);
            }
        });
    }

    public void onSkipButtonClick(View v) {
        startSplash();
    }

    private void startSplash() {
        //start the intent for the password activity
        Intent intent = new Intent(this, SplashActivity.class);
        //start the activity but not let the user get back to this activity
        startActivity(intent);
        finish();
    }

    public void changePage(int num) {
        switch (num) {
            case 0:
                preStartLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentDark));
                setColors(ContextCompat.getColor(this, R.color.colorAccentDark));
                break;
            case 1:
                preStartLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.pagercolor2));
                setColors(ContextCompat.getColor(this, R.color.pagercolor2));
                setColors(ContextCompat.getColor(this, R.color.pagercolor2));
                break;
            case 2:
                preStartLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.pagercolor3));
                setColors(ContextCompat.getColor(this, R.color.pagercolor3));
                setColors(ContextCompat.getColor(this, R.color.pagercolor3));
                animateButtonAndShow();
                break;

        }
    }

    private void animateButtonAndShow() {
        ImageButton button = (ImageButton) findViewById(R.id.pre_start_skip_button);
        button.setVisibility(View.VISIBLE);
        //animate
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.done_button_anim);
        button.startAnimation(animation);
    }

    private void setColors(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        }
    }

}


