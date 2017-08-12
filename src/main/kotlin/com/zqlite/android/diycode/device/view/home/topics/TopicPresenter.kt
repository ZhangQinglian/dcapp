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

import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.dclib.entiry.Node
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/11.
 */
class TopicPresenter(var mView: TopicContract.View) : TopicContract.Presenter {


    private var currentId : Int = -1

    private var currentPosition : Int = 0
    init {
        mView.setPresenter(this)
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun loadTopic(offset: Int, limit: Int,type:String,nodeId : Int) {
        if(nodeId == -1){
            DiyCodeApi.loadTop(offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                mView.updateTopicList(it)
            }
        }else{
            DiyCodeApi.loadTop(offset,limit,nodeId = nodeId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                mView.updateTopicList(it)
            }
        }

    }

    override fun loadNodes() {
        DiyCodeApi.loadNodes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            val nodeList : MutableList<Node> = mutableListOf()
            val nodeAll : Node = Node(-1,"所有",-1,"","",-1,"","")
            nodeList.add(nodeAll)
            nodeList.addAll(it)
            mView.nodesOk(nodeList)
        }
    }

    override fun getCurrentNodeId(): Int {
        return currentId
    }

    override fun setCurrentNode(id: Int,position:Int) {
        currentId = id
        currentPosition = position
        loadTopic(0,150,"",nodeId = id)
    }
    override fun getCurrentNodePosition(): Int {
        return currentPosition
    }
}