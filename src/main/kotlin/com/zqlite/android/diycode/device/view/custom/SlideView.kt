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

package com.zqlite.android.diycode.device.view.custom

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

import com.zqlite.android.logly.Logly

/**
 * Created by scott on 2017/8/4.
 */

class SlideView : FrameLayout {
    private var originalTouchX = 0f
    private var originalTouchY = 0f

    private var originWindowX: Float = 0.toFloat()
    private var originWindowY: Float = 0.toFloat()

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var action: Boolean = false

    private var activity: Activity? = null

    private var enable = true

    private var gestureDetector: GestureDetector? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun init(activity: Activity) {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        this.activity = activity
        gestureDetector = GestureDetector(activity, MyGesture())
    }

    fun enable() {
        enable = true
    }

    fun disable() {
        enable = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!enable) return false
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (ev.x < 30) {
                return onTouchEvent(ev)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!enable) return false
        gestureDetector!!.onTouchEvent(event)
        val child = getChildAt(0)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                originalTouchX = event.x
                originalTouchY = event.y
                originWindowX = child.x
                originWindowY = child.y
            }
            MotionEvent.ACTION_MOVE -> if (originalTouchX < 30) {
                val deltaX = event.x - originalTouchX
                val deltaY = event.y - originalTouchY
                val currentX = originWindowX + deltaX
                if (currentX >= 0) {
                    child.x = originWindowX + deltaX
                }
                //mDecorView.setY(originWindowY + deltaY);
                action = true
            } else {
                action = false
            }
            MotionEvent.ACTION_UP -> if (action) {
                if (child.x < screenWidth / 2) {
                    resetDecorView()

                } else {
                    removeDecorView(0)
                }
            }
        }
        return true
    }

    private fun resetDecorView() {
        getChildAt(0).animate().translationX(0f).setDuration(getChildAt(0).x.toLong() / 2).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                originWindowY = 0f
                originWindowX = originWindowY
                originalTouchY = originWindowX
                originalTouchX = originalTouchY
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }).start()
    }

    private fun removeDecorView(duration: Long) {
        var d = duration
        if (d == 0L) {
            d = ((screenWidth - getChildAt(0).x) / 2).toLong()
        }
        getChildAt(0).animate().translationX(screenWidth.toFloat()).setDuration(d).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                activity!!.finish()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        }).start()
    }

    private inner class MyGesture : GestureDetector.SimpleOnGestureListener() {


        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (velocityX > 5000) {
                removeDecorView(0)
                return true
            } else {
                resetDecorView()
                return true
            }
        }
    }
}
