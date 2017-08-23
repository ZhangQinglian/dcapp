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

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zqlite.android.dclib.entiry.Node
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemTopicNodeItemBinding
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.diycode.device.view.home.topics.TopicContract
import com.zqlite.android.diycode.device.view.home.topics.TopicFragment
import com.zqlite.android.diycode.device.view.home.topics.TopicPresenter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_topic.*

/**
 * Created by scott on 2017/8/11.
 */
class HomeFragment : BaseFragment(),HomeContract.View,HomeContract.CategoryCallback {


    private var mPresetner : HomeContract.Presenter? = null

    private var mTabLayout : TabLayout? = null

    private var mViewPager : ViewPager ? = null

    private var topicFragment : TopicFragment ?=null
    private var topicPresenter : TopicContract.Presenter ?=null

    private var mCurrentType : Int = -1

    private var mAdapter : CategoryAdapter = CategoryAdapter()

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
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        category.layoutManager = linearLayoutManager
        category.adapter = mAdapter
    }

    override fun initData() {
        if(!TokenStore.shouldLogin(context)){
            mPresetner!!.updateDevice(TokenStore.getAccessToken(context))
        }
    }

    override fun updateCategory(type: Int, categories: List<Category>) {
        mCurrentType = type
        mAdapter.updateCategoies(categories)
    }

    override fun justUpdateSelf() {
       mAdapter.notifyDataSetChanged()
    }

    override fun homeClicked() {
        val tabPositon = home_pager.currentItem
        when(tabPositon){
            0->{
                topicPresenter!!.goTop()
            }
        }
    }
    fun getCurrentCategoryId(): Int{
        return topicPresenter!!.getCurrentNodeId()
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

    inner class HomePageAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){

        init {
            topicFragment = TopicFragment.getInstance(null)
            topicPresenter = TopicPresenter(topicFragment!!,this@HomeFragment)
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

    inner class CategoryAdapter : RecyclerView.Adapter<CategoryItemHolder>(){

        val mCategories: MutableList<Category> = mutableListOf()

        fun updateCategoies(categoies : List<Category>){
            mCategories.clear()
            mCategories.addAll(categoies)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return mCategories.size
        }

        override fun onBindViewHolder(holder: CategoryItemHolder?, position: Int) {
            var category = mCategories[position]
            holder!!.bind(mCurrentType,category)

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryItemHolder {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            val binding = ListitemTopicNodeItemBinding.inflate(inflater,parent,false)
            return CategoryItemHolder(binding)
        }

    }

    inner class CategoryItemHolder(val binding : ListitemTopicNodeItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(type:Int,category:Category){
            if(type == Category.TYPE_TOPIC){
                val node : Node = category.data as Node
                binding.setVariable(BR.node,node)
                binding.executePendingBindings()
                if(node.id == getCurrentCategoryId()){
                    binding.nodeText.background = resources.getDrawable(R.drawable.topic_node_bg_select)
                    binding.nodeText.setTextColor(resources.getColor(R.color.colorPrimary))
                }else{
                    binding.nodeText.background = resources.getDrawable(R.drawable.topic_node_bg_normal)
                    binding.nodeText.setTextColor(Color.WHITE)
                }
                binding.root.setOnClickListener {
                    var p : Int = adapterPosition
                    topic_fresh_layout.isRefreshing = true
                    topicPresenter!!.setCurrentNode(node.id,p)
                }
            }
        }
    }
}