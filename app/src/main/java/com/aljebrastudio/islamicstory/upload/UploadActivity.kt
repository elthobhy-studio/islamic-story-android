package com.aljebrastudio.islamicstory.upload

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityUploadBinding
import org.koin.android.ext.android.inject

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val uploadViewModel by inject<UploadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnUpload.setOnClickListener {
                val nama = editTextName.text.toString().trim()
                val umur = editTextUmur.text.toString().trim()
                val tempatDiutus = editTextTempatDiutus.text.toString().trim()
                val kisah = editTextKisah.text.toString().trim()
                val keyId = editTextKeyId.text.toString().trim()
                uploadViewModel.postDataNabi(nama, umur, tempatDiutus, kisah, keyId).observe(this@UploadActivity){
                    when(it.status){
                        Status.SUCCESS -> {
                            Toast.makeText(this@UploadActivity, "success", Toast.LENGTH_LONG).show()
                            Log.e("success", "onClick: ${it.data}" )
                        }
                        Status.ERROR -> {
                            Log.e("error", "onClick: ${it.message}" )
                            Toast.makeText(this@UploadActivity, "error", Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            Log.e("loading", "onClick: ${it.status}" )
                        }
                    }
                }
            }
        }
    }
}