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

package com.zqlite.android.diycode.device.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

/**
 * Created by scott on 2017/8/9.
 */

public class FileUtils {
    public static String getFilePathByUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath = "unknown";//default fileName
        Uri filePathUri = uri;
        try {
            if (uri.getScheme().compareTo("content") == 0) {
                if (Build.VERSION.SDK_INT == 22 || Build.VERSION.SDK_INT == 23) {
                    try {
                        String pathUri = uri.getPath();
                        String newUri = pathUri.substring(pathUri.indexOf("content"),
                                pathUri.lastIndexOf("/ACTUAL"));
                        uri = Uri.parse(newUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Cursor cursor = context.getContentResolver()
                            .query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                filePath = cursor.getString(column_index);
                            }
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Cursor cursor = context.getContentResolver().
                            query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                filePathUri = Uri.parse(cursor.getString(column_index));
                                filePath = filePathUri.getPath();
                            }
                        } catch (Exception e) {
                            cursor.close();
                        }
                    }
                }
            } else if (uri.getScheme().compareTo("file") == 0) {
                filePath = filePathUri.getPath();
            } else {
                filePath = filePathUri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return filePath;
    }
}
