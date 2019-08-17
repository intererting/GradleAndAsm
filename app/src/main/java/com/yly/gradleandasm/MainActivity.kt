package com.yly.gradleandasm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TestJavaClass().testInitData()
        println("xxxxx  ${TestJavaClass.a}")
        println(TestJavaClass.map["name"])
    }

    override fun onResume() {
        super.onResume()
    }

}
