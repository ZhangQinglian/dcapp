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

package com.zqlite.android.diycode.device.view.topicdetial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.css.styles.Github
import com.zqlite.android.dclib.entiry.TopicDetail
import com.zqlite.android.dclib.entiry.TopicReply
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemTopicDetailHeadBinding
import com.zqlite.android.diycode.databinding.ListitemTopicReplyBinding
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.utils.Route
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.diycode.device.view.userdetail.UserDetailActivity
import com.zqlite.android.diycode.device.web.DcWebViewClient
import com.zqlite.android.logly.Logly
import kotlinx.android.synthetic.main.fragment_topic_detail.*

/**
 * Created by scott on 2017/8/13.
 */
class TopicDetailFragment : BaseFragment(), TopicDetailContract.View {


    private var mPresenter: TopicDetailContract.Presenter? = null

    private val mAdapter: TopicDetailAdapter = TopicDetailAdapter()
    override fun setPresenter(presenter: TopicDetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic_detail
    }

    override fun initView() {
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        topic_detail.layoutManager = linearLayoutManager
        topic_detail.adapter = mAdapter
        fresh_layout.isRefreshing = true
        fresh_layout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        fresh_layout.setOnRefreshListener {
            mPresenter!!.loadTopicDetail(-1)
        }
    }

    override fun initData() {
    }

    override fun updateTopicDetail(topicDetal: TopicDetail) {
        mAdapter.updateHead(topicDetal)
        fresh_layout.isRefreshing = false
    }

    override fun updateReplies(replies: List<TopicReply>) {
        var topicRepliesList = replies.map { TopicRepliesItem(it)}
        mAdapter.setItems(topicRepliesList)
    }

    inner class TopicDetailAdapter : RecyclerView.Adapter<TopicDetailHodler>() {

        private var itemList: MutableList<TopicDetailItem> = mutableListOf()

        fun updateHead(topicDetail: TopicDetail) {
            var topicDetailItem: TopicDetailItem = TopicDetailHeadItem(topicDetail)
            itemList.clear()
            itemList.add(topicDetailItem)
            notifyDataSetChanged()
        }

        fun setItems(items: List<TopicDetailItem>) {
            itemList.addAll(items)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: TopicDetailHodler?, position: Int) {
            var topicDetailItem = itemList[position]
            var type = getItemViewType(position)
            when (type) {
                0 -> {
                    var topicDetailHeadItem = topicDetailItem as TopicDetailHeadItem
                    (holder as TopicDetailHeadHolder).bind(topicDetailHeadItem.data)
                }
                1 ->{
                    var topicReplyItem = topicDetailItem as TopicRepliesItem
                    (holder as TopicReplyHolder).bind(topicReplyItem.data)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicDetailHodler? {
            var inflater: LayoutInflater = LayoutInflater.from(context)
            when(viewType){
                0 ->{
                    var binding = ListitemTopicDetailHeadBinding.inflate(inflater, parent, false)
                    return TopicDetailHeadHolder(binding)
                }
                1 ->{
                    var binding = ListitemTopicReplyBinding.inflate(inflater,parent,false)
                    return TopicReplyHolder(binding)
                }
            }
            return null
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun getItemViewType(position: Int): Int {
            when (position) {
                0 -> return 0
                else -> return 1
            }
        }

    }

    abstract class TopicDetailItem

    class TopicDetailHeadItem(val data: TopicDetail) : TopicDetailItem()

    class TopicRepliesItem(val data: TopicReply) : TopicDetailItem()

    inner abstract class TopicDetailHodler(view: View) : RecyclerView.ViewHolder(view)

    inner class TopicDetailHeadHolder(var binding: ListitemTopicDetailHeadBinding) : TopicDetailHodler(binding.root) {
        fun bind(topicDetail: TopicDetail) {
            binding.setVariable(BR.topicDetail, topicDetail)
            binding.executePendingBindings()
            NetworkUtils.instance!!.loadImage(binding.avatar, topicDetail.user.avatarUrl, R.drawable.default_avatar)
            var css = Github()
            css.addRule("body", "padding: 0px")
            binding.markdownView.addStyleSheet(css)
            binding.markdownView.loadMarkdown(topicDetail.getContentWithTitle())
            binding.markdownView.setOnElementListener(object : MarkdownView.OnElementListener{
                        override fun onLinkTap(p0: String?, p1: String?) {
                            Logly.d("onLinkTap")
                        }

                        override fun onButtonTap(p0: String?) {
                            Logly.d("onButtonTap")
                        }

                        override fun onMarkTap(p0: String?) {
                            Logly.d("onMarkTap")
                        }

                        override fun onCodeTap(p0: String?, p1: String?) {
                            Logly.d("onCodeTap")
                        }

                        override fun onHeadingTap(p0: Int, p1: String?) {
                            Logly.d("onHeadingTap")
                        }

                        override fun onImageTap(p0: String?, p1: Int, p2: Int) {
                            Logly.d("onImageTap")
                        }

                        override fun onKeystrokeTap(p0: String?) {
                            Logly.d("onKeystrokeTap")
                        }

                    })
        }
    }

    inner class TopicReplyHolder(var binding:ListitemTopicReplyBinding):TopicDetailHodler(binding.root){
        fun bind(topicReply: TopicReply){
            binding.setVariable(BR.topicReply,topicReply)
            binding.executePendingBindings()
            NetworkUtils.instance!!.loadImage(binding.avatar, topicReply.user.avatarUrl, R.drawable.default_avatar)
            binding.floorAt.text = getString(R.string.floor_at,adapterPosition)
            var css = Github()
            css.addRule("body", "padding: 0px")
            binding.markdownView.addStyleSheet(css)
            binding.markdownView.loadData(NetworkUtils.instance!!.getReplyClickable(topicReply.bodyHtml),"text/html; charset=UTF-8",null)
            binding.markdownView.webViewClient = DcWebViewClient(object :DcWebViewClient.Callback{
                override fun openBrowser(url: String) {
                    val intent :Intent = Intent(Intent.ACTION_VIEW)
                    val uri : Uri = Uri.parse(url)
                    intent.data = uri
                    startActivity(intent)
                }

                override fun goTopic(id: Int) {
                    Route.goTopicDetail(activity,id)
                }

                override fun goFloor(floor: Int) {
                    topic_detail.smoothScrollToPosition(floor)
                }

                override fun goUser(userLogin: String) {
                    Route.goUserDetail(activity,userLogin)
                }
            })
        }
    }

    companion object Factory {
        fun getInstance(args: Bundle?): TopicDetailFragment {
            var fragment: TopicDetailFragment = TopicDetailFragment()
            if (args != null) {
                fragment.arguments = args
            }
            return fragment
        }
    }
}