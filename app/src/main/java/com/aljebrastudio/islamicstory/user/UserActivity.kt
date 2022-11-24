package com.aljebrastudio.islamicstory.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityUserBinding
import com.aljebrastudio.islamicstory.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.android.inject

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val userViewModel by inject<UserViewModel>()
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        getDataUser()
        onClick()
    }

    private fun getDataUser() {
        binding.apply {
            val uid = firebaseUser?.uid
            if (uid != null) {
                userViewModel.getDataUser(uid).observe(this@UserActivity){
                    when(it.status){
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                                tvNameUser.text = it.data?.nameUser
                                Glide.with(this@UserActivity)
                                    .load(it.data?.avatarUser)
                                    .placeholder(android.R.color.darker_gray)
                                    .into(ivUser)
                                tvEmailUser.text = it.data?.emailUser
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.apply {
            btnClose.setOnClickListener {
                finish()
            }
            buttonChangePassword.setOnClickListener {

            }
            btnLogoutUser.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@UserActivity, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }
}