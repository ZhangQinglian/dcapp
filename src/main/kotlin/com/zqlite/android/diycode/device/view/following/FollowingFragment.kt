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

package com.zqlite.android.diycode.device.view.following

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zqlite.android.dclib.entiry.User
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.view.BaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_following.*
/**
 * Created by scott on 2017/8/19.
 */
class FollowingFragment : BaseFragment(),FollowingContract.View {


    private var mPresenter : FollowingContract.Presenter? = null
    private val mAdapter = FollowingAdapter()

    override fun setPresenter(presenter: FollowingContract.Presenter) {
        mPresenter = presenter
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_following
    }

    override fun initView() {
        val layoutManager = GridLayoutManager(context,4,GridLayoutManager.VERTICAL,false)
        my_following.layoutManager = layoutManager
        my_following.adapter = mAdapter
    }

    override fun initData() {
        val login = arguments["login"] as String
        mPresenter!!.loadFollowing(login)
    }


    override fun loadError() {
    }

    override fun onResume() {
        super.onResume()
        val login = arguments["login"] as String
        mPresenter!!.loadFollowing(login)
    }
    override fun loadFollowingSuccess(users: List<User>) {
        mAdapter.users.clear()
        mAdapter.users.addAll(users)
        mAdapter.notifyDataSetChanged()
    }
    companion object Factory{
        fun getInstance(args:Bundle?):FollowingFragment{
            val fragment = FollowingFragment()
            if(args != null){
                fragment.arguments = args
            }
            return fragment
        }
    }

    private inner class FollowingAdapter : RecyclerView.Adapter<UserItemHolder>(){

        val users  = mutableListOf<User>()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserItemHolder {

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.listitem_following_item,parent,false)
            return UserItemHolder(view)
        }

        override fun onBindViewHolder(holder: UserItemHolder?, position: Int) {
            val user = users[position]
            holder!!.bind(user)
        }

        override fun getItemCount(): Int {
            return users.size
        }

    }

    private inner class UserItemHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(user: User){
            val avatar = itemView.findViewById<CircleImageView>(R.id.avatar)
            NetworkUtils.getInstace(context)!!.loadImage(avatar,user.avatarUrl,R.drawable.default_avatar)
            itemView.findViewById<TextView>(R.id.name).text = user.name
            itemView.setOnClickListener {
                Route.goUserDetail(activity,user.login)
            }
        }
    }
}