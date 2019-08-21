package com.yly.gradleandasm

import com.yly.asmannotation.LoginCallback

@LoginCallback
object LoginCheckUtil : LoginCheckInterface {
    override fun unloginCallback() {
        println("=================没有登录====================")
    }

    override fun appLogined(): Boolean {
        return false
    }
}