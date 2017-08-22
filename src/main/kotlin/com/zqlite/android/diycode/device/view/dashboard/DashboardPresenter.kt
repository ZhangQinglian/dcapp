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

package com.zqlite.android.diycode.device.view.dashboard

import android.content.Context
import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.logly.Logly
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/18.
 */
class DashboardPresenter(val mView: DashboardContract.View) : DashboardContract.Presenter {


    init {
        mView.setPresenter(this)
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun getLocalUser(context: Context) {
        val localLogin = TokenStore.getCurrentLogin(context)
        if (localLogin.isNotEmpty()) {
            DiyCodeApi.loadUserDetail(localLogin).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        mView.loadUserSuccess(it)
                    }, {}
            )
        } else {
            mView.needLogin()
        }
    }

    override fun logout(token: String) {
        DiyCodeApi.deleteDevice(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.logoutSuccess()
                    Logly.d(it.toString())
                },
                {
                    Logly.d(it.toString())
                }
        )
    }
}