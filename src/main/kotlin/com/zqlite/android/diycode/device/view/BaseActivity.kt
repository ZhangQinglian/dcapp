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

package com.zqlite.android.diycode.device.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zqlite.android.diycode.R

/**
 * Created by scott on 2017/8/11.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
    }

    abstract fun getLayoutId() : Int

    abstract fun initView()

    abstract fun initData()

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.comm_right_in,R.anim.comm_hold)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.comm_hold,R.anim.comm_right_out)
    }
    fun addFragment(fragment:BaseFragment,containerID:Int){
        supportFragmentManager.beginTransaction().add(containerID,fragment).commit()
    }

    fun replaceFragment(fragment: BaseFragment,containerID:Int){
        supportFragmentManager.beginTransaction().replace(containerID,fragment).commit()
    }

    fun hideFragment(fragment: BaseFragment){
        supportFragmentManager.beginTransaction().hide(fragment).commit()
    }

    fun showFragment(fragment: BaseFragment){
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }
}