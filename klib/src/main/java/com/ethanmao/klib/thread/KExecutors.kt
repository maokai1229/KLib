package com.ethanmao.klib.thread

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

/**
 * 线程池工具 (CPU 密集型),支持暂停/恢复
 * 可用于批量上传下载
 * 按优先级的顺序来执行
 *
 */
object KExecutors {

    private val TAG = "KExecutors"
    private var mKExecutor: ThreadPoolExecutor

    // 用来实现暂停和恢复执行
    private val mLock = ReentrantLock()
    private val mCondition = mLock.newCondition()
    private var mPaused = false

    // 执行结果发送到主线程中
    private val mHandler = Handler(Looper.getMainLooper())

    init {
        val coreThreadSize = Runtime.getRuntime().availableProcessors() + 1
        val maxThreadSize = coreThreadSize * 2 + 1
        val keepAliveTime = 20L
        val unit = TimeUnit.SECONDS
        val queue = PriorityBlockingQueue<Runnable>()
        val threadName = AtomicLong()

        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "KLib-Thread-" + threadName.getAndIncrement()
            return@ThreadFactory thread
        }

        mKExecutor = object : ThreadPoolExecutor(
            coreThreadSize,
            maxThreadSize,
            keepAliveTime,
            unit,
            queue,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                // 判断是否已经暂停了,如果暂停就不要执行了
                if (mPaused) {
                    mLock.lock()
                    try {
                        mCondition.await()
                    } finally {
                        mLock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                // 打印当前工作线程数
                r as PriorityRunnable
                Log.e(TAG, "已经执行完成的线程的优先级为" + r.priority)
                // 打印线程任务的执行时间
            }
        }
    }

    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        mKExecutor.execute(PriorityRunnable(priority, runnable))
    }

    /**
     * 将返回值 post 到主线程的方法
     * <*> 表示可以为任意值
     */
    fun executeCallable(@IntRange(from = 0, to = 10) priority: Int = 0, callable: Callable<*>) {
        mKExecutor.execute(PriorityRunnable(priority, callable))
    }

    /**
     * 这里调用 pause,已经加入的任务不会马上暂停,会在执行下一个任务前判断
     * 具体见  beforeExecute() 中实现
     */
    @Synchronized
    fun pause() {
        mPaused = true
        Log.e(TAG, "pause")
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
        } finally {
            mLock.unlock()
        }
        Log.e(TAG, "resume")
    }

    @Synchronized
    fun isPaused(): Boolean {
        return mPaused
    }


    /**
     * 执行代返回值的
     */
    abstract class Callable<T> : Runnable {
        abstract fun onBeforeRun()
        abstract fun onBackground(): T
        abstract fun onCompleted(t: T)

        override fun run() {
            mHandler.post {
                onBeforeRun()
            }
            val t = onBackground()
            mHandler.post {
                onCompleted(t)
            }
        }
    }
}