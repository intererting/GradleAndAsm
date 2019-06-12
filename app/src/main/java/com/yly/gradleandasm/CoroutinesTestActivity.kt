package com.yly.gradleandasm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class CoroutinesTestActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines_test)
    }

    override fun onResume() {
        testCoroutine()
        super.onResume()
    }

    private fun testCoroutine() {
        launch {
            println("1   ${Thread.currentThread()}")
            val job = async(Dispatchers.Main) {
                println("2   ${Thread.currentThread()}")
//                Executors.newScheduledThreadPool(1).scheduleAtFixedRate(Runnable {
//                    println("scheduleAtFixedRate")
//                }, 0, 1000, TimeUnit.MILLISECONDS)
                Executors.newScheduledThreadPool(1).schedule(Runnable {
                    println("scheduleWithFixedDelay  ${coroutineContext.isActive}")
                    launch {
                        println("after  scheduleWithFixedDelay")
                    }
                }, 2000, TimeUnit.MILLISECONDS)
                println(coroutineContext.isActive)
                return@async 3
            }
            println(" job.await()  ${job.await()}")
            println("end")
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}