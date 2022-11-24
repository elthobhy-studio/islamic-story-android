package com.aljebrastudio.islamicstory.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityLoginBinding
import com.aljebrastudio.islamicstory.main.MainActivity
import com.aljebrastudio.islamicstory.register.RegisterActivity
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by inject<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClick()

    }

    private fun onClick() {
        binding.apply {
            buttonLogin.setOnClickListener {
                val email = editEmail.text.toString().trim()
                val pass = editPassword.text.toString().trim()
                if(validationCheck(email, pass)){
                    loginViewModel.login(email, pass).observe(this@LoginActivity){
                        when(it.status){
                            Status.SUCCESS -> {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                            Status.LOADING -> {}
                            Status.ERROR -> {
                                Toast.makeText(
                                    this@LoginActivity,
                                    it.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
            register.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun validationCheck(email: String, pass: String): Boolean {
        binding.apply {
            when{
                email.isEmpty()->{
                    editEmail.error = "please Field Your Email"
                    editEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                    editEmail.error = "Please Use Valid Email"
                    editEmail.requestFocus()
                }
                pass.isEmpty()->{
                    editPassword.error = "please Field your password"
                    editPassword.requestFocus()
                }
                else->{
                    return true
                }
            }
        }
        return false
    }
}