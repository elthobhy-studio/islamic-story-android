package com.elthobhy.islamicstory.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityMainBinding
import com.elthobhy.islamicstory.detail.DetailActivity
import com.elthobhy.islamicstory.listdata.ListDataActivity
import com.elthobhy.islamicstory.search.SearchActivity
import com.elthobhy.islamicstory.upload.UploadActivity
import com.elthobhy.islamicstory.user.UserActivity
import com.elthobhy.islamicstory.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by inject<UserViewModel>()
    private var firebaseUser: FirebaseUser? = null
    private lateinit var adapterList: AdapterList
    private val listViewModel by inject<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        adapterList = AdapterList()
        getDataUser()
        onClick()
        setList()
        setUpRv()
        binding.floatingAction.visibility = View.VISIBLE
        binding.searchView.setBackgroundResource(R.drawable.bg_edit_text)
    }

    private fun setList() {
        listViewModel.getData().observe(this@MainActivity){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    adapterList.submitList(it.data)
                    Log.e("data", "setList: ${it.data}" )
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.e("fail_showList", "setList: ${it.message}" )
                }
            }
        }
    }

    private fun setUpRv() {
        binding.rvListNabi.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = adapterList
            adapterList.setOnItemClickCallback(object: AdapterList.OnItemClickCallback{
                override fun onItemClicked(data: ListDomain) {
                    setDetail(data)
                }

            })
        }
    }

    internal fun setDetail(data: ListDomain) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(Constants.DATA, data)
        startActivity(intent)
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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun onClick() {
        binding.apply {
            floatingAction.setOnClickListener {
                startActivity(Intent(this@MainActivity, UploadActivity::class.java))
            }
            ivUser.setOnClickListener {
                startActivity(Intent(this@MainActivity, UserActivity::class.java))
            }

            searchView.isFocusable = false
            searchView.isClickable = true
            searchView.setOnClickListener {
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity ,
                        binding.searchView, "searchAnimation"
                    )
                startActivity(Intent(this@MainActivity, SearchActivity::class.java), optionCompat.toBundle())
            }
        }
    }
}