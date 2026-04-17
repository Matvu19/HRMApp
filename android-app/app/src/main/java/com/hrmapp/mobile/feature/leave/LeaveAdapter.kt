package com.hrmapp.mobile.feature.leave

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrmapp.mobile.core.network.LeaveItem
import com.hrmapp.mobile.databinding.ItemLeaveBinding

class LeaveAdapter : RecyclerView.Adapter<LeaveAdapter.ViewHolder>() {

    private val items = mutableListOf<LeaveItem>()

    fun submitList(newItems: List<LeaveItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemLeaveBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LeaveItem) {
            binding.tvLeaveTitle.text = "Đơn nghỉ #${item.leaveRequestId}"
            binding.tvLeaveDate.text = "${item.dateFrom} đến ${item.dateTo}"
            binding.tvLeaveReason.text = item.reasonText ?: ""
            binding.tvLeaveStatus.text = item.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaveBinding.inflate(
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