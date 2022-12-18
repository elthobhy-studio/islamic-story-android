package com.elthobhy.islamicstory.search

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.databinding.ActivitySearchBinding
import com.elthobhy.islamicstory.detail.DetailActivity
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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        searchView.setBackgroundResource(R.drawable.bg_edit_text)
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchViewModel.queryChannel.value = newText
                }
                return true
            }
        })
        return true
    }

    internal fun searchList() {
        searchViewModel.searchResult.observe(this){
            adapterList.submitList(it)
            Log.e("cut", "searchList: $it")
        }
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener{
            override fun onSearchViewShown() {}

            override fun onSearchViewClosed() {
                setList()
            }
        })
    }

    internal fun setList() {
        searchViewModel.searchResult.observe(this){
            adapterList.submitList(it)
        }
    }

    private fun showRv() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = adapterList
        }
        adapterList.setOnItemClickCallback(object : AdapterList.OnItemClickCallback{
            override fun onItemClicked(data: ListDomain) {
                setDetail(data)
            }
        })
    }


    internal fun setDetail(data: ListDomain) {
        val intent = Intent(this@SearchActivity, DetailActivity::class.java)
        intent.putExtra(Constants.DATA, data)
        startActivity(intent)
    }
}