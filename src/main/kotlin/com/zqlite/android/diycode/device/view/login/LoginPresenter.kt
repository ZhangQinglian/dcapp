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

package com.zqlite.android.diycode.device.view.login

import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.logly.Logly
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by scott on 2017/8/15.
 */
class LoginPresenter(val mView: LoginContract.View) : LoginContract.Presenter {


    init {
        mView.setPresenter(this)
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun login(loginName: String, password: String) {

        DiyCodeApi.login(loginName, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.updateToken(it,loginName)
                },
                {
                    mView.loginError()
                }
        )

    }
    override fun loadUserDetail(loginName: String) {
        DiyCodeApi.loadUserDetail(loginName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    mView.loadUserDetailSuccess(it)
                },
                {

                }
        )
    }
}