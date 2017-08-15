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
import com.zqlite.android.dclib.entiry.UserDetail
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemUserDetailHeadBinding
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
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
        mAdapter.updateUserDetail(user)
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

        override fun onBindViewHolder(holder: UserDetailItemHolder?, position: Int) {
            var data : UserDetailData = userDetails.get(position)
            val type = getItemViewType(position)
            when(type){
                0->{
                    val userDetail : UserDetail = data.data as UserDetail
                    (holder as UserDetailHeadHolder).bind(userDetail)
                }
            }
        }

        override fun getItemCount(): Int {
            return userDetails.size
        }

        fun updateUserDetail(userDetail: UserDetail){
            userDetails.clear()
            userDetails.add(UserDetailData(userDetail,UserDetailData.TYPE_HEAD))
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserDetailItemHolder? {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            when(viewType){
                0->{
                    val binding : ListitemUserDetailHeadBinding = ListitemUserDetailHeadBinding.inflate(inflater,parent,false)
                    return UserDetailHeadHolder(binding)
                }else-> return null
            }
        }

        override fun getItemViewType(position: Int): Int {
            when (position) {
                0 -> return 0
                else -> return 1
            }
        }

    }

    inner class UserDetailHeadHolder(var binding: ListitemUserDetailHeadBinding) : UserDetailItemHolder(binding.root) {
        fun bind(userDetail: UserDetail) {
            binding.setVariable(BR.userDetail,userDetail)
            NetworkUtils.getInstace(context)!!.loadImage(binding.userAvatar,userDetail.avatarUrl,R.drawable.default_avatar)
            binding.follow.setOnClickListener{
                if(TokenStore.getAccessToken(context).isEmpty()){
                    Route.goLogin(activity)
                }
            }
        }
    }

    inner abstract class UserDetailItemHolder(view: View) : RecyclerView.ViewHolder(view)

    class UserDetailData(val data:Any,val type:Int){

        companion object Constant{
            val TYPE_HEAD:Int = 0
            val TYPE_NORMAL:Int = 1
        }
    }
}