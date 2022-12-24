package com.elthobhy.islamicstory.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.core.utils.dialogError
import com.elthobhy.islamicstory.core.utils.dialogLoading
import com.elthobhy.islamicstory.core.utils.dialogSuccess
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityForgotPasswordBinding
import com.elthobhy.islamicstory.login.LoginActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.android.inject

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val forgotPasswordViewModel by inject<ForgotPasswordViewModel>()
    private lateinit var dialogLoading: AlertDialog
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogLoading = dialogLoading(this)
        onClick()

        MobileAds.initialize(this) {}
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun onClick() {
        binding.apply {
            btnForgotPassword.setOnClickListener {
                val email = etEmailForgotPassword.text.toString().trim()
                if(checkValidation(email)){
                    forgotPassword(email)
                }
            }
            btnCloseForgotPassword.setOnClickListener {
                finish()
            }
        }
    }

    private fun forgotPassword(email: String) {
        forgotPasswordViewModel.forgotPassword(email).observe(this){
            when (it.status) {
                Status.LOADING -> {
                    dialogLoading.show()
                }
                Status.SUCCESS -> {
                    dialogLoading.dismiss()
                    dialogSuccess(this@ForgotPasswordActivity).show()
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    finishAffinity()
                }
                Status.ERROR -> {
                    dialogLoading.dismiss()
                    dialogError(it.message,this@ForgotPasswordActivity).show()
                }
            }
        }
    }

    private fun checkValidation(email: String): Boolean {
        binding.apply {
            when{
                email.isEmpty()->{
                    etEmailForgotPassword.error = "Please Field Your Email"
                    etEmailForgotPassword.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                    etEmailForgotPassword.error = "Pleas Field Correct Email address"
                    etEmailForgotPassword.requestFocus()
                }
                else->{
                    return true
                }
            }
        }
        return false
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