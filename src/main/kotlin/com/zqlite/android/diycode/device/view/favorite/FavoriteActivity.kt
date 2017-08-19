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

import android.os.Bundle
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.BaseActivity
import com.zqlite.android.diycode.device.view.custom.DecorViewProxy
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by scott on 2017/8/19.
 */
class FavoriteActivity : BaseActivity() {

    private var mFragment : FavoriteFragment ? = null
    private var mPresenter : FavoriteContract.Presenter ? = null

    override fun getLayoutId(): Int {
        return R.layout.favorite_activity
    }

    override fun initView() {
        //action bar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.my_favorite)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener({
            finish()
        })

        //slide
        DecorViewProxy().bind(this)

        val login = intent.extras["login"] as String
        val args = Bundle()
        args.putString("login",login)
        mFragment = FavoriteFragment.getInstance(args)
        mPresenter = FavoritePresenter(mFragment!!)
        addFragment(mFragment!!,R.id.favorite_container)
    }

    override fun initData() {
    }
}