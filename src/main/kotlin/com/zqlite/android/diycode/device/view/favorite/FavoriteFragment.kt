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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemTopicBinding
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_favorite.*
/**
 * Created by scott on 2017/8/19.
 */
class FavoriteFragment : BaseFragment(),FavoriteContract.View {

    private var mPresenter:FavoriteContract.Presenter? = null

    private val mAdapter = FavoriteAdapter()

    private var mLogin : String? = null

    private var mType = -1
    override fun setPresenter(presenter: FavoriteContract.Presenter) {
        mPresenter = presenter
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite
    }

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        favorite_topics.layoutManager = linearLayoutManager
        favorite_topics.adapter = mAdapter
    }

    override fun initData() {
        mLogin = arguments.getString("login")
        mType = arguments["type"] as Int
        mPresenter!!.loadTopic(mLogin!!,mType)
    }

    override fun loadFavoriteSuccess(topics: List<Topic>) {
        mAdapter.updateTopics(topics)
    }

    override fun loadFavoriteError() {
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.loadTopic(mLogin!!,mType)
    }

    private inner class FavoriteAdapter : RecyclerView.Adapter<TopicItemHolder>(){

        val topics = mutableListOf<Topic>()

        fun updateTopics(datas:List<Topic>){
            topics.clear()
            topics.addAll(datas)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: TopicItemHolder?, position: Int) {
            val topic = topics[position]
            holder!!.bind(topic)
            if(position == topics.size - 1){
                mPresenter!!.loadNext(mLogin!!,mType)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicItemHolder {
            val inflater = LayoutInflater.from(context)
            val binding = ListitemTopicBinding.inflate(inflater,parent,false)
            return TopicItemHolder(binding)
        }

        override fun getItemCount(): Int {
            return topics.size
        }

    }

    private inner class TopicItemHolder(val binding:ListitemTopicBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(topic: Topic){
            binding.setVariable(BR.topic, topic)
            binding.executePendingBindings()
            NetworkUtils.getInstace(context)!!.loadImage(binding.avatar, topic.user.avatarUrl, R.drawable.default_avatar)
            binding.root.setOnClickListener {
                Route.goTopicDetail(activity, topic.id)
            }
        }

    }

    companion object Factory{

        fun getInstance(args:Bundle?):FavoriteFragment{
            val fragment = FavoriteFragment()
            if(args != null){
                fragment.arguments = args
            }
            return fragment
        }
    }
}