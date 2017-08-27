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
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Created by scott on 2017/8/9.
 */

object FileUtils {
    fun getFilePathByUri(context: Context, uri: Uri?): String? {
        if(uri == null) return null
        var filePath = "unknown"//default fileName
        var filePathUri: Uri = uri
        try {
            if (filePathUri.scheme.compareTo("content") == 0) {
                if (Build.VERSION.SDK_INT == 22 || Build.VERSION.SDK_INT == 23) {
                    try {
                        val pathUri = uri.path
                        val newUri = pathUri.substring(pathUri.indexOf("content"),
                                pathUri.lastIndexOf("/ACTUAL"))
                        filePathUri = Uri.parse(newUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val cursor = context.contentResolver
                            .query(filePathUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                filePath = cursor.getString(column_index)
                            }
                            cursor.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                filePathUri = Uri.parse(cursor.getString(column_index))
                                filePath = filePathUri.path
                            }
                        } catch (e: Exception) {
                            cursor.close()
                        }

                    }
                }
            } else if (uri.scheme.compareTo("file") == 0) {
                filePath = filePathUri.path
            } else {
                filePath = filePathUri.path
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return filePath
    }
}
