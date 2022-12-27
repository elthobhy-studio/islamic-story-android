package com.elthobhy.islamicstory.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.databinding.ActivitySplashBinding
import com.elthobhy.islamicstory.login.LoginActivity
import com.elthobhy.islamicstory.main.MainActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var mInterstitialAd: InterstitialAd? = null
    private var adIsLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        afterDelayGoto()
        loadInterstitial(this)

    }

    fun loadInterstitial(activity: Activity) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            Constants.AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.e("Error", it) }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e("success", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    adIsLoading = false
                }
            })

    }

    fun showInterstitial(activity: Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("dismiss fullscreen", "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mInterstitialAd = null
                        loadInterstitial(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d("fail Show full screen", "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("ads has showed", "Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }
            mInterstitialAd?.show(activity)
        } else {
            Toast.makeText(activity, "Ad wasn't loaded.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun afterDelayGoto() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuth()
        }, DELAY)
    }

    private fun checkAuth() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            showInterstitial(this)
            finishAffinity()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            showInterstitial(this)
            finishAffinity()
        }
    }

    companion object {
        const val DELAY: Long = 2000
    }
}