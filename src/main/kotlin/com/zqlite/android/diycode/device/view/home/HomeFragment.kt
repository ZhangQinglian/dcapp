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

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.zqlite.android.dclib.DiyCodeApi
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.diycode.device.view.home.topics.TopicContract
import com.zqlite.android.diycode.device.view.home.topics.TopicFragment
import com.zqlite.android.diycode.device.view.home.topics.TopicPresenter
import kotlinx.android.synthetic.main.fragment_home.*
/**
 * Created by scott on 2017/8/11.
 */
class HomeFragment : BaseFragment(),HomeContract.View {

    private var mPresetner : HomeContract.Presenter? = null

    private var mTabLayout : TabLayout? = null

    private var mViewPager : ViewPager ? = null

    override fun setPresenter(presenter: HomeContract.Presenter) {
        mPresetner = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        home_pager.adapter = HomePageAdapter(activity.supportFragmentManager)
        home_tab.setupWithViewPager(home_pager)
        home_tab.getTabAt(0)!!.text = "社区"
    }

    override fun initData() {

    }

    companion object Factory{
        fun getInstance(bundle :Bundle?) : HomeFragment{
            var hf = HomeFragment()
            if(bundle != null){
                hf.arguments = bundle
            }
            return hf
        }
    }

    class HomePageAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){
        var topicFragment : TopicFragment ?=null
        var topicPresenter : TopicContract.Presenter ?=null
        init {
            topicFragment = TopicFragment.getInstance(null)
            topicPresenter = TopicPresenter(topicFragment!!)
        }

        override fun getItem(position: Int): Fragment {
            when(position){
                0-> return topicFragment as Fragment

            }
            return TopicFragment.getInstance(null)
        }

        override fun getCount(): Int {
            return 1
        }

    }
}