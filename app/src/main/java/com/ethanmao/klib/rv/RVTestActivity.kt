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
    lateinit  var headView : TextView
    lateinit  var footView : TextView
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

        binding.rvList.postDelayed({
            adapter.removeFootView(footView)
        },6000)

        binding.rvList.postDelayed({
            adapter.removeHeadView(headView)
        },8000)
    }

    private fun testHeader(adapter: CommonAdapter) {
        headView = TextView(this)
        headView.text = "Header"
        adapter.addHeadView(headView)
    }

    private fun tesFooter(adapter: CommonAdapter) {
        footView= TextView(this)
        footView.text = "Footer"
        adapter.addFootView(footView)
    }

    private fun testAdapter(adapter: CommonAdapter) {
        for (i in 0..2){
            adapter.addItem(i,SimpleDataItem(i.toString()),false)
        }
        adapter.notifyAllDataItem()
    }


}