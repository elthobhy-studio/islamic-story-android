package com.aljebrastudio.islamicstory.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain
import com.aljebrastudio.islamicstory.core.utils.Constants
import com.aljebrastudio.islamicstory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        showData()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.actionBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showData() {
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Constants.DATA, ListDomain::class.java)
        } else {
            intent.getParcelableExtra(Constants.DATA)
        }

        binding.apply {
            namaNabi.text = data?.name
            tempatDiutus.text = data?.umat
            Glide.with(this@DetailActivity)
                .load(data?.display)
                .into(imageDetail)
            umur.text = data?.umur
            kisah.text = data?.detail
        }
    }
}