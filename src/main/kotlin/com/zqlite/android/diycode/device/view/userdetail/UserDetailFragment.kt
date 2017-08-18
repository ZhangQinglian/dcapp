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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.dclib.entiry.User
import com.zqlite.android.dclib.entiry.UserDetail
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemTopicBinding
import com.zqlite.android.diycode.databinding.ListitemUserDetailHeadBinding
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.diycode.device.view.home.topics.TopicFragment
import kotlinx.android.synthetic.main.fragment_user_detail.*

/**
 * Created by scott on 2017/8/14.
 */
class UserDetailFragment : BaseFragment(), UserDetailContract.View {



    private var mPresenter: UserDetailContract.Presenter? = null

    private val mAdapter : UserDetailAdapter = UserDetailAdapter()

    override fun setPresenter(presenter: UserDetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_detail
    }

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        user_detail_list.layoutManager = linearLayoutManager
        user_detail_list.adapter = mAdapter
    }

    override fun initData() {
    }

    override fun updateUser(user: UserDetail) {
        mAdapter.addUserDetail(user)
        if(!TokenStore.shouldLogin(context)){
            mPresenter!!.getFollowing(TokenStore.getCurrentLogin(context))
            mPresenter!!.getUserTopic(user.login)
        }
    }

    override fun updateUserTopics(topics: List<Topic>) {
        mAdapter.updateUserTopic(topics)
    }
    override fun addUserTopics(topics: List<Topic>) {
        mAdapter.addUserTopics(topics)
    }
    override fun updateFollowing(followingUser: List<User>) {
        val userDetailData = mAdapter.getUserDetailData(0)
        if(userDetailData.type == UserDetailData.TYPE_HEAD){
            val userDetail : UserDetail = userDetailData.data as UserDetail
            for (user in followingUser){
                if(user.login == userDetail.login){
                    updateFollowStatus(user.login,true,0)
                    return
                }
            }
            updateFollowStatus(userDetail.login,false,0)
        }
    }
    override fun updateFollowStatus(login: String, isFollow: Boolean,change:Int) {
        val view = user_detail_list.getChildAt(0)
        val textview = view.findViewById<TextView>(R.id.follow)
        if(isFollow){
            textview.setText(R.string.followed)
            updateFollowCount(change)
        }else{
            textview.setText(R.string.follow)
            updateFollowCount(change)
        }
        textview.setOnClickListener {
            follow(login,!isFollow)
            textview.isClickable = false
        }
        textview.isClickable = true
    }

    private fun updateFollowCount(detal: Int){
        val userDetailData = mAdapter.getUserDetailData(0)
        if(userDetailData.type == UserDetailData.TYPE_HEAD){
            val userDetail : UserDetail = userDetailData.data as UserDetail
            userDetail.followersCount = userDetail.followersCount + detal
            val view = user_detail_list.getChildAt(0)
            val textView = view.findViewById<TextView>(R.id.followers)
            textView.text = userDetail.getFollowCountDes()
        }
    }
    private fun follow(login: String,isFollow: Boolean){
        if(TokenStore.shouldLogin(context)){
            Route.goLogin(activity)
            return
        }
        if(isFollow){
            mPresenter!!.followUser(login)
        }else{
            mPresenter!!.unFollowUser(login)
        }
    }

    companion object Factory {

        fun getInstance(args: Bundle?): UserDetailFragment {
            val fragment = UserDetailFragment()
            if (args != null) {
                fragment.arguments = args
            }
            return fragment
        }

    }

    inner class UserDetailAdapter : RecyclerView.Adapter<UserDetailItemHolder>() {

        private val userDetails = mutableListOf<UserDetailData>()

        fun updateUserTopic(topics: List<Topic>){
            val head = userDetails.filter {
                it.type == UserDetailData.TYPE_HEAD
            }

            userDetails.clear()
            userDetails.addAll(head)
            userDetails.addAll(topics.map {
                UserDetailData(it,UserDetailData.TYPE_REPLY)
            })
            notifyItemRangeChanged(1,topics.size)
        }
        fun addUserTopics(topics: List<Topic>){
            val topicsList = topics.map {
                UserDetailData(it,UserDetailData.TYPE_REPLY)
            }
            userDetails.addAll(topicsList)
            notifyItemRangeChanged(userDetails.size - topics.size - 1,topics.size)
        }

        override fun onBindViewHolder(holder: UserDetailItemHolder?, position: Int) {
            var data : UserDetailData = userDetails.get(position)
            val type = getItemViewType(position)
            when(type){
                UserDetailData.TYPE_HEAD->{
                    val userDetail : UserDetail = data.data as UserDetail
                    (holder as UserDetailHeadHolder).bind(userDetail)
                }
                UserDetailData.TYPE_REPLY->{
                    val topic : Topic = data.data as Topic
                    (holder as UserTopicHolder).bind(topic)
                    if(position == userDetails.size - 1){
                        mPresenter!!.loadNextPageTopic(topic.user.login)
                    }
                }
            }


        }

        override fun getItemCount(): Int {
            return userDetails.size
        }

        fun addUserDetail(userDetail: UserDetail){
            userDetails.clear()
            userDetails.add(UserDetailData(userDetail,UserDetailData.TYPE_HEAD))
            notifyDataSetChanged()
        }

        fun getUserDetailData(index:Int):UserDetailData{
            return userDetails[index]
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserDetailItemHolder? {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            when(viewType){
                UserDetailData.TYPE_HEAD->{
                    val binding : ListitemUserDetailHeadBinding = ListitemUserDetailHeadBinding.inflate(inflater,parent,false)
                    return UserDetailHeadHolder(binding)
                }
                UserDetailData.TYPE_REPLY->{
                    val binding : ListitemTopicBinding = ListitemTopicBinding.inflate(inflater,parent,false)
                    return UserTopicHolder(binding)
                }
                else-> return null
            }
        }

        override fun getItemViewType(position: Int): Int {
            when (position) {
                0 -> return UserDetailData.TYPE_HEAD
                else -> return UserDetailData.TYPE_REPLY
            }
        }

    }

    inner class UserDetailHeadHolder(var binding: ListitemUserDetailHeadBinding) : UserDetailItemHolder(binding.root) {
        fun bind(userDetail: UserDetail) {
            binding.setVariable(BR.userDetail,userDetail)
            binding.follow.setOnClickListener { Route.goLogin(activity) }
            NetworkUtils.getInstace(context)!!.loadImage(binding.userAvatar,userDetail.avatarUrl,R.drawable.default_avatar)
        }
    }

    inner abstract class UserDetailItemHolder(view: View) : RecyclerView.ViewHolder(view)

    private inner class UserTopicHolder(var binding: ListitemTopicBinding) : UserDetailItemHolder(binding.root) {
        fun bind(topic: Topic) {
            binding.setVariable(BR.topic, topic)
            binding.executePendingBindings()
            NetworkUtils.getInstace(context)!!.loadImage(binding.avatar, topic.user.avatarUrl, R.drawable.default_avatar)
            binding.root.setOnClickListener {
                Route.goTopicDetail(activity, topic.id)
            }
        }
    }

    class UserDetailData(val data:Any,val type:Int){

        companion object Constant{
            val TYPE_HEAD:Int = 0
            val TYPE_REPLY:Int = 1
        }
    }
}