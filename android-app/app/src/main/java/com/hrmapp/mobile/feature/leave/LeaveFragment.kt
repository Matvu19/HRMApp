package com.hrmapp.mobile.feature.leave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentLeaveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaveFragment : Fragment() {

    private var _binding: FragmentLeaveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeaveViewModel by viewModels()
    private lateinit var adapter: LeaveAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = LeaveAdapter()
        binding.rvLeaveHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaveHistory.adapter = adapter

        binding.btnBackLeave.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSubmitLeave.setSafeClickListener {
            val dateFrom = binding.etDateFrom.text.toString().ifBlank { "2026-04-20" }
            val dateTo = binding.etDateTo.text.toString().ifBlank { "2026-04-21" }
            val reason = binding.etReason.text.toString().ifBlank { "Việc gia đình" }

            viewModel.submit(
                dateFrom = dateFrom,
                dateTo = dateTo,
                reason = reason
            )
        }

        binding.btnRefreshLeave.setSafeClickListener {
            viewModel.loadHistory()
        }

        viewModel.loadHistory()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressLeave.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE
            binding.tvLeaveResult.text = state.message
            adapter.submitList(state.items)

            if (!state.isLoading && state.message.startsWith("Đã gửi đơn nghỉ")) {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}