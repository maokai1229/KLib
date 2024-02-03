package com.ethanmao.klib.rv

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethanmao.klib.databinding.ActivityRvTestBinding
import com.ethanmao.klib.rv.CommonAdapter

class RVTestActivity : Activity() {

    lateinit  var  binding : ActivityRvTestBinding
    // Fragement
    //     binding = FragmentMineBinding.inflate(inflater,container,false);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init ViewBinding
        binding = ActivityRvTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRv()
    }

    private fun initRv() {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter =   CommonAdapter(this)
        binding.rvList.adapter = adapter
        testAdapter(adapter)
    }

    private fun testAdapter(adapter: CommonAdapter) {
        for (i in 0..10){
            adapter.addItem(i,SimpleDataItem(i.toString()),false)
        }
        adapter.notifyAllDataItem()
    }


}