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

import com.zqlite.android.dclib.DiyCodeApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/19.
 */
class FavoritePresenter(val mView: FavoriteContract.View) : FavoriteContract.Presenter {


    init {
        mView.setPresenter(this)
    }

    private var mCurrentOffset = 0

    private val LIMIT = 20

    override fun start() {
    }

    override fun stop() {
    }

    override fun loadFavorite(login: String) {
        DiyCodeApi.getFavoriteTopics(login, 0, LIMIT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.loadFavoriteSuccess(it)
                },
                {
                    mView.loadFavoriteError()
                }
        )
    }

    override fun loadNext(login: String) {
        val offset = mCurrentOffset + LIMIT
        DiyCodeApi.getFavoriteTopics(login, offset, LIMIT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    if(it.isEmpty()){
                        return@subscribe
                    }
                    mView.loadFavoriteSuccess(it)
                    mCurrentOffset+=it.size
                },
                {
                    mView.loadFavoriteError()
                }
        )
    }

}