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

package com.zqlite.android.diycode.device.view

import android.support.design.widget.BottomNavigationView
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.dashboard.DashboardFragment
import com.zqlite.android.diycode.device.view.dashboard.DashboardPresenter
import com.zqlite.android.diycode.device.view.home.HomeContract
import com.zqlite.android.diycode.device.view.home.HomeFragment
import com.zqlite.android.diycode.device.view.home.HomePresenter
import com.zqlite.android.diycode.device.view.notification.NotificationFragment
import com.zqlite.android.diycode.device.view.notification.NotificationPresenter
import com.zqlite.android.logly.Logly
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var mHomeFragment : HomeFragment ? = null
    private var mHomePresenter : HomeContract.Presenter? = null

    private var mDashboradFramgnet : DashboardFragment? = null
    private var mDashboardPresenter : DashboardPresenter? = null

    private var mNotificationFragment : NotificationFragment ? = null
    private var mNotificationPresenter : NotificationPresenter ? = null
    /**
     * 0 home
     * 1 notification
     * 2 dashboard
     */
    private var mCurrentPage = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if(mCurrentPage == 0){
                    mHomePresenter!!.homeClicked()
                    return@OnNavigationItemSelectedListener true
                }
                showFragment(mHomeFragment!!)
                hideFragment(mDashboradFramgnet!!)
                hideFragment(mNotificationFragment!!)
                mCurrentPage = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                showFragment(mDashboradFramgnet!!)
                hideFragment(mHomeFragment!!)
                hideFragment(mNotificationFragment!!)
                mCurrentPage = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                mCurrentPage = 1
                showFragment(mNotificationFragment!!)
                hideFragment(mDashboradFramgnet!!)
                hideFragment(mHomeFragment!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    override fun initView() {
        //底部导航栏
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        //主页面
        if(mHomeFragment == null){
            mHomeFragment = HomeFragment.getInstance(null)
            mHomePresenter = HomePresenter(mHomeFragment!!)
            addFragment(mHomeFragment!!,R.id.home_container)
        }

        //dashboard
        if(mDashboradFramgnet == null){
            mDashboradFramgnet = DashboardFragment.getInstance(null)
            mDashboardPresenter = DashboardPresenter(mDashboradFramgnet!!)
            addFragment(mDashboradFramgnet!!,R.id.home_container)
        }

        //notification
        if(mNotificationFragment == null){
            mNotificationFragment = NotificationFragment.getInstance(null)
            mNotificationPresenter = NotificationPresenter(mNotificationFragment!!)
            addFragment(mNotificationFragment!!,R.id.home_container)
        }
        showFragment(mHomeFragment!!)
        hideFragment(mDashboradFramgnet!!)
        hideFragment(mNotificationFragment!!)
        //init logly
        Logly.setGlobalTag(Logly.Tag(Logly.FLAG_THREAD_NAME,"scott",Logly.VERBOSE))
    }

    override fun initData() {
        //todo
    }
}


