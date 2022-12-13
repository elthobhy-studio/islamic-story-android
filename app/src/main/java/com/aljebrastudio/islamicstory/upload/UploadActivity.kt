package com.aljebrastudio.islamicstory.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aljebrastudio.islamicstory.R
import com.aljebrastudio.islamicstory.core.utils.uriToFile
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityUploadBinding
import org.koin.android.ext.android.inject
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val uploadViewModel by inject<UploadViewModel>()
    private var getFileProfile: File? = null
    private var getFIleDisplay: File? = null
    private var profile: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick() {
        binding.apply {
            uploadImageProfile.setOnClickListener {
                this@UploadActivity.profile = true
                startGallery(profile)
            }
            uploadImageDisplay.setOnClickListener {
                this@UploadActivity.profile = false
                startGallery(!profile)
            }
            btnUpload.setOnClickListener {
                val profile = getFileProfile
                val display = getFIleDisplay
                val nama = editTextName.text.toString().trim()
                val umur = editTextUmur.text.toString().trim()
                val tempatDiutus = editTextTempatDiutus.text.toString().trim()
                val kisah = editTextKisah.text.toString().trim()
                val keyId = editTextKeyId.text.toString().trim()
                if (profile != null && display != null) {
                    uploadViewModel.postDataNabi(nama, umur, tempatDiutus, kisah, keyId, profile, display).observe(this@UploadActivity){
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

    private fun startGallery(profile: Boolean) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        if(profile){
            launcherIntentGallery.launch(chooser)
        }else{
            launcherIntentGallery.launch(chooser)
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val file = uriToFile(selectedImage, this@UploadActivity)
            if(profile){
                binding.profileView.setImageURI(selectedImage)
                getFileProfile = file
            }else{
                binding.displayView.setImageURI(selectedImage)
                getFIleDisplay = file
            }
        }
    }
}