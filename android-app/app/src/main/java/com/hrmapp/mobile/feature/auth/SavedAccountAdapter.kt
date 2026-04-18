package com.hrmapp.mobile.feature.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrmapp.mobile.core.storage.SavedAccount
import com.hrmapp.mobile.databinding.ItemSavedAccountBinding

class SavedAccountAdapter(
    private val onUseClick: (SavedAccount) -> Unit,
    private val onRemoveClick: (SavedAccount) -> Unit
) : RecyclerView.Adapter<SavedAccountAdapter.ViewHolder>() {

    private val items = mutableListOf<SavedAccount>()

    fun submitList(newItems: List<SavedAccount>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemSavedAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SavedAccount) {
            binding.tvSavedUsername.text = item.username
            binding.tvSavedRole.text = item.roleCode

            binding.btnUseAccount.setOnClickListener {
                onUseClick(item)
            }

            binding.btnRemoveAccount.setOnClickListener {
                onRemoveClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}