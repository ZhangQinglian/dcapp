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
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.zqlite.android.ak47.show
import com.zqlite.android.dclib.entiry.UserDetail
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.FragmentDashboardBinding
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.logly.Logly
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * Created by scott on 2017/8/16.
 */
class DashboardFragment : BaseFragment(), DashboardContract.View {


    private var mPresenter: DashboardContract.Presenter? = null

    private var mBinding: FragmentDashboardBinding? = null

    private var mUserDetail: UserDetail? = null
    override fun setPresenter(presenter: DashboardContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun initView() {
        mBinding = FragmentDashboardBinding.bind(view)
        fresh_layout.setOnRefreshListener {
            mPresenter!!.getLocalUser(context)
        }
        fresh_layout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        mBinding!!.avatar.setOnClickListener {
            if (TokenStore.shouldLogin(context)) {
                Route.goLogin(activity)
            }
        }

        my_favorite.setOnClickListener {
            if (TokenStore.shouldLogin(context)) {
                Route.goLogin(activity)
                return@setOnClickListener
            }
            Route.goFavorite(mUserDetail!!.login, activity)
        }

        my_following.setOnClickListener {
            if (TokenStore.shouldLogin(context)) {
                Route.goLogin(activity)
                return@setOnClickListener
            }
            Route.goFollowing(mUserDetail!!.login, activity)
        }

        my_followers.setOnClickListener {
            if (TokenStore.shouldLogin(context)) {
                Route.goLogin(activity)
                return@setOnClickListener
            }
            Route.goFollowers(mUserDetail!!.login, activity)
        }

        my_topic.setOnClickListener {
            if (TokenStore.shouldLogin(context)) {
                Route.goLogin(activity)
                return@setOnClickListener
            }
            Route.goMyTopic(mUserDetail!!.login, activity)
        }
        about.setOnClickListener {
            Route.goAbout(activity)
        }

        exit.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.show {
                setMessage(R.string.logout)
                setPositiveButton(R.string.exit, {
                    p0, _ ->
                    p0.dismiss()
                    mPresenter!!.logout(TokenStore.getAccessToken(context))
                })
                setNegativeButton(R.string.comm_cancel, null)
                setCancelable(true)
            }
        }
    }

    override fun initData() {
        mPresenter!!.getLocalUser(context)
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.getLocalUser(context)
    }

    companion object Factory {

        fun getInstance(args: Bundle?): DashboardFragment {
            val fragment = DashboardFragment()
            if (args != null) {
                fragment.arguments = args
            }
            return fragment
        }
    }

    override fun loadUserSuccess(userDetail: UserDetail) {
        mUserDetail = userDetail
        mBinding!!.apply {
            setVariable(BR.user, userDetail)
            executePendingBindings()
            userName.text = userDetail.name
        }
        fresh_layout.isRefreshing = false
        NetworkUtils.getInstace(context)!!.loadImage(mBinding!!.avatar, userDetail.avatarUrl, R.drawable.default_avatar)
        exit.visibility = View.VISIBLE
    }

    override fun needLogin() {
        fresh_layout.isRefreshing = false
    }

    override fun logoutSuccess() {
        TokenStore.logout(context)
        Toast.makeText(context, R.string.exit_success, Toast.LENGTH_LONG).show()
        clearUI()
    }

    fun clearUI() {
        val empty = UserDetail(-1, "", "", "", "", "", "", "", "", "", "", "", "", 0, 0, 0, 0, 0, "", "")
        mBinding!!.apply {
            setVariable(BR.user, empty)
            executePendingBindings()
            userName.text = resources.getString(R.string.click_avatar_to_login)
        }
        exit.visibility = View.INVISIBLE
    }

}