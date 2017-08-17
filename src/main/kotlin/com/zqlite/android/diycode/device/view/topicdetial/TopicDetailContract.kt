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

package com.zqlite.android.diycode.device.view.topicdetial

import com.zqlite.android.dclib.entiry.TopicDetail
import com.zqlite.android.dclib.entiry.TopicReply
import com.zqlite.android.diycode.device.view.imvp.IPresenter
import com.zqlite.android.diycode.device.view.imvp.IView
import java.io.File

/**
 * Created by scott on 2017/8/13.
 */
interface TopicDetailContract {

    interface Presenter : IPresenter{
        fun loadTopicDetail(id:Int)
        fun loadTopicReplies(id:Int)
        fun followTopic(topicId:Int)
        fun unFollowTopic(topicId:Int)
        fun likeTopic(id:Int)
        fun unlikeTopic(id:Int)
        fun favoriteTopic(id:Int)
        fun unFavoriteTopic(id:Int)
        fun reply(id: Int,content:String)
        fun uploadImage(file: File)
    }

    interface View : IView<Presenter>{
        fun updateTopicDetail(topicDetal : TopicDetail)
        fun updateReplies(replies : List<TopicReply>)
        fun updateLikeStatus(topicId:Int,isLike: Boolean)
        fun updateFollowStatus(topicId:Int,isFollow: Boolean)
        fun updateFavoriteStatus(topicId: Int,isFavorite:Boolean)
        fun updateReplySuccess()
        fun uploadSuccess(url:String)
        fun uploadError()
    }
}