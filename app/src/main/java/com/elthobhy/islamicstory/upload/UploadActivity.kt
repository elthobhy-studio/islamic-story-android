package com.elthobhy.islamicstory.upload

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.utils.*
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityUploadBinding
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val uploadViewModel by inject<UploadViewModel>()
    private var getFileProfile: File? = null
    private var getFIleDisplay: File? = null
    private var profile: Boolean = true
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Constants.DATA, ListDomain::class.java)
        } else {
            intent.getParcelableExtra(Constants.DATA)
        }
        dialogLoading = dialogLoading(this)
        initActionBar()
        onClick(data)
        initMdEditor()
        showDataToUpdate(data)
    }

    private fun initMdEditor() {
        binding.apply {
            editTextKisah.setStylesBar(binding.styleBar)
            val intent = intent.getStringExtra(Constants.REFERENCE)
            if(intent == Constants.UPLOAD){
                editTextName.hint = getString(R.string.title)
                editInput2.visibility = View.GONE
                editInput3.visibility = View.GONE
                uploadButton.visibility = View.GONE
            }
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.btnClose)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun showDataToUpdate(data: ListDomain?) {
        Log.e("data", "showDataToUpdate: $data" )
        if(data!=null){
            binding.apply {
                editTextName.setText(data.name)
                editTextTag.setText(data.tag)
                data.detail?.let { editTextKisah.renderMD(it) }
                editTextUmur.setText(data.umur)
                editTextTempatDiutus.setText(data.umat)
                Glide.with(this@UploadActivity)
                    .load(data.profile)
                    .into(profileView)
                Glide.with(this@UploadActivity)
                    .load(data.display)
                    .into(displayView)
            }
        }
    }

    private fun onClick(data: ListDomain?) {
        binding.apply {
            uploadImageProfile.setOnClickListener {
                this@UploadActivity.profile = true
                startGallery()
            }
            uploadImageDisplay.setOnClickListener {
                this@UploadActivity.profile = false
                startGallery()
            }
            btnClose.setNavigationOnClickListener {
                finish()
            }
            if(data?.keyId != null){
                btnUpload.text = getString(R.string.update)
                btnRemove.setOnClickListener {
                    lifecycleScope.launchWhenCreated {
                        uploadViewModel.removeData(data.keyId)?.observe(this@UploadActivity){
                            when(it.status){
                                Status.SUCCESS -> {
                                    Toast.makeText(this@UploadActivity, it.data, Toast.LENGTH_LONG).show()
                                    finish()
                                }
                                Status.ERROR -> {
                                    Toast.makeText(this@UploadActivity, it.message, Toast.LENGTH_LONG).show()
                                }
                                Status.LOADING -> {
                                    Toast.makeText(this@UploadActivity, "loading", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }else{
                btnUpload.text = getString(R.string.upload)
                btnRemove.visibility = View.GONE
            }
            btnUpload.setOnClickListener {
                val nama: String = editTextName.text.toString().trim()
                val umur = editTextUmur.text.toString().trim()
                val tempatDiutus = editTextTempatDiutus.text.toString().trim()
                val kisah = editTextKisah.getMD()
                val profile = getFileProfile
                val display = getFIleDisplay
                val tag = editTextTag.text.toString().trim()
                if(data?.keyId != null){
                    data.keyId?.let { it1 ->
                        uploadData(nama, umur, tempatDiutus, kisah, profile, display,it1, false, tag)

                    }
                }else{
                    val formatter = SimpleDateFormat("yyyyMMddHHmmSS", Locale.getDefault())
                    val date = Date()
                    val id = formatter.format(date)
                    uploadData(nama, umur, tempatDiutus, kisah, profile, display, id, false, tag)

                }

            }
        }
    }

    private fun uploadData(
        nama: String? = null,
        umur: String? = null,
        tempatDiutus: String? = null,
        kisah: String? = null,
        profile: File? = null,
        display: File? = null,
        id: String,
        recentActivity: Boolean,
        tag: String
    ) {
        uploadViewModel.postDataNabi(nama, umur, tempatDiutus, kisah, id, profile, display, recentActivity, tag).observe(this@UploadActivity){
            when(it.status){
                Status.LOADING -> {
                    dialogLoading.show()
                }
                Status.ERROR -> {
                    dialogLoading.dismiss()
                    Log.e("error", "onClick: ${it.message} \nTry Again" )
                    dialogError(it.message,this).show()
                }
                Status.SUCCESS -> {
                    dialogLoading.dismiss()
                    dialogSuccess(this).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    },2000)
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri? = it.data?.data
            val file: File? = selectedImage?.let { it1 -> uriToFile(it1, this@UploadActivity) }
            if(profile){
                binding.profileView.setImageURI(selectedImage)
                getFileProfile = file
            }else{
                binding.displayView.setImageURI(selectedImage)
                getFIleDisplay = file
            }
        }else{
            getFileProfile = null
            getFIleDisplay = null
        }
    }
}