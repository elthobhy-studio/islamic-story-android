package com.aljebrastudio.islamicstory.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain
import com.aljebrastudio.islamicstory.core.utils.Constants
import com.aljebrastudio.islamicstory.core.utils.vo.Status
import com.aljebrastudio.islamicstory.databinding.ActivityMainBinding
import com.aljebrastudio.islamicstory.databinding.ItemListNabiBinding
import com.aljebrastudio.islamicstory.detail.DetailActivity
import com.aljebrastudio.islamicstory.upload.UploadActivity
import com.aljebrastudio.islamicstory.user.UserActivity
import com.aljebrastudio.islamicstory.user.UserViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by inject<UserViewModel>()
    private var firebaseUser: FirebaseUser? = null
    private val storyAdapter by inject<AdapterList>()
    private val listViewModel by inject<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        getDataUser()
        onClick()
        setList()
        setUpRv()
        binding.floatingAction.visibility = View.VISIBLE
    }

    private fun setList() {
        listViewModel.getData().observe(this){
            storyAdapter.submitList(it)
        }
    }

    private fun setUpRv() {
        binding.rvListNabi.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = storyAdapter
            storyAdapter.setOnItemClickCallback(object: AdapterList.OnItemClickCallback{
                override fun onItemClicked(data: ListDomain, binding: ItemListNabiBinding) {
                    setDetail(data, binding)
                }

            })
        }
    }

    internal fun setDetail(data: ListDomain, binding: ItemListNabiBinding) {
        binding.apply {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(Constants.DATA, data)
            startActivity(intent)
        }
    }

    private fun getDataUser() {
        val uid = firebaseUser?.uid
        if (uid != null) {
            userViewModel.getDataUser(uid).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.apply {
                            tvNameUser.text = it.data?.nameUser
                            Glide.with(this@MainActivity)
                                .load(it.data?.avatarUser)
                                .placeholder(android.R.color.darker_gray)
                                .into(ivUser)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            this,
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun onClick() {
        binding.apply {
            floatingAction.setOnClickListener {
                startActivity(Intent(this@MainActivity, UploadActivity::class.java))
            }
            ivUser.setOnClickListener {
                startActivity(Intent(this@MainActivity, UserActivity::class.java))
            }
        }
    }
}