package com.ethanmao.klib.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DataItem<T,VH : RecyclerView.ViewHolder>(data: T) {

    var mData: T ?=null

    init {
        this.mData = data
    }

    // 获取 layoutId
    open fun getLayoutId(): Int{
        return -1
    }

    // 绑定数据
    abstract fun onBind(holder: RecyclerView.ViewHolder, position: Int)

    // 刷新列表
    open fun refreshItem(){

    }

    // 从列表删除
    open fun removeItem(){

    }

    // 获取视图
    open  fun  getItemView(): View? {
        return null
    }

    // 占几列
    open fun  getSpanSize(): Int {
        return 0
    }
}