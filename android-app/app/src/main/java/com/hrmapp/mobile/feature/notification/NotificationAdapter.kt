package com.hrmapp.mobile.feature.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrmapp.mobile.core.network.NotificationItem
import com.hrmapp.mobile.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val onClick: (NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val items = mutableListOf<NotificationItem>()

    fun submitList(newItems: List<NotificationItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationItem) {
            binding.tvTitle.text = item.title
            binding.tvBody.text = item.messageText
            binding.tvDeeplink.text = item.deeplinkUrl
            binding.tvStatus.text = if (item.readAt == null) "Chưa đọc" else "Đã đọc"
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
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