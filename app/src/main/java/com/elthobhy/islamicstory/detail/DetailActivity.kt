package com.elthobhy.islamicstory.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.utils.dialogError
import com.elthobhy.islamicstory.core.utils.dialogLoading
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.listdata.ListViewModel
import com.elthobhy.islamicstory.upload.UploadActivity
import org.koin.android.ext.android.inject

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val listViewModel by inject<ListViewModel>()
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = dialogLoading(this)
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
        data?.tag?.let { tag ->
            listViewModel.getListNabi(tag).observe(this){ res ->
                when(res.status){
                    Status.LOADING -> {
                        dialog.show()
                    }
                    Status.SUCCESS -> {
                        dialog.dismiss()
                        res.data?.map {
                            if(it.keyId == data.keyId){
                                binding.apply {
                                    namaNabi.text = it.name

                                    if(it.display == null || it.profile == null || it.umat?.isEmpty() == true || it.umur?.isEmpty() == true){
                                        tempatDiutus.visibility = View.GONE
                                        umur.visibility = View.GONE
                                        imageDetail.visibility = View.GONE
                                    }else{
                                        tempatDiutus.visibility = View.VISIBLE
                                        umur.visibility = View.VISIBLE
                                        imageDetail.visibility = View.VISIBLE
                                        tempatDiutus.text = String.format("${this@DetailActivity.resources.getString(R.string.tempat_diutus)} %1$1s",
                                            it.umat
                                        )
                                        Glide.with(this@DetailActivity)
                                            .load(it.display)
                                            .into(imageDetail)
                                        umur.text = String.format("${this@DetailActivity.resources.getString(R.string.umur)} %1$1s",
                                            it.umur
                                        )
                                    }
                                    it.detail?.let { kisah.renderMD(it) }
                                }
                            }
                        }

                    }
                    Status.ERROR -> {
                        dialog.dismiss()
                        dialogError(res.message,this).show()
                    }
                }
            }
        }
    }
}