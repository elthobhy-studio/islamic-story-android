package com.elthobhy.islamicstory.listdata

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
import com.elthobhy.islamicstory.core.utils.dialogError
import com.elthobhy.islamicstory.core.utils.vo.Status
import com.elthobhy.islamicstory.databinding.ActivityListDataBinding
import com.elthobhy.islamicstory.detail.DetailActivity
import com.elthobhy.islamicstory.search.SearchViewModel
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject

@FlowPreview
@ExperimentalCoroutinesApi
class ListDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDataBinding
    private val listViewModel by inject<ListViewModel>()
    internal val searchViewModel by inject<SearchViewModel>()
    private lateinit var adapterList: AdapterList
    private lateinit var searchView: MaterialSearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDataBinding.inflate(layoutInflater)
        adapterList = AdapterList()
        searchView = binding.searchView
        setContentView(binding.root)
        setUpActionBar()
        setContentData()
        setUpRv()
    }

    private fun setContentData() {
        when(intent.getStringExtra(Constants.REFERENCE)){
            Constants.NABI ->{
                binding.imageNabiDanRasul.setImageResource(R.mipmap.qishasul_anbiya_image)
                binding.imageNabiDanRasul.transitionName = "iconQishasulAnbiya"
                setList()
                searchList()
            }
            Constants.KHALIFAH ->{
                binding.imageNabiDanRasul.setImageResource(R.mipmap.khalifah_large_image)
                binding.imageNabiDanRasul.transitionName = "iconKhalifah"
            }
            Constants.SHIRAH ->{
                binding.imageNabiDanRasul.setImageResource(R.mipmap.shirah_nabawiyah_image)
                binding.imageNabiDanRasul.transitionName = "iconShirahNabawiyah"
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        searchView.setBackgroundResource(R.drawable.bg_search)
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.equals("") == false) {
                    searchViewModel.queryChannel.value = newText
                    binding.rvList.visibility =View.VISIBLE
                }else{
                    binding.shimmerList.visibility = View.VISIBLE
                    binding.rvList.visibility =View.INVISIBLE
                }
                return true
            }
        })
        return true
    }

    private fun searchList() {
        searchViewModel.searchResult.observe(this){
            if(it.isNotEmpty()){
                adapterList.submitList(it)
                binding.shimmerList.visibility = View.GONE
            }else{
                binding.shimmerList.visibility = View.VISIBLE
                binding.rvList.visibility = View.INVISIBLE
            }
        }
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener{
            override fun onSearchViewShown() {}

            override fun onSearchViewClosed() {
                binding.shimmerList.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
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
                Status.LOADING -> {
                    binding.shimmerList.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {
                    adapterList.submitList(it.data)
                    binding.shimmerList.visibility = View.GONE
                }
                Status.ERROR -> {
                    dialogError(it.message,this).show()
                    binding.shimmerList.visibility = View.GONE
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
                    data.keyId?.let { setStatusRecent(data, it) }
                }

            })
        }
    }
    internal fun setStatusRecent(data: ListDomain?, keyId: String) {
        if (data != null) {
            listViewModel.setRecentActivity(data, keyId)
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