package com.elthobhy.islamicstory.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.databinding.ActivitySplashBinding
import com.elthobhy.islamicstory.login.LoginActivity
import com.elthobhy.islamicstory.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        afterDelayGoto()
    }

    private fun afterDelayGoto() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuth()
        }, DELAY)
    }

    private fun checkAuth() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    companion object {
        const val DELAY: Long = 2000
    }
}