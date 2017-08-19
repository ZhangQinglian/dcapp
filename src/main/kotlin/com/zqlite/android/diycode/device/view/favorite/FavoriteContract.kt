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

package com.zqlite.android.diycode.device.view.favorite

import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.diycode.device.view.imvp.IPresenter
import com.zqlite.android.diycode.device.view.imvp.IView

/**
 * Created by scott on 2017/8/19.
 */
interface FavoriteContract {

    interface Presenter:IPresenter{
        /**
         * @type
         *     0 收藏的话题
         *     1 发表的话题
         */
        fun loadTopic(login:String,type:Int)
        fun loadNext(login: String,type:Int)
    }

    interface View :IView<FavoriteContract.Presenter>{
        fun loadFavoriteSuccess(topics:List<Topic>)
        fun loadFavoriteError()
    }
}