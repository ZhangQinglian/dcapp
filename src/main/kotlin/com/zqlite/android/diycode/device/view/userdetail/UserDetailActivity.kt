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

import com.zqlite.android.dclib.entiry.UserDetail
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.view.BaseActivity
import com.zqlite.android.diycode.device.view.custom.DecorViewProxy
import kotlinx.android.synthetic.main.activity_user_detail.*

/**
 * Created by scott on 2017/8/14.
 */
class UserDetailActivity:BaseActivity() ,UserDetailFragment.Callback{

    private var mPresenter:UserDetailContract.Presenter? = null

    private var mFragment:UserDetailFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_user_detail
    }

    override fun initView() {
        //action bar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.user_detail)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener({
            finish()
        })

        //slide
        DecorViewProxy().bind(this)

        //add fragment
        mFragment = UserDetailFragment.getInstance(null)
        mPresenter = UserDetailPresenter(mFragment!!)
        addFragment(mFragment!!,R.id.user_detail_container)
    }

    override fun initData() {
        val extra = intent.extras
        if(extra != null){
            val login :String= extra[kUserLoiginKey] as String
            supportActionBar!!.title=login
            mPresenter!!.loadUser(login)
        }
    }

    companion object {
        val kUserLoiginKey = "login"
    }

    override fun userUpdate(userDetail: UserDetail) {
        NetworkUtils.getInstace(this)!!.loadImage(avatar,userDetail.avatarUrl,R.drawable.default_avatar)
    }

}