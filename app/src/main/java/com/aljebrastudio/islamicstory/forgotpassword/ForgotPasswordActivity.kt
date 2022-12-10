package com.aljebrastudio.islamicstory.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityForgotPasswordBinding
import com.aljebrastudio.islamicstory.login.LoginActivity
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
                Status.SUCCESS -> {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Reset Password Link has been sent, please check your inbox",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(
                        Intent(
                            this@ForgotPasswordActivity,
                            LoginActivity::class.java
                        )
                    )
                    finishAffinity()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
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