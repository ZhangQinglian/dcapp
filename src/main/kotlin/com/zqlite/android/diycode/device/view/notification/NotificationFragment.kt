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

package com.zqlite.android.diycode.device.view.notification

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zqlite.android.dclib.entiry.Notification
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.utils.CalendarUtils
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_notification.*
/**
 * Created by scott on 2017/8/21.
 */
class NotificationFragment :BaseFragment(),NotificationContract.View {


    private var mPresenter : NotificationContract.Presenter?=null

    private val mAdapter : NotificationAdapter = NotificationAdapter()


    override fun onResume() {
        super.onResume()
        if(!TokenStore.shouldLogin(context)){
            mPresenter!!.loadNotification()
        }
    }

    override fun setPresenter(presenter: NotificationContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_notification
    }

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        notification_list.layoutManager = linearLayoutManager
        notification_list.adapter = mAdapter
        notification_fresh.setOnRefreshListener({
            mPresenter!!.loadNotification()
        })
        notification_fresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
    }

    override fun initData() {
        mPresenter!!.loadNotification()
    }

    override fun updateNotification(datas: List<Notification>) {
        notification_fresh.isRefreshing = false
        mAdapter.updateNotification(datas)
    }

    private inner class NotificationAdapter : RecyclerView.Adapter<NotificationItemHolder>(){

        private val notifications = mutableListOf<Notification>()

        fun updateNotification(datas: List<Notification>){
            notifications.clear()
            notifications.addAll(datas)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: NotificationItemHolder?, position: Int) {
            val notification = notifications[position]
            holder!!.bind(notification)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NotificationItemHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.listitem_notification_item,parent,false)
            return NotificationItemHolder(view)
        }

        override fun getItemCount(): Int {
            return notifications.size
        }

    }

    private inner class NotificationItemHolder(val root: View):RecyclerView.ViewHolder(root){
        fun bind(notification:Notification){
            val avatar = root.findViewById<CircleImageView>(R.id.avatar)
            val title = root.findViewById<TextView>(R.id.notification_title)
            val time = root.findViewById<TextView>(R.id.notification_time)
            val content = root.findViewById<TextView>(R.id.notification_content)
            NetworkUtils.getInstace(context)!!.loadImage(avatar,notification.actor.avatarUrl,R.drawable.default_avatar)
            time.text = CalendarUtils().getTimeDes(notification.createdAt)
            //clean
            root.setOnClickListener {  }
            title.text = ""
            content.text = ""

            var name = ""
            if(notification.actor.name == null){
                name = notification.actor.login
            }else{
                name = notification.actor.name
            }

            if(notification.read){
                root.setBackgroundColor(Color.WHITE)
            }else{
                root.setBackgroundColor(resources.getColor(R.color.notification_unread))
            }

            when(notification.type){
                "Follow"->{
                    title.text = spanUserName(name)
                    content.setText(R.string.follow_des)
                    root.setOnClickListener {
                        Route.goUserDetail(activity,notification.actor.login)
                        mPresenter!!.readNotification(notification.id)
                    }
                }
                "Mention"->{
                    title.text = spanMention(name)
                    content.text = Html.fromHtml(notification.mention.bodyHtml).trim()
                    root.setOnClickListener {
                        Route.goTopicDetail(activity,notification.mention.topicId,notification.mention.id)
                        mPresenter!!.readNotification(notification.id)
                    }
                }
                "TopicReply"->{
                    title.text = spanTopicReply(name,notification.reply.topicTitle)
                    content.text = Html.fromHtml(notification.reply.bodyHtml).trim()
                    root.setOnClickListener {
                        Route.goTopicDetail(activity,notification.reply.topicId,notification.reply.id)
                        mPresenter!!.readNotification(notification.id)
                    }
                }
                "Topic"->{
                    content.text = spanTopic(name,notification.topic.title)
                    root.setOnClickListener {
                        Route.goTopicDetail(activity,notification.topic.id)
                        mPresenter!!.readNotification(notification.id)
                    }
                }
            }
            //title.text = Html.fromHtml(notification.)
        }
    }

    companion object Factory{
        fun getInstance(args:Bundle?):NotificationFragment{
            val fragment = NotificationFragment()
            if(args != null){
                fragment.arguments = args
            }
            return fragment
        }
    }

    private fun spanUserName(name:String):Spanned{
        val ssb = SpannableStringBuilder(name)
        val foregroundColor : ForegroundColorSpan = ForegroundColorSpan(context.resources.getColor(R.color.colorPrimary))
        ssb.setSpan(foregroundColor,0,name.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }

    private fun spanMention(name:String):Spanned{
        val text = name + resources.getString(R.string.mention_des)
        val ssb = SpannableStringBuilder(text)
        val foregroundColor : ForegroundColorSpan = ForegroundColorSpan(context.resources.getColor(R.color.colorPrimary))
        ssb.setSpan(foregroundColor,0,name.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }

    private fun spanTopicReply(name:String,title:String):Spanned{
        val text = resources.getString(R.string.reply_des,name,title)
        val ssb = SpannableStringBuilder(text)
        val foregroundColor1  = ForegroundColorSpan(context.resources.getColor(R.color.colorPrimary))
        val foregroundColor2  = ForegroundColorSpan(context.resources.getColor(R.color.colorPrimary))
        ssb.setSpan(foregroundColor1,0,name.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        ssb.setSpan(foregroundColor2,text.indexOf(title),text.indexOf(title) + title.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return ssb
    }

    private fun spanTopic(name:String,title:String):Spanned{
        val text = resources.getString(R.string.new_topic,name,title)
        val ssb = SpannableStringBuilder(text)
        val foregroundColor1  = ForegroundColorSpan(context.resources.getColor(R.color.colorPrimary))
        val foregroundColor2  = ForegroundColorSpan(context.resources.getColor(R.color.colorPrimary))
        ssb.setSpan(foregroundColor1,text.indexOf(name),text.indexOf(name) + name.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        ssb.setSpan(foregroundColor2,text.indexOf(title),text.indexOf(title) + title.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return ssb
    }

}