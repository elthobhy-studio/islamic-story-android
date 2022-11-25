package com.aljebrastudio.islamicstory.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aljebrastudio.islamicstory.core.R
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityLoginBinding
import com.aljebrastudio.islamicstory.forgotpassword.ForgotPasswordActivity
import com.aljebrastudio.islamicstory.main.MainActivity
import com.aljebrastudio.islamicstory.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by inject<LoginViewModel>()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGoogleSignIn()
        onClick()

    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private var resultLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    val name = account.displayName
                    val email = account.email
                    if (name != null && email != null) {
                        loginViewModel.loginWithGoogle(name, email, credential).observe(this) {
                            when (it.status) {
                                Status.SUCCESS -> {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                    )
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
                } catch (e: ApiException) {
                    Toast.makeText(
                        this@LoginActivity,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    private fun onClick() {
        binding.apply {
            buttonLogin.setOnClickListener {
                val email = editEmail.text.toString().trim()
                val pass = editPassword.text.toString().trim()
                if (validationCheck(email, pass)) {
                    loginViewModel.login(email, pass).observe(this@LoginActivity) {
                        when (it.status) {
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
            buttonGoogle.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                resultLaunch.launch(signInIntent)
            }
            forgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
        }
    }

    private fun validationCheck(email: String, pass: String): Boolean {
        binding.apply {
            when {
                email.isEmpty() -> {
                    editEmail.error = "please Field Your Email"
                    editEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    editEmail.error = "Please Use Valid Email"
                    editEmail.requestFocus()
                }
                pass.isEmpty() -> {
                    editPassword.error = "please Field your password"
                    editPassword.requestFocus()
                }
                else -> {
                    return true
                }
            }
        }
        return false
    }
}