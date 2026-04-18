package com.hrmapp.mobile.feature.payroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrmapp.mobile.core.network.PayrollItem
import com.hrmapp.mobile.databinding.ItemPayrollBinding
import java.text.DecimalFormat

class PayrollAdapter : RecyclerView.Adapter<PayrollAdapter.ViewHolder>() {

    private val items = mutableListOf<PayrollItem>()
    private val formatter = DecimalFormat("#,###")

    fun submitList(newItems: List<PayrollItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemPayrollBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PayrollItem) {
            binding.tvPayrollMonth.text = "Kỳ lương: ${item.payrollMonth}"
            binding.tvGrossSalary.text = "Lương gross: ${formatter.format(item.grossSalary)}"
            binding.tvAllowance.text = "Phụ cấp: ${formatter.format(item.allowanceAmount)}"
            binding.tvDeduction.text = "Khấu trừ: ${formatter.format(item.deductionAmount)}"
            binding.tvNetSalary.text = "Thực lĩnh: ${formatter.format(item.netSalary)}"
            binding.tvPayrollStatus.text = "Trạng thái: ${item.status}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPayrollBinding.inflate(
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