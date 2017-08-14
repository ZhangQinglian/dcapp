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

package com.zqlite.android.diycode.device.web

import android.webkit.WebView
import android.webkit.WebViewClient
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.logly.Logly

/**
 * Created by scott on 2017/8/14.
 */
class DcWebViewClient(val callback: Callback) : WebViewClient() {

    interface Callback{
        fun goFloor(floor:Int)
        fun goUser(userLogin:String)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

        if(url!!.contains(NetworkUtils.kLocalHostUser)){
            val userLogin = url.substring(NetworkUtils.kLocalHostUser.length+1)
            callback.goUser(userLogin)
        }
        if(url!!.contains(NetworkUtils.kLocalHostFloorAt)){
            var floorAt = url.substring(NetworkUtils.kLocalHostFloorAt.length+1)
            var n = floorAt.substring(5)
            callback.goFloor(n.toInt())
        }
        return true
    }
}