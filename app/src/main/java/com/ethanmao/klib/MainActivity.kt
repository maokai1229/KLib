package com.ethanmao.klib

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.ethanmao.klib.thread.KExecutors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : Activity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mian)
        findViewById<Button>(R.id.bt_create).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        GlobalScope.launch {
            testCoroutine()
        }
    }

    private fun test() {
        KExecutors.execute(2, Runnable {
            Log.e("打印","测试")
        })
        KExecutors.execute(1, Runnable {
            Log.e("打印","测试")
        })
        KExecutors.execute(3, Runnable {
            Log.e("打印","测试")
        })


        KExecutors.executeCallable(1, object : KExecutors.Callable <String>(){
            override fun onBeforeRun() {

            }

            override fun onBackground(): String {
                TODO("Not yet implemented")
            }

            override fun onCompleted(t: String) {
                TODO("Not yet implemented")
            }

        })
    }

   suspend  fun  testCoroutine(){
       Log.e("打印","测试 delay 前")
       delay(2000)
       Log.e("打印","测试 delay后打印")
    }


}