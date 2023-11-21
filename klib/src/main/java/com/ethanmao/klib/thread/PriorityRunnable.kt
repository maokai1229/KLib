package com.ethanmao.klib.thread

/**
 * 优先级比较的 Runnable
 */
class PriorityRunnable(val priority : Int, val runnable: Runnable) : Runnable,Comparable<PriorityRunnable>{

    override fun run() {
        runnable.run()
    }

    override fun compareTo(other: PriorityRunnable): Int {
        // 优先级的计算方式
        //具体来说，PriorityBlockingQueue 会将元素按照以下规则进行排序：
        //如果两个元素的 compareTo() 方法返回 0，则两个元素具有相同的优先级。
        //如果两个元素的 compareTo() 方法返回负数，则第一个元素的优先级高于第二个元素。
        //如果两个元素的 compareTo() 方法返回正数，则第一个元素的优先级低于第二个元素。
        return  if (priority > other.priority) 1 else if (priority < other.priority) -1 else 0
    }
}