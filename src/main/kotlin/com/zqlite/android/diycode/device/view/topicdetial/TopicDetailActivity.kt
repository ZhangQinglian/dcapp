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

import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.view.BaseActivity
import com.zqlite.android.diycode.device.view.custom.DecorViewProxy
import kotlinx.android.synthetic.main.activity_topic_detail.*

/**
 * Created by scott on 2017/8/13.
 */
class TopicDetailActivity : BaseActivity() {

    private var mPresetner : TopicDetailContract.Presenter ? = null
    private var mTopicDetailFragment : TopicDetailFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_topic_detail
    }

    override fun initView() {
        //action bar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.topic_detail)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener({
            finish()
        })

        //slide
        DecorViewProxy().bind(this)
        mTopicDetailFragment = TopicDetailFragment.getInstance(null)
        mPresetner = TopicDetailPresenter(mTopicDetailFragment!!)
        addFragment(mTopicDetailFragment!!,R.id.topic_detail_container)

    }

    override fun initData() {
        var args = intent.extras
        if(args != null){
            var id = args.getInt("topicId")
            mPresetner!!.loadTopicDetail(id)
        }
    }
}