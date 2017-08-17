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

import android.os.Build
import android.support.v4.app.ActivityCompat
import android.transition.Fade
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.view.BaseActivity
import kotlinx.android.synthetic.main.activity_iamge_viewer.*
/**
 * Created by scott on 2017/8/17.
 */
class ImageViewerActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_iamge_viewer
    }

    override fun initView() {
        snappy_image_viewer.addOnClosedListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.sharedElementReturnTransition = Fade(Fade.MODE_IN)
            }
            ActivityCompat.finishAfterTransition(this@ImageViewerActivity)
        }
    }

    override fun initData() {
        val extra = intent.extras
        if(extra != null){
            val url = extra.getString("url")
            NetworkUtils.getInstace(this@ImageViewerActivity)!!.loadImage(snappy_image_viewer.imageView,url,R.drawable.error)
        }
    }
}