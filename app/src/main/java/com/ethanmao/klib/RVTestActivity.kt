package com.ethanmao.klib

import android.app.Activity
import android.os.Bundle
import com.ethanmao.klib.databinding.ActivityRvTestBinding

class RVTestActivity : Activity() {

    lateinit  var  binding : ActivityRvTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init ViewBinding
        binding = ActivityRvTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}