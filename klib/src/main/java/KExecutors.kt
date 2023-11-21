import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong


object KExecutors {

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

        val kExecutor = object :ThreadPoolExecutor(coreThreadSize, maxThreadSize, keepAliveTime, unit, queue, threadFactory){
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                super.beforeExecute(t, r)
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                super.afterExecute(r, t)
            }
        }


    }



}