package com.hrmapp.mobile.feature.approval

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.R
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentApprovalBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment(R.layout.fragment_approval) {

    private var _binding: FragmentApprovalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApprovalViewModel by viewModels()
    private lateinit var adapter: ApprovalAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentApprovalBinding.bind(view)

        binding.btnBack.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter = ApprovalAdapter { item ->
            viewModel.select(item)
            Snackbar.make(binding.root, "Đã chọn yêu cầu #${item.approvalStepId}", Snackbar.LENGTH_SHORT).show()
        }

        binding.rvApprovals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvApprovals.adapter = adapter

        binding.btnRefreshApproval.setSafeClickListener {
            viewModel.load()
        }

        binding.btnApprove.setSafeClickListener {
            viewModel.action("APPROVED", "Duyệt từ ứng dụng Android")
        }

        binding.btnReject.setSafeClickListener {
            viewModel.action("REJECTED", "Từ chối từ ứng dụng Android")
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressApproval.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            adapter.submitList(state.items)
            binding.tvApprovalMessage.text = state.message

            val hasSelection = state.selectedItem != null
            binding.btnApprove.isEnabled = hasSelection && !state.isLoading
            binding.btnReject.isEnabled = hasSelection && !state.isLoading
            binding.layoutApprovalActions.visibility =
                if (state.items.isNotEmpty()) View.VISIBLE else View.GONE

            if (!state.isLoading && state.message == "Đã duyệt yêu cầu") {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }

            if (!state.isLoading && state.message == "Đã từ chối yêu cầu") {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}