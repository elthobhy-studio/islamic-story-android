package com.aljebrastudio.islamicstory.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityRegisterBinding
import com.aljebrastudio.islamicstory.login.LoginActivity
import org.koin.android.ext.android.inject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by inject<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnRegister.setOnClickListener {
                val email = editTextEmail.text.toString().trim()
                val pass = editTextPassword.text.toString().trim()
                val confirmPass = editTextConfirmPassword.text.toString().trim()

                if(checkValidation(email, pass, confirmPass)){
                    registerViewModel.register(email, pass).observe(this@RegisterActivity){
                        when(it.status){
                            Status.SUCCESS -> {
                                binding.pbRegister.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "User Created",
                                    Toast.LENGTH_LONG
                                ).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finishAffinity()
                            }
                            Status.LOADING -> {
                                binding.pbRegister.visibility = View.VISIBLE
                            }
                            Status.ERROR -> {
                                binding.pbRegister.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity,
                                    it.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
            btnCloseRegister.setOnClickListener {
                finish()
            }
        }
    }

    private fun checkValidation(email: String, pass: String, confirmPass: String): Boolean {
        binding.apply {
            when{
                email.isEmpty()->{
                    editTextEmail.error = "Please Field Your Email"
                    editTextEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                    editTextEmail.error = "Please Use Valid Email"
                    editTextEmail.requestFocus()
                }
                pass.isEmpty()->{
                    editTextPassword.error = "Please Field Your Password"
                    editTextPassword.requestFocus()
                }
                confirmPass.isEmpty()->{
                    editTextConfirmPassword.error = "Please Field Your Confirm Password"
                    editTextConfirmPassword.requestFocus()
                }
                pass != confirmPass ->{
                    editTextPassword.error = "Your confirm password didn't match with password"
                    editTextConfirmPassword.error = "Your confirm password didn't match with password"
                    editTextPassword.requestFocus()
                    editTextConfirmPassword.requestFocus()
                }
                else->{
                    return true
                }
            }
        }
        return false
    }
}