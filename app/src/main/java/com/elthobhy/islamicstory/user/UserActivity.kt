package com.elthobhy.islamicstory.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.elthobhy.islamicstory.changepassword.ChangePasswordActivity
import com.elthobhy.islamicstory.core.R
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityUserBinding
import com.elthobhy.islamicstory.login.LoginActivity
import com.elthobhy.islamicstory.recent.RecentActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.android.inject

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val userViewModel by inject<UserViewModel>()
    private var firebaseUser: FirebaseUser? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        initGoogleSignIn()
        getDataUser()
        onClick()

        MobileAds.initialize(this) {}

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun getDataUser() {
        binding.apply {
            val uid = firebaseUser?.uid
            if (uid != null) {
                userViewModel.getDataUser(uid).observe(this@UserActivity) {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.apply {
                                shimmerUser.visibility = View.VISIBLE
                                buttonRecentActivity.visibility = View.GONE
                                btnLogoutUser.visibility = View.GONE
                                buttonChangePassword.visibility = View.GONE
                                tvNameUser.visibility = View.GONE
                                tvEmailUser.visibility = View.GONE
                            }

                        }
                        Status.SUCCESS -> {
                            tvNameUser.text = it.data?.nameUser
                            Glide.with(this@UserActivity)
                                .load(it.data?.avatarUser)
                                .placeholder(android.R.color.darker_gray)
                                .into(ivUser)
                            tvEmailUser.text = it.data?.emailUser
                            binding.shimmerUser.visibility = View.GONE
                            btnLogoutUser.visibility = View.VISIBLE
                            buttonChangePassword.visibility = View.VISIBLE
                            tvNameUser.visibility = View.VISIBLE
                            tvEmailUser.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            binding.shimmerUser.visibility = View.GONE
                            buttonRecentActivity.visibility = View.VISIBLE
                            btnLogoutUser.visibility = View.VISIBLE
                            buttonChangePassword.visibility = View.VISIBLE
                            tvNameUser.visibility = View.VISIBLE
                            tvEmailUser.visibility = View.VISIBLE
                            Toast.makeText(this@UserActivity, "Logout: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
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
                startActivity(Intent(this@UserActivity, ChangePasswordActivity::class.java))
            }
            btnLogoutUser.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                mGoogleSignInClient.signOut()
                startActivity(Intent(this@UserActivity, LoginActivity::class.java))
                finishAffinity()
            }
            buttonRecentActivity.setOnClickListener {
                startActivity(Intent(this@UserActivity, RecentActivity::class.java))
            }
        }
    }
    // Called when leaving the activity
    public override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }
}