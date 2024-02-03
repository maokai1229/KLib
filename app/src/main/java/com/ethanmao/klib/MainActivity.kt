package com.ethanmao.klib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.ethanmao.klib.rv.RVTestActivity
import com.ethanmao.klib.thread.KExecutors

class MainActivity : Activity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mian)
        findViewById<Button>(R.id.bt_create).setOnClickListener(this)
        findViewById<Button>(R.id.bt_rv_test).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_rv_test -> {
                startActivity(Intent(this, RVTestActivity::class.java))
            }
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
       Log.e("打印","测试 delay后打印")
    }


}