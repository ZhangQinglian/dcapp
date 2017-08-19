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

import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.logly.Logly
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/14.
 */
class UserDetailPresenter(val mView : UserDetailContract.View) : UserDetailContract.Presenter {
    private val LIMIT : Int = 20

    private var currentOffset = 0

    init {
        mView.setPresenter(this)
    }
    override fun start() {
    }

    override fun stop() {
    }

    override fun loadUser(login: String) {
        DiyCodeApi.loadUserDetail(login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mView.updateUser(it)
        }
    }

    override fun getFollowing(login: String) {
        DiyCodeApi.getfollowing(login,0,150).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.updateFollowing(it)
                },
                {
                    Logly.d(it.toString())
                }
        )
    }


    override fun followUser(login: String) {
        DiyCodeApi.followUser(login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.updateFollowStatus(login,true,1)
                },
                {

                }
        )
    }

    override fun unFollowUser(login: String) {
        DiyCodeApi.unfollowUser(login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.updateFollowStatus(login,false,-1)
                },
                {

                }
        )
    }
    override fun getUserTopic(login: String) {
       DiyCodeApi.loadUserTopics(login,currentOffset,LIMIT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
               {
                   mView.updateUserTopics(it)
               },{}
       )
    }
    override fun loadNextPageTopic(login: String) {
        var newOffset = currentOffset + LIMIT
        DiyCodeApi.loadUserTopics(login,newOffset,LIMIT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if(it.isEmpty()){
                        return@subscribe
                    }
                    mView.addUserTopics(it)
                    currentOffset += it.size
                },{}
        )
    }


}