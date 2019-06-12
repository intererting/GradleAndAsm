package com.yly.gradleandasm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        launch {
//            repeat(100) {
//                delay(1000)
//                println("xxxxxxxxxx")
//            }
//        }

        goToCoroutinesTest.setOnClickListener {
            startActivity(Intent(this@MainActivity, CoroutinesTestActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
