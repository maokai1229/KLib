package com.ethanmao.klib.rv

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ethanmao.klib.R

class SimpleDataItem @JvmOverloads constructor(val content: String) :
    DataItem<String, ViewHolder>(content) {


    override fun getLayoutId(): Int {
        return R.layout.item_simple
    }

    override fun onBind(holder: ViewHolder, position: Int) {
        val tvContent = holder.itemView.findViewById<TextView>(R.id.tv_content)
        tvContent.text = content
    }
}