package com.yly.gradleandasm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yly.asmannotation.LoginCallback
import com.yly.asmannotation.NeedLoginCheck
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println(Template().temp())
//        println(temp())
        goToCoroutinesTest.setOnClickListener @NeedLoginCheck {
            println("setOnClickListener")
        }
    }
}
