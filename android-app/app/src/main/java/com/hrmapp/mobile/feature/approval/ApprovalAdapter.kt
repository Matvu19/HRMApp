package com.hrmapp.mobile.feature.approval

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrmapp.mobile.core.network.ApprovalStepItem
import com.hrmapp.mobile.databinding.ItemApprovalBinding

class ApprovalAdapter(
    private val onClick: (ApprovalStepItem) -> Unit
) : RecyclerView.Adapter<ApprovalAdapter.ViewHolder>() {

    private val items = mutableListOf<ApprovalStepItem>()

    fun submitList(newItems: List<ApprovalStepItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemApprovalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ApprovalStepItem) {
            binding.tvApprovalTitle.text = "Yêu cầu #${item.approvalStepId}"
            binding.tvApprovalSubtitle.text = "Bước ${item.stepNo} • ${item.approverRoleCode}"
            binding.tvApprovalStatus.text = when (item.decision ?: item.status) {
                "APPROVED" -> "Đã duyệt"
                "REJECTED" -> "Từ chối"
                else -> "Chờ duyệt"
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemApprovalBinding.inflate(
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