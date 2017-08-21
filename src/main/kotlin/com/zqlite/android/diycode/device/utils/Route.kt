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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.custom.ImageViewerActivity
import com.zqlite.android.diycode.device.view.favorite.FavoriteActivity
import com.zqlite.android.diycode.device.view.follow.FollowAvtivity
import com.zqlite.android.diycode.device.view.login.LoginActivity
import com.zqlite.android.diycode.device.view.topicdetial.TopicDetailActivity
import com.zqlite.android.diycode.device.view.userdetail.UserDetailActivity

/**
 * Created by scott on 2017/8/14.
 */
object Route {

    val PICK_IMAGE_REQUEST_CODE = 100

    fun goUserDetail(activity:Activity,loginName:String){
        val intent : Intent = Intent(activity,UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.kUserLoiginKey,loginName)
        activity.startActivity(intent)
    }

    fun goTopicDetail(activity: Activity,topicId:Int,topicReplyId:Int = -1){

        val intent : Intent = Intent(activity, TopicDetailActivity::class.java)
        intent.putExtra("topicId",topicId)
        if(topicReplyId != -1){
            intent.putExtra("replyId",topicReplyId)
        }
        activity.startActivity(intent)
    }

    fun goLogin(activity: Activity){
        val intent : Intent = Intent(activity,LoginActivity::class.java)
        activity.startActivity(intent)
    }

    fun openBrowser(activity: Activity,urlStr :String){

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlStr)
        activity.startActivity(intent)
    }

    fun openImageView(activity: Activity,urlStr: String){
        val intent : Intent = Intent(activity,ImageViewerActivity::class.java)
        intent.putExtra("url",urlStr)
        activity.startActivity(intent)
    }

    fun pickImage(fragment: Fragment){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    fun goFavorite(loginName: String,activity: Activity){
        val intent = Intent(activity,FavoriteActivity::class.java)
        intent.putExtra("login",loginName)
        intent.putExtra("type",0)
        activity.startActivity(intent)
    }
    fun goMyTopic(loginName: String,activity: Activity){
        val intent = Intent(activity,FavoriteActivity::class.java)
        intent.putExtra("login",loginName)
        intent.putExtra("type",1)
        activity.startActivity(intent)
    }

    fun goFollowing(loginName: String,activity: Activity){
        val intent = Intent(activity, FollowAvtivity::class.java)
        intent.putExtra("login",loginName)
        intent.putExtra("type",0)
        activity.startActivity(intent)
    }
    fun goFollowers(loginName: String,activity: Activity){
        val intent = Intent(activity, FollowAvtivity::class.java)
        intent.putExtra("login",loginName)
        intent.putExtra("type",1)
        activity.startActivity(intent)
    }

}