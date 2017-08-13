/*
 *    Copyright 2017 zhangqinglian
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zqlite.android.diycode.device.view.custom;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.zqlite.android.logly.Logly;

/**
 * Created by scott on 2017/8/4.
 */

public class SlideView extends FrameLayout {
    private float originalTouchX = 0, originalTouchY = 0;

    private float originWindowX, originWindowY;

    private int screenWidth, screenHeight;

    private boolean action;

    private Activity activity;

    private boolean enable = true;

    private GestureDetector gestureDetector;
    public SlideView(@NonNull Context context) {
        super(context);
    }

    public SlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        this.activity = activity;
        gestureDetector = new GestureDetector(activity,new MyGesture());
    }
    public void enable(){
        enable = true;
    }

    public void disable(){
        enable = false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!enable) return false;
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            if(ev.getX() < 30){
                return onTouchEvent(ev);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!enable) return false;
        gestureDetector.onTouchEvent(event);
        View child = getChildAt(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                originalTouchX = event.getX();
                originalTouchY = event.getY();
                originWindowX = child.getX();
                originWindowY = child.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (originalTouchX < 30) {
                    float deltaX = event.getX() - originalTouchX;
                    float deltaY = event.getY() - originalTouchY;
                    float currentX = originWindowX + deltaX;
                    if(currentX >=0){
                        child.setX(originWindowX + deltaX);
                    }
                    //mDecorView.setY(originWindowY + deltaY);
                    action = true;
                } else {
                    action = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (action) {
                    if (child.getX() < screenWidth / 2) {
                        resetDecorView();

                    } else {
                        removeDecorView(0);
                    }
                }

                break;
        }
        return true;
    }

    private void resetDecorView() {
        getChildAt(0).animate().translationX(0).setDuration((long) getChildAt(0).getX() / 2).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                originalTouchX = originalTouchY = originWindowX = originWindowY = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void removeDecorView(long duration) {
        if(duration == 0){
            duration = (long) ((screenWidth - getChildAt(0).getX())/2);
        }
        getChildAt(0).animate().translationX(screenWidth).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activity.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private class MyGesture extends GestureDetector.SimpleOnGestureListener{


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Logly.d("    ____________   fling : " + " x = " + velocityX + "   y = " + velocityY);
            if(velocityX > 5000){
                removeDecorView(0);
                return true;
            }else {
                resetDecorView();
                return true;
            }
        }
    }
}
