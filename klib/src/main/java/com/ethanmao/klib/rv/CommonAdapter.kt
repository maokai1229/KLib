package com.ethanmao.klib.rv

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.ParameterizedType

class CommonAdapter(ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // DataItem 数据
    private var mDataItems: MutableList<DataItem<*, RecyclerView.ViewHolder>> = mutableListOf()

    // ViewHolderType 数据
    private var mViewTypes = SparseArray<DataItem<*, RecyclerView.ViewHolder>>()
    private var mLayoutInflater: LayoutInflater? = null
    private var mContext: Context? = null

    // HeadView
    private var mHeadViews: SparseArray<View> = SparseArray()
    private var HEADER_BASE = 10000
    // FootView
    private var mFootViews: SparseArray<View> = SparseArray()
    private var FOOTER_BASE = 20000


    init {
        this.mContext = ctx
        this.mLayoutInflater = LayoutInflater.from(ctx)
    }

    /**
     * 添加单个数据
     */
    fun addItem(index: Int, dataItem: DataItem<*, RecyclerView.ViewHolder>, notify: Boolean) {
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
    fun addItems(items: List<DataItem<*, RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = mDataItems.size
        for (item in items) {
            mDataItems.add(item)
        }
        if (notify) {
            notifyItemRangeChanged(start, items.size)
        }
    }

    /**
     * 移除单个
     */
    fun removeItem(index: Int): DataItem<*, RecyclerView.ViewHolder>? {
        if (index > 0 && index < mDataItems.size) {
            val removeItem = mDataItems.removeAt(index)
            notifyItemRemoved(index)
            return removeItem
        } else {
            return null
        }
    }


    fun removeItem(item: DataItem<*, RecyclerView.ViewHolder>) {
        if (item != null) {
            val index = mDataItems.indexOf(item)
            removeItem(index)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mHeadViews.indexOfKey(viewType) >= 0 ){
            val view = mHeadViews[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        } else if (mFootViews.indexOfKey(viewType) >= 0 ){
            val view = mFootViews[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }

        val dataItem = mViewTypes[viewType]
        var view = dataItem.getItemView()
        if (view == null) {
            val layoutRes = dataItem.getLayoutId()
            if (layoutRes < 0) {
                throw IllegalStateException("layoutRes must > 0")
            }
            view = mLayoutInflater!!.inflate(layoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, view)
    }

    // 内部创建 ViewHoler
    private fun createViewHolderInternal(
        clazz: Class<DataItem<*, RecyclerView.ViewHolder>>,
        view: View?
    ): RecyclerView.ViewHolder {
        // 父类的 Class
        val superClass = clazz.genericSuperclass
        // 判断是不是泛型,由于是 ViewHolder 肯定是泛型
        if (superClass is ParameterizedType) {
            val actualTypeArguments = superClass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                // Class  判断是不是父类是不是 RecyclerView.ViewHolder
                if (actualTypeArgument is Class<*>
                    && RecyclerView.ViewHolder::class.java.isAssignableFrom(actualTypeArgument)
                ) {
                    // 是,则调用构造方法
                    return actualTypeArgument.getConstructor()
                        .newInstance() as RecyclerView.ViewHolder
                }
            }
        }
        return object : RecyclerView.ViewHolder(view!!) {}
    }

    /**
     * 包含头尾的 Item 个数
     */
    override fun getItemCount(): Int {
        // 增加头尾
        return mDataItems.size + mHeadViews.size() + mFootViews.size()
    }

    /**
     *  除去头尾的 Item 个数
     */
    fun getRealItemCount(): Int {
        return mDataItems.size
    }


    /**
     * Bind Data
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 怎么处理头尾?
        //1. 在这里判断头尾,不走,那怎么绑定数据,在 View 内部自己处理
        if(isHeader(position) || isFooter(position))
            return
        // or 2. 走这里处理头尾
        // 常规的  Item Bind
        val dataItem = mDataItems.get(position - mHeadViews.size())
        dataItem.onBind(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        // 相应的也要增加头尾的判断
        if(isHeader(position)){
            return mHeadViews.keyAt(position)
        }
        if (isFooter(position)){
            return mFootViews.keyAt(position -(getRealItemCount() + mHeadViews.size()))
        }
        // 普通 ItemType
        val dataItem = mDataItems[position]
        val type = dataItem.javaClass.hashCode()
        if (mViewTypes.indexOfKey(type) < 0) {
            mViewTypes.put(type, dataItem)
        }
        return type
    }


    /**
     * 适配GridLayoutManager
     * 这里的目的是,根据真实的 SpanCout 来动态设置
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // 获取 SpanCout
        val layoutManager = recyclerView.layoutManager
        // 这里获取 SpanCout 的目的
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            // 动态设置网格布局的列数
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < mDataItems.size) {
                        val dataItem = mDataItems[position]
                        val spanSize = dataItem.getSpanSize()
                        return if (spanSize <= 0) spanCount else spanSize
                    }
                    return spanCount
                }
            }
        }

    }


    /**
     *  TODO 适配 StaggeredGridLayoutManager
     *  瀑布流布局
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        // 如果是瀑布流布局管理器,增加对应的处理

    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

    }

    fun removeItem(dataItem: DataItem<*, *>) {
        if (dataItem != null) {
            val index = mDataItems.indexOf(dataItem)
            removeItem(index)
        }
    }

    fun refreshItem(dataItem: DataItem<*, *>) {
        if (dataItem != null) {
            val index = mDataItems.indexOf(dataItem)
            notifyItemChanged(index)
        }
    }

    //    <------------------ HeadView & FootView ------------------>
    fun isHeader(position: Int): Boolean {
        if (mHeadViews.size() == 0)
            return false
        return position < mHeadViews.size()
    }

    fun isFooter(position: Int): Boolean {
        if (mFootViews.size() == 0)
            return false
        return position > mHeadViews.size() + mDataItems.size - 1
    }

    fun addHeadView(view: View) {
        //TODO 如果添加过,是先移除旧的,换成新的? 还是有就不再添加,这里根据场景来
        // 先定成有就不重复添加
        if (mHeadViews.indexOfValue(view) < 0) {
            mHeadViews.put(HEADER_BASE++, view)
            notifyItemInserted(mHeadViews.size() - 1)
        }
    }

    fun removeHeadView(view: View) {
        // 存在,才 remove
        val index = mHeadViews.indexOfValue(view)
        if (index >= 0) {
            mHeadViews.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun addFootView(view: View) {
        // 尾部的位置怎么确定
        if (mFootViews.indexOfValue(view) < 0) {
            mFootViews.put(FOOTER_BASE++, view)
            notifyItemInserted(itemCount-1)
        }
    }

    fun removeFootView(view: View) {
        val index = mFootViews.indexOfValue(view)
        if (index >= 0) {
            mFootViews.removeAt(index)
            notifyItemRemoved(mHeadViews.size() + mDataItems.size + index -1)
        }
    }
}