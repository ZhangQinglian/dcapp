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

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.text.Html.ImageGetter
import android.view.View
import android.widget.TextView
import okhttp3.OkHttpClient
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


/**
 * Created by scott on 2017/8/16.
 */
class URLDrawable : BitmapDrawable() {
    // the drawable that you need to set, you could set the initial drawing
    // with the loading image if you need to
    var drawable: Drawable? = null

    override fun draw(canvas: Canvas) {
        // override the draw to facilitate refresh function later
        if (drawable != null) {
            drawable!!.draw(canvas)
        }
    }
}

class URLImageParser(internal var container: View, internal var c: Context) : ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val urlDrawable = URLDrawable()

        // get the actual source
        val asyncTask = ImageGetterAsyncTask(urlDrawable)

        asyncTask.execute(source)

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable
    }

    inner class ImageGetterAsyncTask(internal var urlDrawable: URLDrawable) : AsyncTask<String, Void, Drawable>() {

        override fun doInBackground(vararg params: String): Drawable? {
            val source = params[0]
            return fetchDrawable(source)
        }

        override fun onPostExecute(result: Drawable?) {
            if(result == null) return
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(0, 0, 0 + result.intrinsicWidth, 0 + result.intrinsicHeight)

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result

            // redraw the image by invalidating the container
            val textview = this@URLImageParser.container as TextView
            textview.text = textview.text
        }

        /***
         * Get the Drawable from URL
         * @param urlString
         * *
         * @return
         */
        fun fetchDrawable(urlString: String): Drawable? {
            try {
                val `is` = fetch(urlString)
                val drawable = Drawable.createFromStream(`is`, "src")
                drawable.setBounds(0, 0, 0 + drawable.intrinsicWidth, 0 + drawable.intrinsicHeight)
                return drawable
            } catch (e: Exception) {
                return null
            }

        }

        @Throws(MalformedURLException::class, IOException::class)
        private fun fetch(urlString: String): InputStream {
            val url = URL(urlString)
            val connection = url.openConnection()
            val inputStream = BufferedInputStream(connection.getInputStream())
            return inputStream
        }
    }
}