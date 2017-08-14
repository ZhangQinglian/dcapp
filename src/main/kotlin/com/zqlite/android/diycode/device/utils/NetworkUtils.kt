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

package com.zqlite.android.diycode.device.utils

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import com.zqlite.android.diycode.R
import com.zqlite.android.logly.Logly
import java.util.regex.Pattern

/**
 * Created by scott on 2017/8/11.
 */
class NetworkUtils(context :Context) {

    var picsso : Picasso? = null
    init {
        picsso = Picasso.Builder(context).downloader(OkHttpDownloader(context,1024L * 1024 * 100)).build()
    }

    fun loadImage(imageView: ImageView,url:String,defaultId:Int){
        picsso!!.load(url).error(defaultId).into(imageView)
    }

    fun getReplyClickable(html:String):String{
        var pattern : Pattern = Pattern.compile("<a href=\"/.*?class=\"at_user\"")
        var matcher = pattern.matcher(html)
        if(matcher.find()){
            var s = matcher.group()
            var index = html.indexOf(s)
            var builder : StringBuilder = StringBuilder(html)
            builder.insert(index + 9,"http://localhost")
            return builder.toString()
        }
        return html
    }
     companion object Factory{
        var instance : NetworkUtils?=null

        fun getInstace(context: Context): NetworkUtils?{

            if(instance == null){
                instance = NetworkUtils(context)
            }

            return instance
        }
    }
}