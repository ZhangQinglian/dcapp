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

package com.zqlite.android.diycode.device.view.home.topics

import com.zqlite.android.dclib.entiry.Node
import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.diycode.device.view.imvp.IPresenter
import com.zqlite.android.diycode.device.view.imvp.IView

/**
 * Created by scott on 2017/8/11.
 */
interface TopicContract {

    interface Presenter : IPresenter{

        fun loadTopic(offset:Int = 0,limit:Int = 150,type:String,nodeId:Int)

        fun loadNodes()

        fun getCurrentNodeId():Int

        fun getCurrentNodePosition():Int

        fun setCurrentNode(id:Int,position:Int)
    }

    interface View : IView<Presenter>{
        fun updateTopicList(topicList:List<Topic>)
        fun nodesOk(nodes : List<Node>)
    }
}