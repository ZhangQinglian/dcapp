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

package com.zqlite.android.diycode.device.view.follow

import com.zqlite.android.dclib.entiry.User
import com.zqlite.android.diycode.device.view.imvp.IPresenter
import com.zqlite.android.diycode.device.view.imvp.IView

/**
 * Created by scott on 2017/8/19.
 */
interface FollowContract {

    interface Presenter : IPresenter{
        /**
         * 加载关注或被关注的用户
         * @type
         *    0  关注的用户
         *    1 被关注的用户
         */
        fun loadFollowing(login:String,type:Int)
    }

    interface View : IView<FollowContract.Presenter>{
        fun loadFollowingSuccess(users :List<User>)
        fun loadError()
    }
}