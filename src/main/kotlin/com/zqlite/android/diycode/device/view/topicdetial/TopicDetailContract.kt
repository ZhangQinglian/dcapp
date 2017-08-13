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

/**
 * Created by scott on 2017/8/13.
 */
interface TopicDetailContract {

    interface Presenter : IPresenter{
        fun loadTopicDetail(id:Int)
        fun loadTopicReplies(id:Int)
    }

    interface View : IView<Presenter>{
        fun updateTopicDetail(topicDetal : TopicDetail)
        fun updateReplies(replies : List<TopicReply>)
    }
}