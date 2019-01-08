package com.example.project.myproject;

import android.webkit.JavascriptInterface;

/**
 * Created by wangshuo on 2018/12/19.
 */

// 继承自Object类
public class AndroidtoJs1 extends Object {

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        System.out.println("JS调用了Android的hello方法");
    }

}
