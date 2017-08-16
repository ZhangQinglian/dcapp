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

package com.zqlite.android.diycode.device.view.login

import android.os.Bundle
import android.widget.Toast
import com.zqlite.android.dclib.entiry.Token
import com.zqlite.android.diycode.R
import com.zqlite.android.diycode.device.utils.TokenStore
import com.zqlite.android.diycode.device.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
/**
 * Created by scott on 2017/8/15.
 */
class LoginFragment : BaseFragment(),LoginContract.View {


    private var mPresenter : LoginContract.Presenter? = null

    override fun setPresenter(presenter: LoginContract.Presenter) {
        mPresenter = presenter
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        login.setOnClickListener{
            val loginName = login_name_input.text.toString()
            val password = password_input.text.toString()
            if(!loginName.isEmpty() && !password.isEmpty()){
                mPresenter!!.login(loginName,password)
            }
        }
    }

    override fun initData() {
    }

    override fun updateToken(token: Token,login:String) {
        TokenStore.saveToken(context,token)
        TokenStore.saveCurrentLogin(context,login)
        Toast.makeText(context,R.string.login_success,Toast.LENGTH_LONG).show()
        activity.finish()
    }

    override fun loginError() {
        Toast.makeText(context,R.string.login_error,Toast.LENGTH_LONG).show()
    }
    companion object Factory{
        fun getInstance(args : Bundle?):LoginFragment{
            val fragment = LoginFragment()
            if(args != null){
                fragment.arguments = args
            }
            return fragment
        }
    }

}