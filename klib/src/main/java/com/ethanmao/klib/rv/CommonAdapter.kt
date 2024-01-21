package com.ethanmao.klib.rv

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.ParameterizedType

class CommonAdapter(ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // DataItem 数据
    private var mDataItems: MutableList<DataItem<*, RecyclerView.ViewHolder>> = mutableListOf()

    // ViewHolderType 数据
    private var mViewTypes = SparseArray<DataItem<*,RecyclerView.ViewHolder>>()
    private var mLayoutInflater: LayoutInflater? = null
    private var mContext: Context? = null

    init {
        this.mContext = ctx
        this.mLayoutInflater = LayoutInflater.from(ctx)
    }

    /**
     * 添加单个数据
     */
    fun addItem(index: Int, dataItem: DataItem<*,RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            mDataItems.add(index, dataItem)
        } else {
            mDataItems.add(dataItem)
        }
        val notifyIndex = if (index > 0) index else mDataItems.size - 1
        if (notify) {
            notifyItemInserted(notifyIndex)
        }
    }

    /**
     * 添加多个数据
     */
    fun addItems(items: List<DataItem<*,RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = mDataItems.size
        for (item in items){
            mDataItems.add(item)
        }
        if (notify){
            notifyItemRangeChanged(start,items.size)
        }
    }

    /**
     * 移除单个
     */
    fun  removeItem(index: Int): DataItem<*,RecyclerView.ViewHolder>? {
        if (index > 0 && index < mDataItems.size){
            val removeItem = mDataItems.removeAt(index)
            notifyItemRemoved(index)
            return removeItem
        }else{
            return null
        }
    }


    fun  removeItem(item: DataItem<*,RecyclerView.ViewHolder>) {
        if (item != null){
            val index = mDataItems.indexOf(item)
            removeItem(index)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
            val dataItem =mViewTypes[viewType]
            var view = dataItem.getItemView()
            if (view == null){
                val layoutRes = dataItem.getLayoutId()
                if (layoutRes < 0){
                    throw IllegalStateException("layoutRes must > 0")
                }
                view = mLayoutInflater!!.inflate(layoutRes,parent,false)
            }
        return createViewHolderInternal(dataItem.javaClass,view)
    }

    // 内部创建 ViewHoler
    private fun createViewHolderInternal(
        clazz: Class<DataItem<*,RecyclerView.ViewHolder>>,
        view: View?
        ): RecyclerView.ViewHolder {
        // 父类的 Class
        val superClass = clazz.genericSuperclass
        // 判断是不是泛型,由于是 ViewHolder 肯定是泛型
        if (superClass is ParameterizedType){
            val actualTypeArguments = superClass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments){
                // Class  判断是不是父类是不是 RecyclerView.ViewHolder
                if (actualTypeArgument is Class<*>
                    && RecyclerView.ViewHolder::class.java.isAssignableFrom(actualTypeArgument)){
                    // 是,则调用构造方法
                    return actualTypeArgument.getConstructor().newInstance() as RecyclerView.ViewHolder
                }
            }
        }
        return object : RecyclerView.ViewHolder(view!!){}
    }

    override fun getItemCount(): Int {
        return mDataItems.size
    }


    /**
     * Bind Data
     */
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        val dateItem = mDataItems[position]
        val type = dateItem.javaClass.hashCode()
        if (mViewTypes.indexOfKey(type) < 0){
            mViewTypes.put(type,dateItem)
        }
        return type
    }


}