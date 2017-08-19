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

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.dclib.sevice.DiyCodeContract
import com.zqlite.android.logly.Logly
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.File

/**
 * Created by scott on 2017/8/13.
 */
class TopicDetailPresenter(val mView: TopicDetailContract.View) : TopicDetailContract.Presenter {


    var mId: Int? = null

    var mIsStart = false

    init {
        mView.setPresenter(this)
    }

    override fun start() {
        mIsStart = true
    }

    override fun stop() {
        mIsStart = false
    }

    override fun loadTopicDetail(id: Int) {
        if (id != -1) {
            mId = id
        }
        DiyCodeApi.loadTopicDetail(mId!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateTopicDetail(it)
                        loadTopicReplies(mId!!)
                    }
                },
                {

                }

        )
    }

    override fun loadTopicReplies(id: Int) {
        if (id != -1) {
            mId = id
        }
        DiyCodeApi.loadTopicReplies(mId!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateReplies(it)
                    }
                },
                {}
        )
    }

    override fun followTopic(topicId: Int) {
        DiyCodeApi.followTopic(topicId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateFollowStatus(topicId, true)
                    }
                },
                {


                }
        )
    }

    override fun unFollowTopic(topicId: Int) {
        DiyCodeApi.unfollowTopic(topicId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateFollowStatus(topicId, false)
                    }
                },
                {

                }
        )
    }


    override fun likeTopic(id: Int) {
        DiyCodeApi.like(id, type = DiyCodeContract.LikeType.TYPE_TOPIC).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateLikeStatus(id, true)
                    }
                },
                {}
        )
    }


    override fun unlikeTopic(id: Int) {
        DiyCodeApi.unlike(id, type = DiyCodeContract.LikeType.TYPE_TOPIC).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateLikeStatus(id, false)
                    }
                },
                {}
        )
    }

    override fun favoriteTopic(id: Int) {
        DiyCodeApi.favoriteTopic(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateFavoriteStatus(id, true)
                    }
                },
                {}
        )
    }

    override fun unFavoriteTopic(id: Int) {
        DiyCodeApi.unFavoriteTopic(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateFavoriteStatus(id, false)
                    }
                },
                {}
        )
    }

    override fun reply(id: Int, content: String) {
        DiyCodeApi.replyTopic(id, content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        mView.updateReplySuccess()
                    }
                }, {}
        )
    }

    override fun uploadImage(file: File) {
        DiyCodeApi.uploadPhoto(file).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if (mIsStart) {
                        val jsonObject = JSONObject(it.string())
                        val url = jsonObject.optString("image_url", "")
                        if (url.isNotEmpty()) {
                            mView.uploadSuccess(url)
                        } else {
                            mView.uploadError()
                        }
                    }


                },
                {
                    if (mIsStart) {
                        mView.uploadError()
                    }
                }
        )
    }
}