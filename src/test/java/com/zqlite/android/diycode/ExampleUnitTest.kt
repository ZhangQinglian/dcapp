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

package com.zqlite.android.diycode

import org.junit.Test

import org.junit.Assert.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun parseHtml(){
        var pattern : Pattern = Pattern.compile("<a href=\"/.*?class=\"at_user\"")
        var html = "<p><a href=\"#reply1\" class=\"at_floor\" data-floor=\"1\">#1楼</a> <a href=\"/luhaoaimama1\" class=\"at_user\" title=\"@luhaoaimama1\"><i>@</i>luhaoaimama1</a> 我写的是社区首页的地址，这个对app应该没什么影响。<a href=\"/luhaoaimama1\" class=\"at_user\" title=\"@luhaoaimama1\"><i>@</i>luhaoaimama1</a></p>"
        var matcher = pattern.matcher(html)
        if(matcher.find()){
            var s = matcher.group()
            var index = html.indexOf(s)
            var builder : StringBuilder = StringBuilder(html)
            builder.insert(index + 9,"http://localhost")
            println(builder.toString())
        }
    }
}
