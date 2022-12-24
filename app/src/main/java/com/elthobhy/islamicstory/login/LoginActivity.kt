package com.elthobhy.islamicstory.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.core.R
import com.elthobhy.islamicstory.core.utils.dialogError
import com.elthobhy.islamicstory.core.utils.dialogLoading
import com.elthobhy.islamicstory.core.utils.dialogSuccess
import com.elthobhy.islamicstory.core.utils.showDialogAnimation
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityLoginBinding
import com.elthobhy.islamicstory.forgotpassword.ForgotPasswordActivity
import com.elthobhy.islamicstory.main.MainActivity
import com.elthobhy.islamicstory.register.RegisterActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
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
    private lateinit var dialogLoading: AlertDialog
    private lateinit var mAdView: AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogLoading = dialogLoading(this)
        initGoogleSignIn()
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

    private var resultLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                            Status.LOADING -> {
                                dialogLoading.show()
                            }
                            Status.SUCCESS -> {
                                dialogLoading.dismiss()
                                dialogSuccess(this).show()
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finishAffinity()
                            }
                            Status.ERROR -> {
                                dialogLoading.dismiss()
                                dialogError(it.message,this).show()
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                showDialogAnimation(
                    context = this,
                    message = e.message,
                    state = "Error",
                    animation = "bedug.json").show()
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
                            Status.LOADING -> {
                                dialogLoading.show()
                            }
                            Status.SUCCESS -> {
                                dialogSuccess(this@LoginActivity).show()
                                dialogLoading.dismiss()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                            Status.ERROR -> {
                                dialogError(it.message,this@LoginActivity).show()
                                dialogLoading.dismiss()
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