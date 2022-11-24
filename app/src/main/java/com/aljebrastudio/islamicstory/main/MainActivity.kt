package com.aljebrastudio.islamicstory.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aljebrastudio.islamicstory.core.domain.model.User
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityMainBinding
import com.aljebrastudio.islamicstory.upload.UploadActivity
import com.aljebrastudio.islamicstory.user.UserActivity
import com.aljebrastudio.islamicstory.user.UserViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by inject<UserViewModel>()
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        getDataUser()
        onClick()
    }

    private fun getDataUser() {
        val uid = firebaseUser?.uid
        if (uid != null) {
            userViewModel.getDataUser(uid).observe(this){
                when(it.status){
                    Status.SUCCESS -> {
                        binding.apply {
                            tvNameUser.text = it.data?.nameUser
                            Glide.with(this@MainActivity)
                                .load(it.data?.avatarUser)
                                .placeholder(android.R.color.darker_gray)
                                .into(ivUser)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            this,
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun onClick() {
        binding.apply {
            floatingAction.setOnClickListener {
                startActivity(Intent(this@MainActivity, UploadActivity::class.java))
            }
            ivUser.setOnClickListener {
                startActivity(Intent(this@MainActivity, UserActivity::class.java))
            }
        }
    }
}