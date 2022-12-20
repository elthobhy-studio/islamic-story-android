package com.elthobhy.islamicstory.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.databinding.ItemListNabiBinding
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.core.utils.DataListObject
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityMainBinding
import com.elthobhy.islamicstory.databinding.LayoutDialogAllStoriesBinding
import com.elthobhy.islamicstory.detail.DetailActivity
import com.elthobhy.islamicstory.listdata.ListDataActivity
import com.elthobhy.islamicstory.listdata.ListViewModel
import com.elthobhy.islamicstory.search.SearchActivity
import com.elthobhy.islamicstory.upload.UploadActivity
import com.elthobhy.islamicstory.user.UserActivity
import com.elthobhy.islamicstory.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by inject<UserViewModel>()
    private var firebaseUser: FirebaseUser? = null
    private val listViewModel by inject<ListViewModel>()
    private lateinit var adapterList: AdapterList
    private lateinit var searchView: MaterialSearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchView = binding.searchView
        firebaseUser = FirebaseAuth.getInstance().currentUser
        adapterList = AdapterList()
        setUpActionBar()
        getDataUser()
        onClick()
        binding.floatingAction.visibility = View.VISIBLE
        setList()
        setUpRv()
        setUpImageSlider()
    }

    private fun setUpActionBar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.title = ""
        }
        binding.apply {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if(abs(verticalOffset) -appBarLayout.totalScrollRange == 0){
                    toolbar.visibility = View.VISIBLE
                    searchView.visibility = View.VISIBLE
                    tvRecommended.visibility = View.GONE
                }else{
                    toolbar.visibility = View.GONE
                    searchView.visibility = View.GONE
                    tvRecommended.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        searchView.setBackgroundResource(R.drawable.bg_edit_text)
        return true
    }

    private fun setUpImageSlider() {
        val imageSlider = binding.imageSlider
        val imageList = ArrayList<SlideModel>()
        DataListObject.nabiGambar.map {
            imageList.add(SlideModel(it))
        }
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }

    private fun setList() {
        listViewModel.getData().observe(this){
            when(it.status){
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    adapterList.submitList(it.data)
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
                override fun onItemClicked(data: ListDomain, binding: ItemListNabiBinding) {
                    setDetail(data, binding)
                }

            })
        }
    }

    internal fun setDetail(data: ListDomain, binding: ItemListNabiBinding) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(Constants.DATA, data)
        val optionCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity ,
                binding.imageCard, "imageDisplay"
            )
        startActivity(intent, optionCompat.toBundle())
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
            iconQishasulAnbiya.setOnClickListener {
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity ,
                        Pair(binding.iconQishasulAnbiya, "iconQishasulAnbiya")
                    )
                val intent = Intent(this@MainActivity, ListDataActivity::class.java)
                intent.putExtra(Constants.REFERENCE, Constants.NABI)
                startActivity(intent, optionCompat.toBundle())
            }

            iconShirahNabawiyah.setOnClickListener {
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity ,
                        Pair(binding.iconShirahNabawiyah, "iconShirahNabawiyah")
                    )
                val intent = Intent(this@MainActivity, ListDataActivity::class.java)
                intent.putExtra(Constants.REFERENCE, Constants.SHIRAH)
                startActivity(intent, optionCompat.toBundle())
            }
            iconKhalifah.setOnClickListener {
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity ,
                        Pair(binding.iconKhalifah, "iconKhalifah")
                    )
                val intent = Intent(this@MainActivity, ListDataActivity::class.java)
                intent.putExtra(Constants.REFERENCE, Constants.KHALIFAH)
                startActivity(intent, optionCompat.toBundle())
            }
            allStories.setOnClickListener {
                showDialogForm()
            }
        }
    }

    private fun showDialogForm() {
        val dialogBinding = LayoutDialogAllStoriesBinding.inflate(layoutInflater)
        val alert = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
        alert.show().window?.decorView?.setBackgroundResource(android.R.color.transparent)
    }
}