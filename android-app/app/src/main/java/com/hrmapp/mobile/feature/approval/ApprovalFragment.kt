package com.hrmapp.mobile.feature.approval

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentApprovalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment(R.layout.fragment_approval) {

    private var _binding: FragmentApprovalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApprovalViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentApprovalBinding.bind(view)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.load(2L)

        binding.btnApprove.setOnClickListener {
            val item = viewModel.uiState.value?.item ?: return@setOnClickListener
            viewModel.action(item.approvalStepId, "APPROVED", "Duyệt từ ứng dụng Android")
        }

        binding.btnReject.setOnClickListener {
            val item = viewModel.uiState.value?.item ?: return@setOnClickListener
            viewModel.action(item.approvalStepId, "REJECTED", "Từ chối từ ứng dụng Android")
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val item = state.item
            if (item != null) {
                binding.tvApprovalTitle.text = "Yêu cầu phê duyệt #${item.approvalStepId}"
                binding.tvApprovalSubtitle.text = "Bước ${item.stepNo} • ${item.approverRoleCode}"
                binding.tvApprovalStatus.text = when (item.decision ?: item.status) {
                    "APPROVED" -> "Đã duyệt"
                    "REJECTED" -> "Từ chối"
                    else -> "Chờ duyệt"
                }
            } else if (state.message.isNotBlank()) {
                binding.tvApprovalStatus.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}