package com.elthobhy.islamicstory.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.core.utils.dialogError
import com.elthobhy.islamicstory.core.utils.dialogLoading
import com.elthobhy.islamicstory.core.utils.dialogSuccess
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityForgotPasswordBinding
import com.elthobhy.islamicstory.login.LoginActivity
import org.koin.android.ext.android.inject

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val forgotPasswordViewModel by inject<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClick()
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
                    dialogLoading(this).show()
                }
                Status.SUCCESS -> {
                    dialogLoading(this).dismiss()
                    dialogSuccess(this).show()
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    finishAffinity()
                }
                Status.ERROR -> {
                    dialogLoading(this).dismiss()
                    dialogError(it.message,this).show()
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
}