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
import android.content.SharedPreferences
import com.zqlite.android.dclib.entiry.Token

/**
 * Created by scott on 2017/8/15.
 */
object TokenStore {

    val TOKEN = "tokenStore"

    val ACCESS_TOKEN_KEY = "access_token_key"

    val TOKEN_TYPE_KEY = "token_type_key"

    val EXPIRESIN = "expiresin_key"

    val FRESH_TOKEN_KEY = "fresh_token_key"

    val CREATE_AT = "createat_key"

    fun saveToken(context: Context, token: Token) {
        val edit = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit()
        edit.putString(ACCESS_TOKEN_KEY,token.accessToken)
        edit.putString(TOKEN_TYPE_KEY,token.tokenType)
        edit.putString(EXPIRESIN,token.expiresIn)
        edit.putString(FRESH_TOKEN_KEY,token.refreshToken)
        edit.putString(CREATE_AT,token.createdAt)
        edit.apply()
    }

    fun getAccessToken(context: Context) : String {
        return context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).getString(ACCESS_TOKEN_KEY, "")
    }

    fun shouldLogin(context:Context):Boolean{
        val accessToken = context.getSharedPreferences(TOKEN,Context.MODE_PRIVATE).getString(ACCESS_TOKEN_KEY,"")
        return accessToken.isEmpty()
    }
}