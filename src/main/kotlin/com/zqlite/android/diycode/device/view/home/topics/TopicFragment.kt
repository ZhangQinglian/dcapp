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

package com.zqlite.android.diycode.device.view.home.topics

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zqlite.android.dclib.entiry.Node
import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemTopicBinding
import com.zqlite.android.diycode.databinding.ListitemTopicNodeItemBinding
import com.zqlite.android.diycode.device.utils.CalendarUtils
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.diycode.device.view.topicdetial.TopicDetailActivity
import com.zqlite.android.logly.Logly
import kotlinx.android.synthetic.main.fragment_topic.*
import kotlinx.android.synthetic.main.fragment_topic_detail.*

/**
 * Created by scott on 2017/8/11.
 */
class TopicFragment : BaseFragment(), TopicContract.View {

    private var mPresenter: TopicContract.Presenter? = null

    private val mAdapter: TopicAdapter = TopicAdapter()

    override fun onStart() {
        super.onStart()
        mPresenter!!.start()
    }

    override fun onStop() {
        super.onStop()
        mPresenter!!.stop()
    }

    override fun setPresenter(presenter: TopicContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

    override fun initView() {
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        topic_list.layoutManager = linearLayoutManager
        topic_list.adapter = mAdapter

        topic_fresh_layout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        topic_fresh_layout.setOnRefreshListener({
            mPresenter!!.loadTopic()
        })
        topic_fresh_layout.isRefreshing = true

        mPresenter!!.loadNodes()
    }

    override fun initData() {

    }


    override fun updateTopicList(topicList: List<Topic>) {
        if (topic_fresh_layout.isRefreshing) {
            topic_fresh_layout.isRefreshing = false
        }
        mAdapter.updateList(topicList)
    }

    override fun addTopicList(topicList: List<Topic>) {
        mAdapter.addTopicList(topicList)
    }


    companion object Factory {
        fun getInstance(bundle: Bundle?): TopicFragment {
            var tf = TopicFragment()
            if (bundle != null) {
                tf.arguments = bundle
            }
            return tf
        }
    }

    private inner class TopicAdapter : RecyclerView.Adapter<TopicHolder>() {

        private var topicList: MutableList<Topic> = mutableListOf()

        fun updateList(data: List<Topic>) {
            topicList.clear()
            topicList.addAll(data)
            notifyDataSetChanged()
            topic_list.scrollToPosition(0)
        }

        fun addTopicList(data: List<Topic>) {
            topicList.addAll(data)
            notifyItemRangeChanged(topicList.size - 1 - data.size, data.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicHolder {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            val listItemTopicBinding: ListitemTopicBinding = ListitemTopicBinding.inflate(inflater, parent, false)
            return TopicItemHolder(listItemTopicBinding)

        }

        override fun onBindViewHolder(itemHolder: TopicHolder?, position: Int) {
            val topic = topicList[position]
            (itemHolder as TopicItemHolder).bind(topic)
            if (position == topicList.size - 1) {
                mPresenter!!.loadNextPage()
            }
        }

        override fun getItemCount(): Int {
            return topicList.size
        }

    }

    private inner abstract class TopicHolder(view: View) : RecyclerView.ViewHolder(view)

    private inner class TopicItemHolder(var binding: ListitemTopicBinding) : TopicHolder(binding.root) {
        fun bind(topic: Topic) {
            binding.setVariable(BR.topic, topic)
            binding.executePendingBindings()
            NetworkUtils.getInstace(context)!!.loadImage(binding.avatar, topic.user.avatarUrl, R.drawable.default_avatar)
            binding.root.setOnClickListener {
                Route.goTopicDetail(activity, topic.id)
            }
            binding.avatar.setOnClickListener {
                Route.goUserDetail(activity, topic.user.login)
            }
        }
    }

}