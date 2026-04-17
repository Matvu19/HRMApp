package com.hrmapp.mobile.feature.approval

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentApprovalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApprovalFragment : Fragment(R.layout.fragment_approval) {

    private var _binding: FragmentApprovalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApprovalViewModel by viewModels()
    private lateinit var adapter: ApprovalAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentApprovalBinding.bind(view)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter = ApprovalAdapter { item ->
            viewModel.select(item)
        }

        binding.rvApprovals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvApprovals.adapter = adapter

        binding.btnRefreshApproval.setOnClickListener {
            viewModel.load()
        }

        binding.btnApprove.setOnClickListener {
            viewModel.action("APPROVED", "Duyệt từ ứng dụng Android")
        }

        binding.btnReject.setOnClickListener {
            viewModel.action("REJECTED", "Từ chối từ ứng dụng Android")
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.items)
            binding.tvApprovalMessage.text = state.message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}