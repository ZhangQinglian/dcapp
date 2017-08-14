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

import android.os.Bundle
import com.zqlite.android.dclib.entiry.UserDetail
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.BaseFragment

/**
 * Created by scott on 2017/8/14.
 */
class UserDetailFragment : BaseFragment(),UserDetailContract.View {

    private var mPresenter : UserDetailContract.Presenter ? = null

    interface Callback{
        fun userUpdate(userDetail: UserDetail)
    }

    override fun setPresenter(presenter: UserDetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_detail
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun updateUser(user: UserDetail) {
        (activity as UserDetailFragment.Callback).userUpdate(user)
    }

    companion object Factory{

        fun getInstance(args:Bundle?):UserDetailFragment{
            val fragment = UserDetailFragment()
            if(args!= null){
                fragment.arguments = args
            }
            return fragment
        }

    }
}