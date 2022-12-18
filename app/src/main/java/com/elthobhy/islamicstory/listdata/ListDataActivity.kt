package com.elthobhy.islamicstory.listdata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.databinding.ActivityListDataBinding

class ListDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}