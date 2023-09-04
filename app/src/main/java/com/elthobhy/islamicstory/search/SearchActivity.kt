package com.elthobhy.islamicstory.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.databinding.ItemListNabiBinding
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.databinding.ActivitySearchBinding
import com.elthobhy.islamicstory.detail.DetailActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject

@FlowPreview
@ExperimentalCoroutinesApi
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    internal val searchViewModel by inject<SearchViewModel>()
    private lateinit var adapterList: AdapterList
    private lateinit var searchView: MaterialSearchView
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        adapterList = AdapterList()
        setContentView(binding.root)

        searchView = binding.searchView
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        showRv()
        searchList()

        MobileAds.initialize(this) {}
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.requestFocusFromTouch()
        searchView.setMenuItem(item)
        searchView.setBackgroundResource(R.drawable.bg_edit_text)
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.equals("") == false) {
                    searchViewModel.queryChannel.value = newText
                    binding.rvSearch.visibility =View.VISIBLE
                    binding.empty.visibility = View.GONE
                }else{
                    binding.shimmerList.visibility = View.VISIBLE
                    binding.rvSearch.visibility =View.INVISIBLE
                    binding.empty.visibility = View.GONE
                }
                return true
            }
        })
        return true
    }

    private fun searchList() {
        searchViewModel.searchResult().observe(this){
            if(it.isNotEmpty()){
                adapterList.submitList(it)
                binding.shimmerList.visibility = View.GONE
                binding.empty.visibility = View.GONE
            }else{
                binding.empty.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.INVISIBLE
                binding.shimmerList.visibility = View.GONE
            }
        }
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener{
            override fun onSearchViewShown() {}

            override fun onSearchViewClosed() {
                binding.shimmerList.visibility = View.GONE
                binding.empty.visibility = View.VISIBLE
            }
        })
    }

    private fun showRv() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = adapterList
        }
        adapterList.setOnItemClickCallback(object : AdapterList.OnItemClickCallback{
            override fun onItemClicked(data: ListDomain, binding: ItemListNabiBinding) {
                setDetail(data, binding)
            }
        })
    }


    internal fun setDetail(data: ListDomain, binding: ItemListNabiBinding) {
        val intent = Intent(this@SearchActivity, DetailActivity::class.java)
        intent.putExtra(Constants.DATA, data)
        val optionCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@SearchActivity ,
                binding.imageCard, "imageDisplay"
            )
        startActivity(intent, optionCompat.toBundle())
    }
}