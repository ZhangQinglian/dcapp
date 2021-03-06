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

package com.zqlite.android.diycode.device.view.userdetail

import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.dclib.entiry.User
import com.zqlite.android.dclib.entiry.UserDetail
import com.zqlite.android.diycode.device.view.imvp.IPresenter
import com.zqlite.android.diycode.device.view.imvp.IView

/**
 * Created by scott on 2017/8/14.
 */
interface UserDetailContract {

    interface Presenter : IPresenter{
        fun loadUser(login:String)
        fun followUser(login: String)
        fun unFollowUser(login: String)
        fun getFollowing(login: String)
        fun getUserTopic(login: String)
        fun loadNextPageTopic(login:String)
    }

    interface View : IView<UserDetailContract.Presenter>{
        fun updateUser(user :UserDetail)
        fun updateFollowStatus(login: String,isFollow:Boolean,change :Int)
        fun updateFollowing(followingUser:List<User>)
        fun updateUserTopics(topics:List<Topic>)
        fun addUserTopics(topics: List<Topic>)
    }
}