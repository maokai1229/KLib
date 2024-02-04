package com.ethanmao.klib.rv

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethanmao.klib.databinding.ActivityRvTestBinding
import com.ethanmao.klib.databinding.ActivityRvTestBinding.*
import com.ethanmao.klib.rv.CommonAdapter

class RVTestActivity : Activity() {

    lateinit  var  binding : ActivityRvTestBinding
    // Fragement
    //     binding = FragmentMineBinding.inflate(inflater,container,false);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init ViewBinding
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initRv()
    }

    private fun initRv() {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter =   CommonAdapter(this)
        binding.rvList.adapter = adapter
        testAdapter(adapter)

        binding.rvList.postDelayed({
            testHeader(adapter)
        },2000)

        binding.rvList.postDelayed({
            tesFooter(adapter)
        },4000)
    }

    private fun testHeader(adapter: CommonAdapter) {
        val headView = TextView(this)
        headView.text = "Header"
        adapter.addHeadView(headView)
    }

    private fun tesFooter(adapter: CommonAdapter) {
        val headView = TextView(this)
        headView.text = "Footer"
        adapter.addFootView(headView)
    }

    private fun testAdapter(adapter: CommonAdapter) {
        for (i in 0..2){
            adapter.addItem(i,SimpleDataItem(i.toString()),false)
        }
        adapter.notifyAllDataItem()
    }


}