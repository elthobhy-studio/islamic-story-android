package com.elthobhy.islamicstory.recent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.databinding.ItemListNabiBinding
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.databinding.ActivityRecentBinding
import com.elthobhy.islamicstory.detail.DetailActivity
import org.koin.android.ext.android.inject

class RecentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecentBinding
    private val recentViewModel by inject<RecentViewModel>()
    private lateinit var adapterList: AdapterList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentBinding.inflate(layoutInflater)
        adapterList = AdapterList()
        setContentView(binding.root)

        actionBarSet()
        onClick()
        setList()
        setUpRv()
    }

    private fun onClick() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
    }


    private fun setList() {
        recentViewModel.getRecentActivity().observe(this){ data ->
            adapterList.submitList(data)

            binding.clearText.setOnClickListener {
                for(i in data.indices){
                    if(data[i].recentActivity){
                        data[i].keyId?.let { it1 -> recentViewModel.clearRecentActivity(it1, data[i]) }
                    }
                }
            }
        }
    }

    private fun setUpRv() {
        binding.rvListNabi.apply {
            layoutManager = LinearLayoutManager(this@RecentActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = adapterList
            adapterList.setOnItemClickCallback(object: AdapterList.OnItemClickCallback{
                override fun onItemClicked(data: ListDomain, binding: ItemListNabiBinding) {
                    setDetail(data, binding)
                }

            })
        }
    }

    internal fun setDetail(data: ListDomain, binding: ItemListNabiBinding) {
        val intent = Intent(this@RecentActivity, DetailActivity::class.java)
        intent.putExtra(Constants.DATA, data)
        val optionCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@RecentActivity ,
                binding.imageCard, "imageDisplay"
            )
        startActivity(intent, optionCompat.toBundle())
    }

    private fun actionBarSet() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.recent_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}