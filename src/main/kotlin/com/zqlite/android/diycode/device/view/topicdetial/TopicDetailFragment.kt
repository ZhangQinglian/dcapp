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

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
import com.zqlite.android.diycode.device.view.custom.URLImageParser
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
        var topicRepliesList = replies.map { TopicRepliesItem(it) }
        mAdapter.setItems(topicRepliesList)
    }

    override fun updateLikeStatus(topicId: Int, isLike: Boolean) {
        val head = topic_detail.getChildAt(0)
        val likeImage = head.findViewById<ImageView>(R.id.like)

        if(isLike){
            Toast.makeText(context,R.string.like_success,Toast.LENGTH_SHORT).show()
            likeImage.setImageResource(R.drawable.ic_topic_like_ok)
        }else{
            Toast.makeText(context,R.string.unlike_success,Toast.LENGTH_SHORT).show()
            likeImage.setImageResource(R.drawable.ic_like_normal)
        }
        val likeCount = head.findViewById<TextView>(R.id.like_count)
        val like = likeCount.text.toString().toInt()
        if(isLike){
            likeCount.text = (like + 1).toString()
        }else{
            likeCount.text = (like -1 ).toString()
        }
        likeImage.setOnClickListener {
            if(isLike){
                mPresenter!!.unlikeTopic(topicId)
            }else{
                mPresenter!!.likeTopic(topicId)
            }
            likeImage.isClickable = false
        }
        likeImage.isClickable = true
    }

    override fun updateFollowStatus(topicId: Int, isFollow: Boolean) {
        val head = topic_detail.getChildAt(0)
        val followImage = head.findViewById<ImageView>(R.id.follow)
        if(isFollow){
            Toast.makeText(context,R.string.follow_success,Toast.LENGTH_SHORT).show()
            followImage.setImageResource(R.drawable.ic_topic_follow_ok)
        }else{
            Toast.makeText(context,R.string.unfollow_success,Toast.LENGTH_SHORT).show()
            followImage.setImageResource(R.drawable.ic_topic_follow_normal)
        }
        followImage.setOnClickListener {
            if(isFollow){
                mPresenter!!.unFollowTopic(topicId)
            }else{
                mPresenter!!.followTopic(topicId)
            }
            followImage.isClickable = false
        }
        followImage.isClickable = true
    }

    override fun updateFavoriteStatus(topicId: Int, isFavorite: Boolean) {
        val head = topic_detail.getChildAt(0)
        val favoriteImage = head.findViewById<ImageView>(R.id.favorite)
        if(isFavorite){
            Toast.makeText(context,R.string.favorite_success,Toast.LENGTH_SHORT).show()
            favoriteImage.setImageResource(R.drawable.ic_topic_favorite_ok)
        }else{
            Toast.makeText(context,R.string.unfavorite_success,Toast.LENGTH_SHORT).show()
            favoriteImage.setImageResource(R.drawable.ic_topic_favorite_normal)
        }
        favoriteImage.setOnClickListener {
            if(isFavorite){
                mPresenter!!.unFavoriteTopic(topicId)
            }else{
                mPresenter!!.favoriteTopic(topicId)
            }
            favoriteImage.isClickable = false
        }
        favoriteImage.isClickable = true
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
                1 -> {
                    var topicReplyItem = topicDetailItem as TopicRepliesItem
                    (holder as TopicReplyHolder).bind(topicReplyItem.data)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicDetailHodler? {
            var inflater: LayoutInflater = LayoutInflater.from(context)
            when (viewType) {
                0 -> {
                    var binding = ListitemTopicDetailHeadBinding.inflate(inflater, parent, false)
                    return TopicDetailHeadHolder(binding)
                }
                1 -> {
                    var binding = ListitemTopicReplyBinding.inflate(inflater, parent, false)
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
        init {
            val css = Github()
            css.addRule("body", "padding: 0px")

            binding.markdownView.addStyleSheet(css)
            binding.markdownView.setOnElementListener(object : MarkdownView.OnElementListener {
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

        fun bind(topicDetail: TopicDetail) {
            binding.avatar.setOnClickListener {
                Route.goUserDetail(activity,topicDetail.user.login)
            }
            if(topicDetail.liked){
                binding.like.setImageResource(R.drawable.ic_topic_like_ok)
                binding.like.setOnClickListener {
                    mPresenter!!.unlikeTopic(topicDetail.id)
                    binding.like.isClickable = false
                }
            }else{
                binding.like.setImageResource(R.drawable.ic_like_normal)
                binding.like.setOnClickListener {
                    mPresenter!!.likeTopic(topicDetail.id)
                    binding.like.isClickable = false
                }
            }

            if(topicDetail.followed){
                binding.follow.setImageResource(R.drawable.ic_topic_follow_ok)
                binding.follow.setOnClickListener {
                    mPresenter!!.unFollowTopic(topicDetail.id)
                    binding.follow.isClickable = false
                }
            }else{
                binding.follow.setImageResource(R.drawable.ic_topic_follow_normal)
                binding.follow.setOnClickListener {
                    mPresenter!!.followTopic(topicDetail.id)
                    binding.follow.isClickable = false
                }
            }

            if(topicDetail.favorited){
                binding.favorite.setImageResource(R.drawable.ic_topic_favorite_ok)
                binding.favorite.setOnClickListener {
                    binding.favorite.isClickable = false
                    mPresenter!!.unFavoriteTopic(topicDetail.id)
                }
            }else{
                binding.favorite.setImageResource(R.drawable.ic_topic_favorite_normal)
                binding.favorite.setOnClickListener {
                    binding.favorite.isClickable = false
                    mPresenter!!.favoriteTopic(topicDetail.id)
                }
            }

            binding.setVariable(BR.topicDetail, topicDetail)
            binding.executePendingBindings()

            NetworkUtils.instance!!.loadImage(binding.avatar, topicDetail.user.avatarUrl, R.drawable.default_avatar)
            binding.markdownView.loadMarkdown(topicDetail.getContentWithTitle())

        }
    }

    inner class TopicReplyHolder(var binding: ListitemTopicReplyBinding) : TopicDetailHodler(binding.root) {

        fun bind(topicReply: TopicReply) {
            binding.setVariable(BR.topicReply, topicReply)
            binding.executePendingBindings()
            NetworkUtils.instance!!.loadImage(binding.avatar, topicReply.user.avatarUrl, R.drawable.default_avatar)
            binding.avatar.setOnClickListener {
                Route.goUserDetail(activity,topicReply.user.login)
            }
            binding.floorAt.text = getString(R.string.floor_at, adapterPosition)
            binding.markdownView.linksClickable = true
            binding.markdownView.movementMethod = LinkMovementMethod.getInstance()
            binding.markdownView.text = addSpann(Html.fromHtml(topicReply.bodyHtml,URLImageParser(binding.markdownView,context),null))
            binding.root.setOnClickListener {
                Logly.d("touch")
            }
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

    private fun addSpann(spanned: Spanned): Spanned {
        val spanBuilder = SpannableStringBuilder(spanned)
        val urlSpans = spanBuilder.getSpans(0, spanBuilder.length, URLSpan::class.java)
        for (span in urlSpans) {
            val start = spanBuilder.getSpanStart(span)
            val end = spanBuilder.getSpanEnd(span)
            val flags = spanBuilder.getSpanFlags(span)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View?) {
                    Logly.d("  on click " + span.url)
                    route(span.url)
                }

            }
            spanBuilder.setSpan(clickableSpan, start, end, flags)
            spanBuilder.removeSpan(span)
        }
        return spanBuilder
    }

    private fun route(url: String) {
        if (url.startsWith("#reply")) {
            val floor = url.substring(6).toInt()
            topic_detail.smoothScrollToPosition(floor)
            return
        }
        if (url.startsWith("/")) {
            val login = url.substring(1)
            Route.goUserDetail(activity, login)
            return
        }
        if (url.startsWith("https://www.diycode.cc/topics/")) {
            val topicId = url.substring("https://www.diycode.cc/topics/".length).toInt()
            Route.goTopicDetail(activity, topicId)
            return
        }
        Route.openBrowser(activity,url)

    }

}