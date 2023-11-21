package com.ethanmao.klib.thread

import android.util.Log
import java.util.concurrent.Callable
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * 线程池工具
 * 可打印任务执行时间
 * 按优先级的顺序来执行
 *
 */
object KExecutors {

    val  TAG = "KExecutors"
    var mKExecutor : ThreadPoolExecutor
    // 用来实现暂停和恢复执行
    val mLock  = ReentrantLock()
    val mCondition = mLock.newCondition()
    var mPaused = false

    init {
        val coreThreadSize = Runtime.getRuntime().availableProcessors() + 1
        val maxThreadSize = coreThreadSize * 2 + 1
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS
        val queue = PriorityBlockingQueue<Runnable>()
        val threadName = AtomicLong()

        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "KLib-Thread-"+threadName.getAndIncrement()
            return@ThreadFactory thread
        }

        mKExecutor = object :ThreadPoolExecutor(coreThreadSize, maxThreadSize, keepAliveTime, unit, queue, threadFactory){
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                // 判断是否已经暂停了,如果暂停就不要执行了
                if(mPaused){
                    mLock.lock()
                    try {
                        mCondition.await()
                    }finally {
                        mLock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                // 打印当前工作线程数

                // 打印线程任务的执行时间
            }
        }
    }

    @JvmOverloads
    fun  execute(runnable: Runnable) {
        mKExecutor.execute(runnable)
    }





    /**
     * 这里调用 pause,已经加入的任务不会马上暂停,会在执行下一个任务前判断
     * 具体见  beforeExecute() 中实现
      */
    @Synchronized
    fun pause() {
        mPaused = true
        Log.e(TAG,"pause")
    }


    /**
     * 唤醒
     *  Synchronized 用于多线程调用加锁
     *  Condition 用于唤醒线程
     */
    @Synchronized
    fun resume() {
        mPaused = false
        mLock.lock()
        try {
            // 唤醒所有阻塞的线程
            mCondition.signalAll()
        }finally {
            mLock.unlock()
        }
        Log.e(TAG,"resume")
    }

    fun stop() {
//        kExecutor.()
    }

    @Synchronized
    fun isPaused() : Boolean{
        return mPaused
    }

}