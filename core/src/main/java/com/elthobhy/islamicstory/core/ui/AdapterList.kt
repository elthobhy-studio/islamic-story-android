package com.elthobhy.islamicstory.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elthobhy.islamicstory.core.R
import com.elthobhy.islamicstory.core.databinding.ItemListNabiBinding
import com.elthobhy.islamicstory.core.domain.model.ListDomain

class AdapterList: ListAdapter<ListDomain, AdapterList.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListDomain, binding: ItemListNabiBinding)
    }

    inner class ViewHolder(private val binding: ItemListNabiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListDomain) {
            binding.apply {
                tvName.text = item.name
                if(item.profile == null || item.display == null || item.umat.isNullOrEmpty()){
                    imageCard.visibility = View.GONE
                    imageProfileCard.visibility = View.GONE
                    tvDescription.visibility = View.GONE
                }else{
                    Glide.with(itemView)
                        .load(item.profile)
                        .override(800, 600)
                        .into(profilePhoto)
                    Glide.with(itemView.context)
                        .load(item.display)
                        .into(image)
                    val text = String.format("${itemView.resources.getString(R.string.tempat_diutus)} %1$1s", item.umat)
                    tvDescription.text = text
                }

                item.detail?.let { textStory.renderMD(it) }

                view.setOnClickListener {
                    onItemClickCallback.onItemClicked(item, binding)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListNabiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListDomain>() {
            override fun areItemsTheSame(oldItem: ListDomain, newItem: ListDomain): Boolean {
                return oldItem.keyId == newItem.keyId
            }

            override fun areContentsTheSame(oldItem: ListDomain, newItem: ListDomain): Boolean {
                return oldItem == newItem
            }
        }
    }
}