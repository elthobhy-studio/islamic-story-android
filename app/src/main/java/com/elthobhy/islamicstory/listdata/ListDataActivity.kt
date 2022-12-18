package com.elthobhy.islamicstory.listdata

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elthobhy.islamicstory.R
import com.elthobhy.islamicstory.core.databinding.ItemListNabiBinding
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityListDataBinding
import com.elthobhy.islamicstory.detail.DetailActivity
import com.elthobhy.islamicstory.main.ListViewModel
import com.elthobhy.islamicstory.search.SearchViewModel
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import kotlin.math.abs

@FlowPreview
@ExperimentalCoroutinesApi
class ListDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDataBinding
    private val listViewModel by inject<ListViewModel>()
    private val searchViewModel by inject<SearchViewModel>()
    private lateinit var adapterList: AdapterList
    private lateinit var searchView: MaterialSearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDataBinding.inflate(layoutInflater)
        adapterList = AdapterList()
        searchView = binding.searchView
        setContentView(binding.root)
        setUpActionBar()
        setList()
        setUpRv()
        searchList()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.setMenuItem(item)
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

    private fun searchList() {
        searchViewModel.searchResult.observe(this){
            adapterList.submitList(it)
        }
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener{
            override fun onSearchViewShown() {}

            override fun onSearchViewClosed() {
                setList()
            }
        })
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }


    internal fun setList() {
        listViewModel.getData().observe(this@ListDataActivity){
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
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@ListDataActivity, LinearLayoutManager.VERTICAL, false)
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
        val intent = Intent(this@ListDataActivity, DetailActivity::class.java)
        intent.putExtra(Constants.DATA, data)
        val optionCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@ListDataActivity ,
                binding.imageCard, "imageDisplay"
            )
        startActivity(intent, optionCompat.toBundle())
    }
}