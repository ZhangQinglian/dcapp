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

import android.os.Bundle
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.BaseFragment

/**
 * Created by scott on 2017/8/16.
 */
class DashboardFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun initView() {
    }

    override fun initData() {
    }

    companion object Factory{

        fun getInstance(args : Bundle?):DashboardFragment{
            val fragment = DashboardFragment()
            if(args != null){
                fragment.arguments = args
            }
            return fragment
        }
    }
}