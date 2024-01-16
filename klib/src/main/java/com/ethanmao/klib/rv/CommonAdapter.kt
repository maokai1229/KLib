package com.ethanmao.klib.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CommonAdapter (ctx : Context): RecyclerView.Adapter<ViewHolder>() {

    // DataItem 数据
    private var mDataItems : MutableList<DataItem<*,ViewHolder>> = mutableListOf()
    // ViewHolderType 数据 ? 不是和上面的重复了嘛?
    private var mViewTypes : MutableList<DataItem<*,ViewHolder>> = mutableListOf()
    private var mLayoutInflater : LayoutInflater? = null
    private var mContext : Context? = null

    init {
        this.mContext = ctx
        this.mLayoutInflater = LayoutInflater.from(ctx)
    }



    fun addItem(index : Int , dataItem: DataItem<* , ViewHolder> , notify : Boolean){

    }

    fun addItem(dataItem: DataItem<*, ViewHolder>,notify: Boolean){

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    }

    override fun getItemCount(): Int {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }



}