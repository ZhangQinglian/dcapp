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

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * Created by scott on 2017/8/4.
 */

class DecorViewProxy {
    private var mDecorView: ViewGroup? = null

    private var activity: Activity? = null

    private var slideView: SlideView? = null

    fun bind(original: Activity): DecorViewProxy {
        activity = original
        mDecorView = activity!!.window.decorView as ViewGroup
        slideView = SlideView(original)

        val child = mDecorView!!.getChildAt(0)
        val vglp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slideView!!.layoutParams = child.layoutParams
        child.layoutParams = vglp
        mDecorView!!.removeViewAt(0)
        slideView!!.addView(child)
        mDecorView!!.addView(slideView)

        slideView!!.init(activity!!)
        return this
    }

    fun enableSlideActivity() {
        slideView!!.enable()
    }

    fun disableSlideActivity() {
        slideView!!.disable()
    }
}
