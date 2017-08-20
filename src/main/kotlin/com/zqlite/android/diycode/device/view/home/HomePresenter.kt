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

package com.zqlite.android.diycode.device.view.home

import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.logly.Logly
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/11.
 */
class HomePresenter constructor(var mView: HomeContract.View) : HomeContract.Presenter {



    init {
        mView.setPresenter(this)
    }

    override fun start() {

    }

    override fun stop() {
    }

    override fun homeClicked() {
        mView.homeClicked()
    }

    override fun updateDevice(token: String) {
        DiyCodeApi.updateDevice(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    Logly.d(it.string())
                },
                {

                }
        )
    }

    override fun testNo() {
        DiyCodeApi.getNotification(0,20).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    for(no in it){
                        Logly.d(no.toString())
                    }
                },
                {
                    Logly.d(it.toString())
                }
        )
    }
}