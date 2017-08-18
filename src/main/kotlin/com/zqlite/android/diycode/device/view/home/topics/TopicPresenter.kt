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
import com.zqlite.android.diycode.device.view.home.Category
import com.zqlite.android.diycode.device.view.home.HomeContract
import com.zqlite.android.logly.Logly
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.Comparator

/**
 * Created by scott on 2017/8/11.
 */
class TopicPresenter(var mView: TopicContract.View,var mCategoryCallback: HomeContract.CategoryCallback) : TopicContract.Presenter {


    private val LIMIT : Int = 20

    //当前选择的Node的Id
    private var currentNodeId: Int = -1

    //当前选择Node的Id在RecyclerView中的位置。
    private var currentPosition : Int = 0

    //当前Node分类下的offset
    private var currentOffset = 0

    init {
        mView.setPresenter(this)
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun loadTopic(offset: Int, limit: Int,type:String) {
        if(currentNodeId == -1){
            DiyCodeApi.loadTopic(offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                mView.updateTopicList(it)
                mCategoryCallback.justUpdateSelf()
            }
        }else{
            DiyCodeApi.loadTopic(offset,limit,nodeId = currentNodeId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                mView.updateTopicList(it)
                mCategoryCallback.justUpdateSelf()
            }
        }

    }

    override fun loadNodes() {
        DiyCodeApi.loadNodes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            val nodeList : MutableList<Node> = mutableListOf()
            val nodeAll : Node = Node(-1,"所有",-1,"","",-1,"","")
            nodeList.add(nodeAll)
            Collections.sort(it,{n1,n2->
                n2.topicsCount - n1.topicsCount
            })
            nodeList.addAll(it)
            mCategoryCallback.updateCategory(Category.TYPE_TOPIC,nodeList.map { Category(it) })
            loadTopic()
        }
    }

    override fun getCurrentNodeId(): Int {
        return currentNodeId
    }

    override fun setCurrentNode(id: Int,position:Int) {
        currentNodeId = id
        currentPosition = position
        //在选择Node的时候currentOffset需要清零
        currentOffset = 0
        loadTopic(0,LIMIT)
    }
    override fun getCurrentNodePosition(): Int {
        return currentPosition
    }

    override fun loadNextPage() {
        var newOffset = currentOffset + LIMIT
        if(currentNodeId == -1){

            DiyCodeApi.loadTopic(newOffset, limit = LIMIT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                currentOffset += it.size
                mView.addTopicList(it)
            }
        }else{
            DiyCodeApi.loadTopic(newOffset,limit = LIMIT,nodeId = currentNodeId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                currentOffset += it.size
                mView.addTopicList(it)
            }
        }
    }

    override fun goTop() {
        mView.goTop()
    }
}