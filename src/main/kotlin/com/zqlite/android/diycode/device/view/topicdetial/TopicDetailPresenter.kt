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

import com.zqlite.android.dclib.DiyCodeApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/13.
 */
class TopicDetailPresenter(val mView: TopicDetailContract.View) : TopicDetailContract.Presenter {

    var mId : Int? = null
    init {
        mView.setPresenter(this)
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun loadTopicDetail(id: Int) {
        if(id != -1){
            mId = id
        }
        DiyCodeApi.loadTopicDetail(mId!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mView.updateTopicDetail(it)
            loadTopicReplies(mId!!)
        }
    }

    override fun loadTopicReplies(id: Int) {
        DiyCodeApi.loadTopicReplies(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mView.updateReplies(it)
        }
    }

}