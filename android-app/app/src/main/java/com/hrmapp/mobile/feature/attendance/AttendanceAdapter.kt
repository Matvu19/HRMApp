package com.hrmapp.mobile.feature.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrmapp.mobile.core.network.AttendanceEventItem
import com.hrmapp.mobile.databinding.ItemAttendanceBinding

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    private val items = mutableListOf<AttendanceEventItem>()

    fun submitList(newItems: List<AttendanceEventItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemAttendanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AttendanceEventItem) {
            binding.tvAttendanceType.text =
                if (item.eventType == "CHECK_IN") "Chấm công vào" else "Chấm công ra"
            binding.tvAttendanceTime.text = item.eventTimeServer
            binding.tvAttendanceStatus.text = item.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(
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