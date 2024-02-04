package com.ethanmao.klib.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DataItem<T,VH : RecyclerView.ViewHolder>(data: T) {

    var mData: T ?=null
    var mAdapter : CommonAdapter ?= null

    init {
        this.mData = data
    }

    // 获取 layoutId
    open fun getLayoutId(): Int{
        return -1
    }

    // 绑定数据
    abstract fun onBind(viewHolder: VH, position: Int)

    // 刷新列表
    open fun refreshItem(){
        mAdapter?.refreshItem(this)
    }

    // 从列表删除
    open fun removeItem(){
        mAdapter?.removeItem(this)
    }

    fun setAdapter(adapter: CommonAdapter){
        this.mAdapter = adapter
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