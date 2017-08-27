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
import com.zqlite.android.ak47.getSPString
import com.zqlite.android.ak47.save
import com.zqlite.android.ak47.set
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

    val CURRENT_LOGIN = "current_login"

    val CURRENT_AVATAR_URL = "current_avatar"

    fun saveToken(context: Context, token: Token) {
        context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).save {
            set(ACCESS_TOKEN_KEY to token.accessToken)
            set(TOKEN_TYPE_KEY to token.tokenType)
            set(EXPIRESIN to token.expiresIn)
            set(FRESH_TOKEN_KEY to token.refreshToken)
            set(CREATE_AT to  token.createdAt)
        }
    }

    fun saveCurrentLogin(context: Context, login: String) {
        context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).save {
            set(CURRENT_LOGIN to login)
        }
    }

    fun getCurrentLogin(context: Context): String {
        return context.getSPString(TOKEN, CURRENT_LOGIN,"")
    }

    fun saveCurrentAvatarUrl(context: Context,avatarUrl:String){
        context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).save {
            set(CURRENT_AVATAR_URL to avatarUrl)
        }
    }

    fun getCurrentAvatarUrl(context: Context): String {
        return context.getSPString(TOKEN, CURRENT_AVATAR_URL,"")
    }

    fun getAccessToken(context: Context): String {
        return context.getSPString(TOKEN, ACCESS_TOKEN_KEY,"")
    }

    fun shouldLogin(context: Context): Boolean {
        val accessToken = context.getSPString(TOKEN, ACCESS_TOKEN_KEY,"")
        return accessToken.isEmpty()
    }

    fun logout(context: Context){
        context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).save {
            set(ACCESS_TOKEN_KEY to "")
            set(TOKEN_TYPE_KEY to  "")
            set(EXPIRESIN to  "")
            set(FRESH_TOKEN_KEY to  "")
            set(CREATE_AT to "")
            set(CURRENT_LOGIN to "")
            set(CURRENT_AVATAR_URL to "")
        }
    }
}