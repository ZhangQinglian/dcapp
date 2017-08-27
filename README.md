# 可能是东半球最有特色的DiyCode Android客户端

## 软件截图

![](http://7xprgn.com1.z0.glb.clouddn.com/diycode_screens.png)
[点击查看大图](http://7xprgn.com1.z0.glb.clouddn.com/diycode_screens.png)

## 功能

- 登录、退出登录
- 注册（暂不支持）

----

- 分类查看主题
- 回复主题，回复楼层
- 回复中插入图片，连接
- 查看个人资料及其所有主题
- 对主题进行收藏、点赞、和关注
- 发布主题（暂不支持）
- 查看主题及回复中的图片

----

- 项目（暂不支持）
- News（暂不支持）
- 酷站（暂不支持）

----

- 查看通知

----

- 个人中心
- 查看我的话题
- 查看我关注的人
- 查看关注我的人
- 查看我的收藏

## 关于缺失功能

大部分缺失功能是因为时间问题，后期都能加上。

`发布主题`这个功能我想了很久暂时还是不加为好。第一，在手机上大量编写Markdown文本体验不加。第二，这个功能会降低发水贴的成本，降低网站文章的质量。感兴趣的同学可以后期自己加上。

## 特点

本工程用Kotlin编写完成，没有一个Java类（如果发现有java文件可提醒我，我会将其改成kotlin文件）。如果想要学习Kotlin，这也是个不错的Demo。

## 源码及使用

本工程源码分三个模块`dcapp`，`dclib`,`ak47`,其中dclib提供简洁的api为上层提供支持，dcapp为UI模块，ak47为extension function扩展库。


### dclib中开放接口


```kotlin

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

package com.zqlite.android.dclib

import android.os.Environment
import com.zqlite.android.dclib.entiry.*
import com.zqlite.android.dclib.sevice.DiyCodeContract
import com.zqlite.android.dclib.sevice.DiyCodeService
import io.reactivex.Observable
import okhttp3.*
import java.io.File

/**
 * Created by scott on 2017/8/11.
 */
object DiyCodeApi:DiyCodeService.Callback{

    private var mCallback:Callback?=null

    private val service : DiyCodeService = DiyCodeService.create(this)

    interface Callback{
        fun getToken():String?
    }

    fun init(callback:Callback){
        mCallback = callback
    }

    override fun getToken(): String? {
        if(mCallback == null){
            throw RuntimeException("****************   you should init DiyCodeApi ")
        }
        return mCallback!!.getToken()
    }


    fun loadTopic(offset : Int, limit:Int) : Observable<List<Topic>>{
        return service.getTopic(offset,limit)
    }

    fun loadTopic(offset : Int, limit:Int, type:String=DiyCodeContract.TopicParams.typeLastActived, nodeId:Int) : Observable<List<Topic>>{
        return service.getTopic(type,nodeId,offset,limit)
    }

    fun followTopic(topicId:Int):Observable<ResponseBody>{
        return service.followTopic(topicId)
    }

    fun unfollowTopic(topicId:Int):Observable<ResponseBody>{
        return service.unfollowTopic(topicId)
    }

    fun favoriteTopic(topicId:Int):Observable<ResponseBody>{
        return service.favoriteTopic(topicId)
    }

    fun unFavoriteTopic(topicId:Int):Observable<ResponseBody>{
        return service.unfavoriteTopic(topicId)
    }
    fun loadNodes() : Observable<MutableList<Node>>{
        return service.getNodes()
    }

    fun loadTopicDetail(id:Int):Observable<TopicDetail>{
        return service.getTopicDetail(id)
    }

    fun loadTopicReplies(id:Int):Observable<List<TopicReply>>{
        return service.getTopicReplies(id)
    }

    fun replyTopic(id: Int,content:String):Observable<ResponseBody>{
        return service.replyTopic(id,content)
    }

    fun loadUserTopics(login: String,offset: Int,limit: Int):Observable<List<Topic>>{
        return service.getUserTopics(login,offset,limit)
    }

    fun loadUserDetail(login:String):Observable<UserDetail>{
        return service.getUserDetail(login)
    }

    fun loadMe():Observable<UserDetail>{
        return service.getMe()
    }

    fun like(id:Int,type:String):Observable<ResponseBody>{
        return service.like(id,type)
    }

    fun followUser(loginName:String):Observable<ResponseBody>{
        return service.followUser(loginName)
    }

    fun unfollowUser(loginName:String):Observable<ResponseBody>{
        return service.unfollowUser(loginName)
    }

    fun getfollowing(login: String,offset:Int,limit: Int):Observable<List<User>>{
        return service.getFollowing(login,offset,limit)
    }
    fun getfollower(login: String,offset:Int,limit: Int):Observable<List<User>>{
        return service.getFollowers(login,offset,limit)
    }
    fun getFavoriteTopics(login: String,offset: Int,limit: Int):Observable<List<Topic>>{
        return service.getUserFavoriteTopics(login,offset,limit)
    }
    fun unlike(id:Int,type:String):Observable<ResponseBody>{
        return service.unlike(id,type)
    }

    fun login(login:String,password:String) : Observable<Token>{
        return service.login(DiyCodeContract.kOAuthUrl,"password",login,password,BuildConfig.CLIENT_ID,BuildConfig.CLIENT_SECRET)
    }

    fun uploadPhoto(file: File):Observable<ResponseBody>{
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val body = RequestBody.create(MediaType.parse("multipart/form-data"),file)
        builder.addFormDataPart("file",file.name,body)
        val parts = builder.build().parts()
        return service.uploadPhoto(parts)
    }

    fun updateDevice(token:String):Observable<ResponseBody>{
        return service.updateDevice(token = token)
    }

    fun getNotification(offset: Int,limit: Int):Observable<List<Notification>>{
        return service.getNotification(offset,limit)
    }

    fun readNotification(ids:List<Int>):Observable<ResponseBody>{
        return service.readNotification(ids)
    }

    fun deleteDevice(token:String):Observable<ResponseBody>{
        return service.deleteDevice(token = token)
    }
}

```


### 模块地址

dclib:[https://github.com/ZhangQinglian/dclib](https://github.com/ZhangQinglian/dclib)
dcapp:[https://github.com/ZhangQinglian/dcapp](https://github.com/ZhangQinglian/dcapp)
ak47:[https://github.com/ZhangQinglian/ak47](https://github.com/ZhangQinglian/ak47)
### 编译

**需要使用Android Studio 3.0及以上版本**

- 自己再本地新建一个Project，然后将上述两个模块导入，并设置dcapp依赖dclib和ak47
- 在dclib中添加local.properties文件，并设置以下两个值：

```properties
client_id=0*****4c
client_secret=a78ca******************************23e2f8
```

OK，此时项目已经可以成功编译了

## APK下载

[点击下载](http://7xprgn.com1.z0.glb.clouddn.com/DCv0.1.apk)

扫码下载

![](http://7xprgn.com1.z0.glb.clouddn.com/1503384548.png)


## 最后

感谢DiyCode提供Api，此举乃大义。

发现bug可递交至对应仓库的Issues，对代码有问题可以邮件我 zqlxtt@live.com


### Licese

```
Copyright 2017 zhangqinglian

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```