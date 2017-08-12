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

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zqlite.android.dclib.entiry.Node
import com.zqlite.android.dclib.entiry.Topic
import com.zqlite.android.diycode.BR
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.databinding.ListitemTopicBinding
import com.zqlite.android.diycode.databinding.ListitemTopicNodeItemBinding
import com.zqlite.android.diycode.device.utils.NetworkUtils
import com.zqlite.android.diycode.device.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_topic.*
/**
 * Created by scott on 2017/8/11.
 */
class TopicFragment : BaseFragment(),TopicContract.View {


    private var mPresenter : TopicContract.Presenter?=null

    private val mAdapter : TopicAdapter = TopicAdapter()

    private var mNodeAdapter : NodeAdapter? = null

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
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        topic_list.layoutManager = linearLayoutManager
        topic_list.adapter = mAdapter

        topic_fresh_layout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        topic_fresh_layout.setOnRefreshListener({
            mPresenter!!.loadTopic(type = "",nodeId = mPresenter!!.getCurrentNodeId())
        })
        topic_fresh_layout.isRefreshing = true

        mPresenter!!.loadNodes()
    }

    override fun initData() {

    }

    override fun nodesOk(nodes: List<Node>) {
        mNodeAdapter = NodeAdapter(nodes)
        if(mNodeAdapter!!.itemCount > 0){
            mPresenter!!.loadTopic(type = "",nodeId = mPresenter!!.getCurrentNodeId())
        }
    }
    override fun updateTopicList(topicList: List<Topic>) {
        if(topic_fresh_layout.isRefreshing){
            topic_fresh_layout.isRefreshing = false
        }
        mAdapter.updateList(topicList)
    }


    companion object Factory{
        fun getInstance(bundle : Bundle?) : TopicFragment{
            var tf = TopicFragment()
            if(bundle != null){
                tf.arguments = bundle
            }
            return tf
        }
    }

    private inner class TopicAdapter : RecyclerView.Adapter<TopicHolder>(){

        private var topicList : MutableList<Topic> = mutableListOf()

        fun updateList(data:List<Topic>){
            topicList.clear()
            //加一个虚假Topic用于顶部Node列表占位
            topicList.add(Topic.getMockTopic())
            topicList.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicHolder {
            if(viewType == 0){
                val inflater : LayoutInflater = LayoutInflater.from(context)
                val listItemTopicBinding : ListitemTopicBinding = ListitemTopicBinding.inflate(inflater,parent,false)
                return TopicItemHolder(listItemTopicBinding)
            }else{
                val inflater : LayoutInflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.listitem_topic_node,parent,false)
                val recyclerView :RecyclerView = view as RecyclerView
                val linearLayoutManager : StaggeredGridLayoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
                recyclerView.layoutManager = linearLayoutManager
                return TopicNodeHolder(view)
            }

        }

        override fun onBindViewHolder(itemHolder: TopicHolder?, position: Int) {
            if(getItemViewType(position) == 0){
                val topic = topicList[position]
                (itemHolder as TopicItemHolder).bind(topic)
            }else{
                (itemHolder as TopicNodeHolder).setAdapter(mNodeAdapter!!)
                mNodeAdapter!!.notifyDataSetChanged()
                itemHolder.go(mPresenter!!.getCurrentNodePosition())
            }

        }

        override fun getItemCount(): Int {
            return topicList.size
        }

        override fun getItemViewType(position: Int): Int {
            if(position >0) return 0
            return 1
        }

    }

    private inner class NodeAdapter(val nodes:List<Node>) :RecyclerView.Adapter<NodeItemHolder>(){

        override fun getItemCount(): Int {
            return nodes.size
        }

        override fun onBindViewHolder(holder: NodeItemHolder?, position: Int) {
            holder!!.bind(nodes[position])

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NodeItemHolder {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            val binding : ListitemTopicNodeItemBinding = ListitemTopicNodeItemBinding.inflate(inflater,parent,false)
            return NodeItemHolder(binding)
        }

        fun getNodeAt(index :Int) : Node{
            return nodes[index]
        }
    }

    private inner abstract class TopicHolder(view : View) : RecyclerView.ViewHolder(view)

    private inner class TopicItemHolder(var binding : ListitemTopicBinding) : TopicHolder(binding.root){
        fun bind(topic: Topic){
            binding.setVariable(BR.topic,topic)
            binding.executePendingBindings()
            NetworkUtils.getInstace(context)!!.loadImage(binding.avatar,topic.user.avatarUrl,R.drawable.default_avatar)
        }
    }

    private inner class TopicNodeHolder(view :View):TopicHolder(view){
        fun setAdapter(adapter : NodeAdapter){
            val recyclerView = itemView.findViewById<RecyclerView>(R.id.node_list)
            recyclerView.adapter = adapter
        }

        fun go(position : Int){
            val recyclerView = itemView.findViewById<RecyclerView>(R.id.node_list)
            recyclerView.smoothScrollToPosition(position)
        }
    }

    private inner class NodeItemHolder(var bingding:ListitemTopicNodeItemBinding):RecyclerView.ViewHolder(bingding.root){
        fun bind(node:Node){
            bingding.setVariable(BR.node,node)
            bingding.executePendingBindings()
            if(node.id == mPresenter!!.getCurrentNodeId()){
                bingding.nodeWrapper.background = resources.getDrawable(R.drawable.topic_node_bg_select)
                bingding.nodeText.setTextColor(resources.getColor(R.color.colorPrimary))
            }else{
                bingding.nodeWrapper.background = resources.getDrawable(R.drawable.topic_node_bg_normal)
                bingding.nodeText.setTextColor(Color.WHITE)
            }

            bingding.root.setOnClickListener {
                var p : Int = adapterPosition
                var node : Node = mNodeAdapter!!.getNodeAt(p)
                mPresenter!!.setCurrentNode(node.id,p)
            }
        }
    }
}