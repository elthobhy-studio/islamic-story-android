package com.elthobhy.islamicstory.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.upload.UploadActivity

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Constants.DATA, ListDomain::class.java)
        } else {
            intent.getParcelableExtra(Constants.DATA)
        }
        showData(data)
        onClick(data)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.actionBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun onClick(data: ListDomain?) {
        binding.apply {
            val intent = Intent(this@DetailActivity, UploadActivity::class.java)
            intent.putExtra(Constants.DATA, data)
            fabDenganGambar.setOnClickListener {
                intent.putExtra(Constants.REFERENCE, Constants.UPLOAD_WITH_GAMBAR)
                startActivity(intent)
            }
            fabTampaGambar.setOnClickListener {
                intent.putExtra(Constants.REFERENCE, Constants.UPLOAD)
                startActivity(intent)
            }
        }
    }

    private fun showData(data: ListDomain?) {
        binding.apply {
            namaNabi.text = data?.name

            if(data?.display == null || data.profile == null || data.umat?.isEmpty() == true || data.umur?.isEmpty() == true){
                tempatDiutus.visibility = View.GONE
                umur.visibility = View.GONE
                imageDetail.visibility = View.GONE
            }else{
                tempatDiutus.text = String.format("${this@DetailActivity.resources.getString(R.string.tempat_diutus)} %1$1s",
                    data.umat
                )
                Glide.with(this@DetailActivity)
                    .load(data.display)
                    .into(imageDetail)
                umur.text = String.format("${this@DetailActivity.resources.getString(R.string.umur)} %1$1s",
                    data.umur
                )
            }
            data?.detail?.let { kisah.renderMD(it) }
        }
    }
}